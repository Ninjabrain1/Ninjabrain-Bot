package ninjabrainbot.io.mcinstance;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.preferences.enums.McVersion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MacActiveInstanceListener implements IActiveInstanceProvider, Runnable {

    private int lastForegroundPid = -1;
    private final HashMap<String, MinecraftInstance> minecraftInstances;
    private final ObservableField<MinecraftInstance> activeMinecraftInstance;
    private final SavesReader savesReader;

    public static final class CF {
        public interface CoreFoundation extends Library {
            CoreFoundation INSTANCE = Native.load("CoreFoundation", CoreFoundation.class);

            class CFTypeRef extends PointerType {}
            class CFStringRef extends CFTypeRef {}
            class CFArrayRef extends CFTypeRef {}
            class CFDictionaryRef extends CFTypeRef {}
            class CFNumberRef extends CFTypeRef {}

            int kCFStringEncodingUTF8 = 0x08000100;
            int kCFNumberSInt32Type = 3;

            CFStringRef CFStringCreateWithCString(Pointer alloc, String cStr, int encoding);
            // CFArray
            long CFArrayGetCount(CFArrayRef theArray);
            Pointer CFArrayGetValueAtIndex(CFArrayRef theArray, long idx);

            // CFDictionary
            Pointer CFDictionaryGetValue(CFDictionaryRef theDict, Pointer key /* CFStringRef */);

            // CFNumber
            boolean CFNumberGetValue(CFNumberRef number, int theType, Pointer valuePtr);

            // Memory mgmt
            void CFRelease(CFTypeRef cfTypeRef);
        }

        public static CF.CoreFoundation.CFStringRef cfString(String s) {
            return CoreFoundation.INSTANCE.CFStringCreateWithCString(Pointer.NULL, s, CoreFoundation.kCFStringEncodingUTF8);
        }
        /** Release any JNA PointerType by wrapping as CFTypeRef. */
        public static void cfReleaseAny(PointerType any) {
            if (any != null && any.getPointer() != null) {
                CF.CoreFoundation.CFTypeRef tmp = new CF.CoreFoundation.CFTypeRef();
                tmp.setPointer(any.getPointer());
                CoreFoundation.INSTANCE.CFRelease(tmp);
            }
        }
    }

    // https://stackoverflow.com/questions/72443976/how-to-get-arguments-of-nsrunningapplication
    public interface LibC extends Library {
        LibC INSTANCE = Native.load("c", LibC.class);

        // int sysctl(int *name, u_int namelen, void *oldp, size_t *oldlenp, void *newp, size_t newlen);
        int sysctl(IntByReference name, int namelen, Pointer oldp, LongByReference oldlenp, Pointer newp, long newlen);
    }

    /** Read argv for a pid using sysctl(KERN_PROCARGS2). Returns null on failure. */
    private static String[] processArguments(int pid) {
        final int CTL_KERN = 1, KERN_PROCARGS2 = 49;

        int[] mibArr = { CTL_KERN, KERN_PROCARGS2, pid };
        Memory mibMem = new Memory((long) mibArr.length * 4);
        for (int i = 0; i < mibArr.length; i++) mibMem.setInt((long) i * 4, mibArr[i]);
        IntByReference mib = new IntByReference(); mib.setPointer(mibMem);

        LongByReference lenRef = new LongByReference();
        int rc = LibC.INSTANCE.sysctl(mib, mibArr.length, Pointer.NULL, lenRef, Pointer.NULL, 0);
        if (rc != 0 || lenRef.getValue() <= 0) return null;

        long needed = lenRef.getValue();
        if (needed > Integer.MAX_VALUE) return null;
        Memory buf = new Memory(needed);

        rc = LibC.INSTANCE.sysctl(mib, mibArr.length, buf, lenRef, Pointer.NULL, 0);
        if (rc != 0) return null;

        int argc = buf.getInt(0);
        long pos = 4;

        // Skip saved exec path
        while (pos < needed && buf.getByte(pos) != 0) pos++;
        if (pos >= needed) return null;

        // Skip trailing NULs
        while (pos < needed && buf.getByte(pos) == 0) pos++;
        if (pos >= needed) return null;

        java.util.ArrayList<String> out = new java.util.ArrayList<>(Math.max(argc, 4));
        for (int i = 0; i < argc; i++) {
            long start = pos;
            while (pos < needed && buf.getByte(pos) != 0) pos++;
            if (pos >= needed) return null;
            int len = (int) (pos - start);
            byte[] bytes = new byte[len];
            buf.read(start, bytes, 0, len);
            out.add(new String(bytes));
            pos++; // skip NUL
        }
        return out.toArray(new String[0]);
    }

    public static final class CG {
        public interface CoreGraphics extends Library {
            CoreGraphics INSTANCE = Native.load("CoreGraphics", CoreGraphics.class);
            // CFArrayRef CGWindowListCopyWindowInfo(uint32 options, uint32 relativeToWindow)
            Pointer CGWindowListCopyWindowInfo(int options, int relativeToWindow);
        }

        // Common CGWindowList options
        private static final int kCGWindowListOptionOnScreenOnly = 1;
        private static final int kCGWindowListOptionExcludeDesktopElements = 16;
        private static final int kCGNullWindowID = 0;

        // Dictionary keys we will read
        private static final CF.CoreFoundation.CFStringRef K_OWNER_PID = CF.cfString("kCGWindowOwnerPID");
        private static final CF.CoreFoundation.CFStringRef K_LAYER     = CF.cfString("kCGWindowLayer");

        /** Return PID of topmost layer-0 window; also returns its names via holder if provided. */
        public static int getFrontmostOwnerPID() {
            Pointer pArr = CoreGraphics.INSTANCE.CGWindowListCopyWindowInfo(
                    kCGWindowListOptionOnScreenOnly | kCGWindowListOptionExcludeDesktopElements,
                    kCGNullWindowID
            );
            if (pArr == null) return -1;
            CF.CoreFoundation.CFArrayRef arr = new CF.CoreFoundation.CFArrayRef();
            arr.setPointer(pArr);
            try {
                long count = CF.CoreFoundation.INSTANCE.CFArrayGetCount(arr);
                for (long i = 0; i < count; i++) {
                    Pointer dictPtr = CF.CoreFoundation.INSTANCE.CFArrayGetValueAtIndex(arr, i);
                    if (dictPtr == null) continue;
                    CF.CoreFoundation.CFDictionaryRef dict = new CF.CoreFoundation.CFDictionaryRef();
                    dict.setPointer(dictPtr);

                    // layer filter
                    Pointer layerPtr = CF.CoreFoundation.INSTANCE.CFDictionaryGetValue(dict, K_LAYER.getPointer());
                    int layer = readCFNumber(layerPtr);
                    if (layer != 0) continue;

                    // owner pid
                    Pointer pidPtr = CF.CoreFoundation.INSTANCE.CFDictionaryGetValue(dict, K_OWNER_PID.getPointer());
                    int pid = readCFNumber(pidPtr);
                    if (pid <= 0) continue;
                    return pid; // first layer-0 window is the frontmost
                }
                return -1;
            } finally {
                CF.cfReleaseAny(arr); // we own the array (Copy)
            }
        }

        private static int readCFNumber(Pointer pNum) {
            if (pNum == null) return 0;
            CF.CoreFoundation.CFNumberRef num = new CF.CoreFoundation.CFNumberRef();
            num.setPointer(pNum);
            Memory out = new Memory(4);
            boolean ok = CF.CoreFoundation.INSTANCE.CFNumberGetValue(num, CF.CoreFoundation.kCFNumberSInt32Type, out);
            return ok ? out.getInt(0) : 0;
        }
    }

    /** Derive PRE_119 vs POST_119 from argv (no window title, no native). */
    private McVersion getMinecraftVersion(int pid) {
        String[] args = processArguments(pid);
        if (args == null || args.length == 0) return null;

        String rawVersion = null;

        // Vanilla launcher: --version <ver>
        for (int i = 0; i < args.length - 1; i++) {
            if ("--version".equals(args[i])) {
                rawVersion = args[i + 1];
                break;
            }
        }

        // Fabric changes --version to fabric-loader-X.Y[.Z]-<mc>
        if (rawVersion != null && !rawVersion.isEmpty()) {
            rawVersion = rawVersion.replaceFirst("^fabric-loader-\\d+\\.\\d+(?:\\.\\d+)?-", "");
        }

        // If still unknown (Prism/MultiMC), try extracting from other args:
        if (rawVersion == null || rawVersion.isEmpty()) {
            java.util.regex.Pattern p1 = java.util.regex.Pattern.compile(".*minecraft-([0-9.]+?)-client\\.jar");
            java.util.regex.Pattern p2 = java.util.regex.Pattern.compile(".*/intermediary/([0-9.]+)/intermediary/.*");
            for (String a : args) {
                if (a == null) continue;
                java.util.regex.Matcher m1 = p1.matcher(a);
                if (m1.matches()) { rawVersion = m1.group(1); break; }
                java.util.regex.Matcher m2 = p2.matcher(a);
                if (m2.matches()) { rawVersion = m2.group(1); break; }
            }
        }

        if (rawVersion == null || rawVersion.isEmpty()) return null;

        // Map "1.x.y" → enum bucket
        String[] parts = rawVersion.split("\\.");
        if (parts.length < 2) return null;
        if (!"1".equals(parts[0])) return null;
        try {
            int minor = Integer.parseInt(parts[1]);
            return minor < 19 ? McVersion.PRE_119 : McVersion.POST_119;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    /** Extract the .minecraft path from argv (vanilla & Prism/MultiMC). Returns null if not found. */
    private String getDotMinecraftDirectoryFromProcessId(int pid) {
        String[] args = processArguments(pid);
        if (args == null || args.length == 0) return null;

        // Vanilla launcher: --gameDir <path>
        for (int i = 0; i < args.length - 1; i++) {
            if ("--gameDir".equals(args[i])) {
                String p = args[i + 1];
                return p != null && !p.isEmpty() ? p : null;
            }
        }

        // Prism/MultiMC/etc: -Djava.library.path=<...>/natives → try .../minecraft or .../.minecraft
        final String prefix = "-Djava.library.path=";
        for (String a : args) {
            if (a != null && a.startsWith(prefix)) {
                String raw = a.substring(prefix.length());
                String base = raw.endsWith("/natives") ? raw.substring(0, raw.length() - "/natives".length()) : raw;
                String c1 = base + "/minecraft";
                String c2 = base + "/.minecraft";
                if (new File(c1).isDirectory()) return c1;
                if (new File(c2).isDirectory()) return c2;
            }
        }

        return null;
    }

    public MacActiveInstanceListener() throws IOException {
        minecraftInstances = new HashMap<>();
        activeMinecraftInstance = new ObservableField<>(null);
        savesReader = new SavesReader(activeMinecraftInstance);

        Thread t = new Thread(this, "Active instance listener (macOS)");
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            savesReader.pollEvents();
            pollForegroundWindow();
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
    }

    @Override public IObservable<MinecraftInstance> activeMinecraftInstance() { return activeMinecraftInstance; }
    @Override public IObservable<IMinecraftWorldFile> activeMinecraftWorld() { return savesReader.activeMinecraftWorld(); }
    @Override public ISubscribable<IMinecraftWorldFile> whenActiveMinecraftWorldModified() { return savesReader.whenActiveMinecraftWorldModified(); }
    @Override public boolean supportsReadingActiveMinecraftWorld() { return true; }

    private void pollForegroundWindow() {
        Integer pid = getFrontmostProcessPid();
        if (pid == null || pid <= 0) return;
        if (pid == lastForegroundPid) return;
        lastForegroundPid = pid;

        if (!isJavaProcess(pid)) return;

        String mcDir = getDotMinecraftDirectoryFromProcessId(pid);
        if (mcDir == null || mcDir.isEmpty()) return;
        System.out.println(mcDir);
        minecraftInstances.computeIfAbsent(mcDir, MinecraftInstance::new);
        MinecraftInstance instance = minecraftInstances.get(mcDir);

        McVersion parsed = getMinecraftVersion(pid);
        if (instance.minecraftVersion == null) instance.minecraftVersion = parsed != null ? parsed : McVersion.POST_119;
        activeMinecraftInstance.set(instance);
    }

    private Integer getFrontmostProcessPid() {
        int pid = CG.getFrontmostOwnerPID();
        if (pid <= 0) return null;
        return pid;
    }

    private boolean isJavaProcess(int pid) {
        String[] cmd = {"bash", "-lc", "ps -p " + pid + " -o comm="};
        String comm = runAndReadFirstLine(cmd);
        if (comm == null) return false;
        comm = comm.trim().toLowerCase();
        return comm.endsWith("/java") || comm.equals("java");
    }

    private String runAndReadFirstLine(String[] command) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                return r.readLine();
            }
        } catch (IOException ignored) {
            return null;
        } finally {
            if (p != null) try { p.destroy(); } catch (Exception ignored) {}
        }
    }
}
