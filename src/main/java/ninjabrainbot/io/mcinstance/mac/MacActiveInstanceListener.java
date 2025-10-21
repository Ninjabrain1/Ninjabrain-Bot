package ninjabrainbot.io.mcinstance.mac;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.mcinstance.MinecraftInstance;
import ninjabrainbot.io.mcinstance.SavesReader;
import ninjabrainbot.io.preferences.enums.McVersion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sun.jna.platform.mac.SystemB.INT_SIZE;

public class MacActiveInstanceListener implements IActiveInstanceProvider, Runnable {

	private int lastForegroundPid = -1;
	private final HashMap<String, MinecraftInstance> minecraftInstances;
	private final ObservableField<MinecraftInstance> activeMinecraftInstance;
	private final SavesReader savesReader;

	public MacActiveInstanceListener() throws IOException {
		minecraftInstances = new HashMap<>();
		activeMinecraftInstance = new ObservableField<>(null);
		savesReader = new SavesReader(activeMinecraftInstance);

		Thread activeInstanceListenerThread = new Thread(this, "Active instance listener (macOS)");
		activeInstanceListenerThread.setDaemon(true);
		activeInstanceListenerThread.start();
	}

	@Override
	public void run() {
		while (true) {
			savesReader.pollEvents();
			pollForegroundWindow();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IObservable<MinecraftInstance> activeMinecraftInstance() {
		return activeMinecraftInstance;
	}

	@Override
	public IObservable<IMinecraftWorldFile> activeMinecraftWorld() {
		return savesReader.activeMinecraftWorld();
	}

	@Override
	public ISubscribable<IMinecraftWorldFile> whenActiveMinecraftWorldModified() {
		return savesReader.whenActiveMinecraftWorldModified();
	}

	@Override
	public boolean supportsReadingActiveMinecraftWorld() {
		return true;
	}

	/**
	 * Read argv for a pid using sysctl(KERN_PROCARGS2). Returns null on failure.
	 * Adapted from this swift code:
	 * <a href="https://stackoverflow.com/questions/72443976/how-to-get-arguments-of-nsrunningapplication">stackoverflow post</a>
	 */
	private static String[] processArguments(int pid) {
		final int CTL_KERN = 1, KERN_PROCARGS2 = 49;

		int[] nameArray = {CTL_KERN, KERN_PROCARGS2, pid};
		Memory nameMemory = new Memory((long) nameArray.length * INT_SIZE);
		for (int i = 0; i < nameArray.length; i++)
			nameMemory.setInt((long) i * INT_SIZE, nameArray[i]);
		IntByReference name = new IntByReference();
		name.setPointer(nameMemory);

		LongByReference requiredSpaceRef = new LongByReference();
		// returnCode 0 means success, -1 means failure
		int returnCode = ILibC.INSTANCE.sysctl(name, nameArray.length, Pointer.NULL, requiredSpaceRef, Pointer.NULL, 0);
		if (returnCode != 0 || requiredSpaceRef.getValue() <= 0)
			return null;

		long requiredSpace = requiredSpaceRef.getValue();
		if (requiredSpace > Integer.MAX_VALUE)
			return null;
		Memory argcBuffer = new Memory(requiredSpace);

		returnCode = ILibC.INSTANCE.sysctl(name, nameArray.length, argcBuffer, requiredSpaceRef, Pointer.NULL, 0);
		if (returnCode != 0)
			return null;

		int argc = argcBuffer.getInt(0);

		// sizeof (int), in order to start after argc.
		long position = INT_SIZE;

		// Skip saved exec path
		while (position < requiredSpace && argcBuffer.getByte(position) != 0)
			position++;

		if (position >= requiredSpace)
			return null;

		// Skip trailing NULs
		while (position < requiredSpace && argcBuffer.getByte(position) == 0)
			position++;

		if (position >= requiredSpace)
			return null;

		ArrayList<String> out = new ArrayList<>(Math.max(argc, 4));
		for (int i = 0; i < argc; i++) {
			long start = position;
			while (position < requiredSpace && argcBuffer.getByte(position) != 0)
				position++;
			if (position >= requiredSpace)
				return null;
			int len = (int) (position - start);
			byte[] bytes = new byte[len];
			argcBuffer.read(start, bytes, 0, len);
			out.add(new String(bytes));
			position++; // skip NUL
		}
		return out.toArray(new String[0]);
	}

	/**
	 * Derive PRE_119 vs POST_119 from argv.
	 */
	private McVersion getMinecraftVersion(int pid) {
		String[] args = processArguments(pid);
		if (args == null || args.length == 0)
			return null;

		return getMinecraftVersionFromArgs(args);
	}

	private static McVersion getMinecraftVersionFromArgs(String[] args) {
		String versionString = null;

		// Vanilla launcher: --version <ver>
		for (int i = 0; i < args.length - 1; i++) {
			if ("--version".equals(args[i])) {
				versionString = args[i + 1];
				break;
			}
		}

		// Fabric changes --version to fabric-loader-X.Y[.Z]-<mc>
		if (versionString != null && !versionString.isEmpty()) {
			versionString = versionString.replaceFirst("^fabric-loader-\\d+\\.\\d+(?:\\.\\d+)?-", "");
		}

		// If still unknown (Prism/MultiMC), try extracting from other args:
		if (versionString == null || versionString.isEmpty()) {
			Pattern clientJarVersionPattern = Pattern.compile(".*minecraft-([0-9.]+?)-client\\.jar");
			Pattern intermediaryVersionPattern = Pattern.compile(".*/intermediary/([0-9.]+)/intermediary/.*");
			for (String arg : args) {
				if (arg == null)
					continue;
				Matcher matcher1 = clientJarVersionPattern.matcher(arg);
				if (matcher1.matches()) {
					versionString = matcher1.group(1);
					break;
				}
				Matcher matcher2 = intermediaryVersionPattern.matcher(arg);
				if (matcher2.matches()) {
					versionString = matcher2.group(1);
					break;
				}
			}
		}

		if (versionString == null || versionString.isEmpty())
			return null;

		// Map "1.x.y" → enum bucket
		String[] versionNumbers = versionString.split("\\.");
		if (versionNumbers.length < 2)
			return null;
		if (!"1".equals(versionNumbers[0]))
			return null;
		try {
			int minor = Integer.parseInt(versionNumbers[1]);
			return minor < 19 ? McVersion.PRE_119 : McVersion.POST_119;
		} catch (NumberFormatException ignored) {
			return null;
		}
	}

	/**
	 * Extract the .minecraft path from argv (vanilla & Prism/MultiMC). Returns null if not found.
	 */
	private String getMinecraftDirectoryFromProcessId(int pid) {
		String[] args = processArguments(pid);
		if (args == null || args.length == 0)
			return null;

		// Vanilla launcher: --gameDir <path>
		for (int i = 0; i < args.length - 1; i++) {
			if ("--gameDir".equals(args[i])) {
				String gameDirectory = args[i + 1];
				return gameDirectory != null && !gameDirectory.isEmpty() ? gameDirectory : null;
			}
		}

		// Prism/MultiMC/etc: -Djava.library.path=<...>/natives -> try .../minecraft or .../.minecraft
		final String prefix = "-Djava.library.path=";
		for (String arg : args) {
			if (arg != null && arg.startsWith(prefix)) {
				String nativesDir = arg.substring(prefix.length());
				String baseDir = nativesDir.endsWith("/natives") ? nativesDir.substring(0, nativesDir.length() - "/natives".length()) : nativesDir;
				String minecraftDir1 = baseDir + "/minecraft";
				String minecraftDir2 = baseDir + "/.minecraft";
				if (new File(minecraftDir1).isDirectory())
					return minecraftDir1;
				if (new File(minecraftDir2).isDirectory())
					return minecraftDir2;
			}
		}

		return null;
	}

	private void pollForegroundWindow() {
		Integer pid = getFrontmostProcessPid();
		if (pid == null || pid <= 0)
			return;
		if (pid == lastForegroundPid)
			return;
		lastForegroundPid = pid;

		if (!isJavaProcess(pid))
			return;

		String minecraftDirectory = getMinecraftDirectoryFromProcessId(pid);
		if (minecraftDirectory == null || minecraftDirectory.isEmpty())
			return;
		minecraftInstances.computeIfAbsent(minecraftDirectory, MinecraftInstance::new);
		MinecraftInstance instance = minecraftInstances.get(minecraftDirectory);

		McVersion parsedVersion = getMinecraftVersion(pid);
		if (instance.getMcVersion() == null) {
			instance.setMcVersion(parsedVersion);
		}
		activeMinecraftInstance.set(instance);
	}

	private Integer getFrontmostProcessPid() {
		int pid = CoreGraphics.getFrontmostOwnerPID();
		if (pid <= 0)
			return null;
		return pid;
	}

	private boolean isJavaProcess(int pid) {
		String[] getProcessExecutableCommand = {"bash", "-lc", "ps -p " + pid + " -o comm="};
		String executable = runCommandAndReadFirstLine(getProcessExecutableCommand);
		if (executable == null)
			return false;
		executable = executable.trim().toLowerCase();
		return executable.endsWith("/java") || executable.equals("java");
	}

	private String runCommandAndReadFirstLine(String[] command) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				return reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (process != null) {
				try {
					process.destroy();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
