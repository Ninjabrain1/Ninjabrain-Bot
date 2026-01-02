package ninjabrainbot.io;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class KeyPresser {

	Robot robot;
	final int CTRL_KEYCODE;
	final boolean isMacOS;
	
	static final Set<Character> SHIFT_CHARS = new HashSet<>();
	static final Map<Character, Integer> SPECIAL_KEY_CODES = new HashMap<>();
	static {
		String shiftChars = "!@#$%^&*()_+{}|:\"<>?~";
		for (char c : shiftChars.toCharArray()) {
			SHIFT_CHARS.add(c);
		}
		
		SPECIAL_KEY_CODES.put('!', KeyEvent.VK_1);
		SPECIAL_KEY_CODES.put('@', KeyEvent.VK_2);
		SPECIAL_KEY_CODES.put('#', KeyEvent.VK_3);
		SPECIAL_KEY_CODES.put('$', KeyEvent.VK_4);
		SPECIAL_KEY_CODES.put('%', KeyEvent.VK_5);
		SPECIAL_KEY_CODES.put('^', KeyEvent.VK_6);
		SPECIAL_KEY_CODES.put('&', KeyEvent.VK_7);
		SPECIAL_KEY_CODES.put('*', KeyEvent.VK_8);
		SPECIAL_KEY_CODES.put('(', KeyEvent.VK_9);
		SPECIAL_KEY_CODES.put(')', KeyEvent.VK_0);
		SPECIAL_KEY_CODES.put('_', KeyEvent.VK_MINUS);
		SPECIAL_KEY_CODES.put('+', KeyEvent.VK_EQUALS);
		SPECIAL_KEY_CODES.put('{', KeyEvent.VK_OPEN_BRACKET);
		SPECIAL_KEY_CODES.put('}', KeyEvent.VK_CLOSE_BRACKET);
		SPECIAL_KEY_CODES.put('|', KeyEvent.VK_BACK_SLASH);
		SPECIAL_KEY_CODES.put(':', KeyEvent.VK_SEMICOLON);
		SPECIAL_KEY_CODES.put('"', KeyEvent.VK_QUOTE);
		SPECIAL_KEY_CODES.put('<', KeyEvent.VK_COMMA);
		SPECIAL_KEY_CODES.put('>', KeyEvent.VK_PERIOD);
		SPECIAL_KEY_CODES.put('?', KeyEvent.VK_SLASH);
		SPECIAL_KEY_CODES.put('~', KeyEvent.VK_BACK_QUOTE);
		
		SPECIAL_KEY_CODES.put('.', KeyEvent.VK_PERIOD);
		SPECIAL_KEY_CODES.put('-', KeyEvent.VK_MINUS);
		SPECIAL_KEY_CODES.put(',', KeyEvent.VK_COMMA);
		SPECIAL_KEY_CODES.put('/', KeyEvent.VK_SLASH);
		SPECIAL_KEY_CODES.put(';', KeyEvent.VK_SEMICOLON);
		SPECIAL_KEY_CODES.put('\'', KeyEvent.VK_QUOTE);
		SPECIAL_KEY_CODES.put('[', KeyEvent.VK_OPEN_BRACKET);
		SPECIAL_KEY_CODES.put(']', KeyEvent.VK_CLOSE_BRACKET);
		SPECIAL_KEY_CODES.put('\\', KeyEvent.VK_BACK_SLASH);
		SPECIAL_KEY_CODES.put('`', KeyEvent.VK_BACK_QUOTE);
		SPECIAL_KEY_CODES.put('=', KeyEvent.VK_EQUALS);
	}

	public KeyPresser() throws AWTException {
		robot = new Robot();
		String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		isMacOS = osName.equals("mac os x");
		if (isMacOS) {
			CTRL_KEYCODE = KeyEvent.VK_META;
		} else {
			CTRL_KEYCODE = KeyEvent.VK_CONTROL;
		}
	}

	public void paste(String s) throws InterruptedException {
		if (isMacOS) {
			typeString(s);
		} else {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection(s);
			clipboard.setContents(selection, null);
			pasteWithRobot();
		}
	}
	
	private void typeString(String s) throws InterruptedException {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			typeChar(c);
		}
	}
	
	private void typeChar(char c) throws InterruptedException {
		// XXX(itisapachee): Maybe remove some of waitForIdle() calls to speed up typing.

		int keyCode;
		boolean needsShift = false;
		
		if (SPECIAL_KEY_CODES.containsKey(c)) {
			keyCode = SPECIAL_KEY_CODES.get(c);
			needsShift = SHIFT_CHARS.contains(c);
		} else if (Character.isUpperCase(c)) {
			keyCode = KeyEvent.getExtendedKeyCodeForChar(Character.toLowerCase(c));
			needsShift = true;
		} else {
			keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
			needsShift = false;
		}
		
		if (keyCode == 0) {
			throw new RuntimeException("Character '" + c + "' not supported for key press.");
		}
		
		if (needsShift) {
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.waitForIdle();
		}
		
		robot.keyPress(keyCode);
		robot.waitForIdle();
		robot.keyRelease(keyCode);
		robot.waitForIdle();
		
		if (needsShift) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.waitForIdle();
		}
	}
	
	private void pasteWithRobot() throws InterruptedException {
		robot.keyPress(CTRL_KEYCODE);
		robot.waitForIdle();
		robot.keyPress(KeyEvent.VK_V);
		robot.waitForIdle();
		Thread.sleep(50);
		robot.keyRelease(KeyEvent.VK_V);
		robot.waitForIdle();
		robot.keyRelease(CTRL_KEYCODE);
		robot.waitForIdle();
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
