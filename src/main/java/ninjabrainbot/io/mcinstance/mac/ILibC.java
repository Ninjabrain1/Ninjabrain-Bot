package ninjabrainbot.io.mcinstance.mac;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

public interface ILibC extends Library {
	ILibC INSTANCE = Native.load("c", ILibC.class);

	// C signature:
	// int sysctl(int *name, u_int namelen, void *oldp, size_t *oldlenp, void *newp, size_t newlen);
	int sysctl(IntByReference name, int namelen, Pointer oldp, LongByReference oldlenp, Pointer newp, long newlen);
}
