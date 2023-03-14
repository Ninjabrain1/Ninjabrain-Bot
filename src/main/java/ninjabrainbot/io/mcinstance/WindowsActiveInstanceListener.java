package ninjabrainbot.io.mcinstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.sun.jna.Native;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;

public class WindowsActiveInstanceListener implements IActiveInstanceProvider, Runnable {

	private int lastForegroundWindowProcessId = -1;

	private HashMap<String, MinecraftInstance> minecraftInstances;
	private ObservableField<MinecraftInstance> activeMinecraftInstance;

	private SavesReader savesReader;

	WindowsActiveInstanceListener() throws IOException {
		minecraftInstances = new HashMap<>();
		activeMinecraftInstance = new ObservableField<MinecraftInstance>(null);

		savesReader = new SavesReader(activeMinecraftInstance);

		Thread activeInstanceListenerThread = new Thread(this, "Active instance listener");
		activeInstanceListenerThread.start();
	}

	@Override
	public void run() {
		while (true) {
			savesReader.pollEvents();
			pollForegroundWindow();
			try {
				Thread.sleep(1000);
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

	private void pollForegroundWindow() {
		HWND foregroundWindowHandle = User32.INSTANCE.GetForegroundWindow();
		if (foregroundWindowHandle == null)
			return;

		int processId = getWindowProcessId(foregroundWindowHandle);
		if (lastForegroundWindowProcessId == processId)
			return;
		lastForegroundWindowProcessId = processId;

		String windowTitle = getWindowTitle(foregroundWindowHandle);
		if (!isWindowMinecraft(foregroundWindowHandle, windowTitle))
			return;

		String dotMinecraftDirectory = getDotMinecraftDirectoryFromProcessId(processId);
		if (dotMinecraftDirectory == null)
			return;

		if (!minecraftInstances.containsKey(dotMinecraftDirectory))
			minecraftInstances.put(dotMinecraftDirectory, new MinecraftInstance(dotMinecraftDirectory));

		MinecraftInstance minecraftInstance = minecraftInstances.get(dotMinecraftDirectory);
		if (minecraftInstance.minecraftVersion == null)
			minecraftInstance.minecraftVersion = GetMinecraftVersion(windowTitle);

		activeMinecraftInstance.set(minecraftInstance);
	}

	private String getWindowTitle(HWND windowHandle) {
		char[] windowTitle = new char[1024 * 2];
		User32.INSTANCE.GetWindowText(windowHandle, windowTitle, 1024);
		return Native.toString(windowTitle);
	}

	private boolean isWindowMinecraft(HWND windowHandle, String windowTitle) {
		if (!windowTitle.startsWith("Minecraft"))
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

	private String getDotMinecraftDirectoryFromProcessId(int pid) {
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

	private String getDotMinecraftDirectory(String nativesJvmArgument) {
		int dotMinecraftIndex = nativesJvmArgument.lastIndexOf(".minecraft");
		if (dotMinecraftIndex != -1)
			return nativesJvmArgument.substring(19, dotMinecraftIndex + 10);

		if (nativesJvmArgument.endsWith("natives"))
			return nativesJvmArgument.substring(19).replace("natives", ".minecraft");

		return null;
	}

	private McVersion GetMinecraftVersion(String windowTitle) {
		String[] titleWords = windowTitle.split(" ");
		if (titleWords.length <= 1)
			return null;

		String[] versionNumbers = titleWords[1].split("\\.");
		if (versionNumbers.length <= 1)
			return null;

		String majorVersion = versionNumbers[0];
		if (!majorVersion.contentEquals("1"))
			return null;

		int minorVersion;
		try {
			minorVersion = Integer.parseInt(versionNumbers[1]);
		} catch (NumberFormatException e) {
			return null;
		}

		return minorVersion < 19 ? McVersion.PRE_119 : McVersion.POST_119;
	}

}
