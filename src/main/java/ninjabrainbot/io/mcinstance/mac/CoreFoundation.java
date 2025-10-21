package ninjabrainbot.io.mcinstance.mac;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public final class CoreFoundation {

	public static ICoreFoundationLib.CFStringRef cfString(String s) {
		return ICoreFoundationLib.INSTANCE.CFStringCreateWithCString(Pointer.NULL, s, ICoreFoundationLib.kCFStringEncodingUTF8);
	}

	/**
	 * Release any JNA PointerType by wrapping as CFTypeRef.
	 */
	public static void cfReleaseAny(PointerType any) {
		if (any != null && any.getPointer() != null) {
			ICoreFoundationLib.CFTypeRef tmp = new ICoreFoundationLib.CFTypeRef();
			tmp.setPointer(any.getPointer());
			ICoreFoundationLib.INSTANCE.CFRelease(tmp);
		}
	}
}
