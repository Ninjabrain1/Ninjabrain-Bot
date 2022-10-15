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

	public void paste(String s) {
//		System.out.println(1);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//		System.out.println(2);
		StringSelection selection = new StringSelection(s);
//		System.out.println(3);
		clipboard.setContents(selection, null);
//		System.out.println(4);
		robot.keyPress(CTRL_KEYCODE);
//		System.out.println(5);
		robot.keyPress(KeyEvent.VK_V);
//		System.out.println(6);
		robot.keyRelease(CTRL_KEYCODE);
//		System.out.println(7);
		robot.keyRelease(KeyEvent.VK_V);
//		System.out.println(8);
	}

	public void releaseF3C() {
		robot.keyRelease(KeyEvent.VK_F3);
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
