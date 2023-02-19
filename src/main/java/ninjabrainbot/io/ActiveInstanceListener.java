package ninjabrainbot.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.jna.Native;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;

public class ActiveInstanceListener implements Runnable {

	private int lastForegroundWindowProcessId = -1;

	private ObservableField<File> activeMinecraftInstance;

	public ActiveInstanceListener() {
		activeMinecraftInstance = new ObservableField<File>(null);
	}

	@Override
	public void run() {
		while (true) {
			poll();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ISubscribable<File> whenActiveMinecraftInstanceChanged() {
		return activeMinecraftInstance;
	}

	private void poll() {
		HWND foregroundWindowHandle = User32.INSTANCE.GetForegroundWindow();
		if (foregroundWindowHandle == null)
			return;

		int processId = getWindowProcessId(foregroundWindowHandle);
		if (lastForegroundWindowProcessId == processId)
			return;
		lastForegroundWindowProcessId = processId;

		if (isWindowMinecraft(foregroundWindowHandle)) {
			activeMinecraftInstance.set(getMinecraftInstanceDirectoryFromProcessId(processId));
			System.out.println(activeMinecraftInstance.get());
		}
	}

	private boolean isWindowMinecraft(HWND windowHandle) {
		char[] windowTitle = new char[1024 * 2];

		User32.INSTANCE.GetWindowText(windowHandle, windowTitle, 1024);

		if (!Native.toString(windowTitle).contains("Minecraft"))
			return false;

		if (!WindowUtils.getProcessFilePath(windowHandle).contains("javaw.exe"))
			return false;

		return true;
	}

	private int getWindowProcessId(HWND windowHandle) {
		final IntByReference pid = new IntByReference();
		User32.INSTANCE.GetWindowThreadProcessId(windowHandle, pid);
		return pid.getValue();
	}

	private File getMinecraftInstanceDirectoryFromProcessId(int pid) {
		Runtime runtime = Runtime.getRuntime();
		String[] commands = { "jcmd", "" + pid, "VM.command_line" };
		try {
			Process process = runtime.exec(commands);

			BufferedReader commandOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String vmArgument = null;
			while ((vmArgument = commandOutputReader.readLine()) != null) {
				if (vmArgument.startsWith("jvm_args")) {
					for (String jvmArgument : vmArgument.split(" -")) {
						if (jvmArgument.startsWith("Djava.library.path")) {
							process.destroy();
							commandOutputReader.close();
							return getDotMinecraftDirectory(jvmArgument.strip());
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private File getDotMinecraftDirectory(String nativesJvmArgument) {
		int dotMinecraftIndex = nativesJvmArgument.lastIndexOf(".minecraft");
		if (dotMinecraftIndex != -1)
			return new File(nativesJvmArgument.substring(19, dotMinecraftIndex + 10));

		if (nativesJvmArgument.endsWith("natives"))
			return new File(nativesJvmArgument.substring(19).replace("natives", ".minecraft"));

		return null;
	}

}
