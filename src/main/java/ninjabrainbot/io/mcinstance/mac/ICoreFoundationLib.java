package ninjabrainbot.io.mcinstance.mac;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public interface ICoreFoundationLib extends Library {
	class CFTypeRef extends PointerType {}
	class CFStringRef extends CFTypeRef {}
	class CFArrayRef extends CFTypeRef {}
	class CFDictionaryRef extends CFTypeRef {}
	class CFNumberRef extends CFTypeRef {}

	ICoreFoundationLib INSTANCE = Native.load("CoreFoundation", ICoreFoundationLib.class);

	int kCFStringEncodingUTF8 = 0x08000100;
	int kCFNumberSInt32Type = 3;

	CFStringRef CFStringCreateWithCString(Pointer alloc, String cStr, int encoding);

	long CFArrayGetCount(CFArrayRef theArray);

	// CFArray
	Pointer CFArrayGetValueAtIndex(CFArrayRef theArray, long idx);

	// CFDictionary
	Pointer CFDictionaryGetValue(CFDictionaryRef theDict, Pointer key /* CFStringRef */);

	// CFNumber
	boolean CFNumberGetValue(CFNumberRef number, int theType, Pointer valuePtr);

	// Memory mgmt
	void CFRelease(CFTypeRef cfTypeRef);
}
