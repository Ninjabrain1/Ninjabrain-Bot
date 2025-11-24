package ninjabrainbot.io.mcinstance.mac;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface ICoreGraphicsLib extends Library {
	ICoreGraphicsLib INSTANCE = Native.load("CoreGraphics", ICoreGraphicsLib.class);

	// CFArrayRef CGWindowListCopyWindowInfo(uint32 options, uint32 relativeToWindow)
	Pointer CGWindowListCopyWindowInfo(int options, int relativeToWindow);
}
