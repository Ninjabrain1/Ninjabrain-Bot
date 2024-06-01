package ninjabrainbot.io;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.Locale;

public class KeyPresser {

	Robot robot;
	final int CTRL_KEYCODE;

	public KeyPresser() throws AWTException {
		robot = new Robot();
		String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		if (osName.equals("mac os x")) {
			CTRL_KEYCODE = KeyEvent.VK_META;
		} else {
			CTRL_KEYCODE = KeyEvent.VK_CONTROL;
		}
	}

	public void paste(String s) throws InterruptedException {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(s);
		clipboard.setContents(selection, null);
		robot.keyPress(CTRL_KEYCODE);
		Thread.sleep(50);
		robot.keyPress(KeyEvent.VK_V);
		Thread.sleep(60);
		robot.keyRelease(CTRL_KEYCODE);
		Thread.sleep(50);
		robot.keyRelease(KeyEvent.VK_V);
	}

	public void releaseC() {
		robot.keyRelease(KeyEvent.VK_C);
	}

	public void openCommand() {
		press(KeyEvent.VK_K);
	}

	public void enter() {
		press(KeyEvent.VK_ENTER);
	}

	public void press(int key) {
		robot.keyPress(key);
		robot.keyRelease(key);
	}

}
