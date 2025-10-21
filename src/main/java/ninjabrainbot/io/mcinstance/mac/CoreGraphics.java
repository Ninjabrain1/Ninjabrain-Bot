package ninjabrainbot.io.mcinstance.mac;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import ninjabrainbot.io.mcinstance.mac.ICoreFoundationLib.CFArrayRef;
import ninjabrainbot.io.mcinstance.mac.ICoreFoundationLib.CFNumberRef;
import ninjabrainbot.io.mcinstance.mac.ICoreFoundationLib.CFStringRef;

import static com.sun.jna.platform.mac.SystemB.INT_SIZE;

public final class CoreGraphics {

	// Common CGWindowList options
	private static final int kCGWindowListOptionOnScreenOnly = 1;
	private static final int kCGWindowListOptionExcludeDesktopElements = 16;
	private static final int kCGNullWindowID = 0;

	// Dictionary keys we will read
	private static final CFStringRef K_OWNER_PID = CoreFoundation.cfString("kCGWindowOwnerPID");
	private static final CFStringRef K_LAYER = CoreFoundation.cfString("kCGWindowLayer");

	/**
	 * Return PID of topmost layer-0 window; also returns its names via holder if provided.
	 */
	public static int getFrontmostOwnerPID() {
		Pointer arrayPointer = ICoreGraphicsLib.INSTANCE.CGWindowListCopyWindowInfo(
				kCGWindowListOptionOnScreenOnly | kCGWindowListOptionExcludeDesktopElements,
				kCGNullWindowID
		);
		if (arrayPointer == null)
			return -1;
		CFArrayRef array = new CFArrayRef();
		array.setPointer(arrayPointer);
		try {
			long count = ICoreFoundationLib.INSTANCE.CFArrayGetCount(array);
			for (long i = 0; i < count; i++) {
				Pointer dictPtr = ICoreFoundationLib.INSTANCE.CFArrayGetValueAtIndex(array, i);
				if (dictPtr == null)
					continue;
				ICoreFoundationLib.CFDictionaryRef dict = new ICoreFoundationLib.CFDictionaryRef();
				dict.setPointer(dictPtr);

				// layer filter
				Pointer layerPtr = ICoreFoundationLib.INSTANCE.CFDictionaryGetValue(dict, K_LAYER.getPointer());
				int layer = readCFNumber(layerPtr);
				if (layer != 0)
					continue;

				// owner pid
				Pointer pidPtr = ICoreFoundationLib.INSTANCE.CFDictionaryGetValue(dict, K_OWNER_PID.getPointer());
				int pid = readCFNumber(pidPtr);
				if (pid <= 0)
					continue;
				return pid; // first layer-0 window is the frontmost
			}
			return -1;
		} finally {
			CoreFoundation.cfReleaseAny(array); // we own the array (Copy)
		}
	}

	private static int readCFNumber(Pointer numPointer) {
		if (numPointer == null)
			return 0;
		CFNumberRef num = new CFNumberRef();
		num.setPointer(numPointer);
		Memory out = new Memory(INT_SIZE);
		boolean ok = ICoreFoundationLib.INSTANCE.CFNumberGetValue(num, ICoreFoundationLib.kCFNumberSInt32Type, out);
		return ok ? out.getInt(0) : 0;
	}
}
