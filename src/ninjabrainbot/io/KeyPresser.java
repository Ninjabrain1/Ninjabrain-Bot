package ninjabrainbot.io;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class KeyPresser {
	
	Robot robot;
	
	public KeyPresser() throws AWTException {
		robot = new Robot();
	}
	
	public void paste(String s) {
//		System.out.println(1);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//		System.out.println(2);
		StringSelection selection = new StringSelection(s);
//		System.out.println(3);
		clipboard.setContents(selection, null);
//		System.out.println(4);
		robot.keyPress(KeyEvent.VK_CONTROL);
//		System.out.println(5);
		robot.keyPress(KeyEvent.VK_V);
//		System.out.println(6);
		robot.keyRelease(KeyEvent.VK_CONTROL);
//		System.out.println(7);
		robot.keyRelease(KeyEvent.VK_V);
//		System.out.println(8);
	}
	
	public void releaseF3C() {
		robot.keyRelease(KeyEvent.VK_F3);
		robot.keyRelease(KeyEvent.VK_C);
	}
	
	public void openCommand() {
		press(KeyEvent.VK_MINUS);
	}
	
	public void enter() {
		press(KeyEvent.VK_ENTER);
	}
	
	public void press(int key) {
		robot.keyPress(key);
		robot.keyRelease(key);
	}
	
}

