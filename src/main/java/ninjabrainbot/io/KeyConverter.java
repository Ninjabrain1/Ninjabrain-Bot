package ninjabrainbot.io;

import java.awt.event.KeyEvent;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.SwingKeyAdapter;

public class KeyConverter extends SwingKeyAdapter {

	public int convertNativeKeyCodeToKeyCode(int nativeKeyCode) {
		NativeKeyEvent nativeKeyEvent = new NativeKeyEvent(0, 0, 0, nativeKeyCode, (char) 0);
		return getJavaKeyEvent(nativeKeyEvent).getKeyCode();
	}

	public static int convertKeyCodeToNativeKeyCode(int keyCode) {
		switch (keyCode) {
			case KeyEvent.VK_ESCAPE:
				return NativeKeyEvent.VC_ESCAPE;
			case KeyEvent.VK_F1:
				return NativeKeyEvent.VC_F1;
			case KeyEvent.VK_F2:
				return NativeKeyEvent.VC_F2;
			case KeyEvent.VK_F3:
				return NativeKeyEvent.VC_F3;
			case KeyEvent.VK_F4:
				return NativeKeyEvent.VC_F4;
			case KeyEvent.VK_F5:
				return NativeKeyEvent.VC_F5;
			case KeyEvent.VK_F6:
				return NativeKeyEvent.VC_F6;
			case KeyEvent.VK_F7:
				return NativeKeyEvent.VC_F7;
			case KeyEvent.VK_F8:
				return NativeKeyEvent.VC_F8;
			case KeyEvent.VK_F9:
				return NativeKeyEvent.VC_F9;
			case KeyEvent.VK_F10:
				return NativeKeyEvent.VC_F10;
			case KeyEvent.VK_F11:
				return NativeKeyEvent.VC_F11;
			case KeyEvent.VK_F12:
				return NativeKeyEvent.VC_F12;
			case KeyEvent.VK_F13:
				return NativeKeyEvent.VC_F13;
			case KeyEvent.VK_F14:
				return NativeKeyEvent.VC_F14;
			case KeyEvent.VK_F15:
				return NativeKeyEvent.VC_F15;
			case KeyEvent.VK_F16:
				return NativeKeyEvent.VC_F16;
			case KeyEvent.VK_F17:
				return NativeKeyEvent.VC_F17;
			case KeyEvent.VK_F18:
				return NativeKeyEvent.VC_F18;
			case KeyEvent.VK_F19:
				return NativeKeyEvent.VC_F19;
			case KeyEvent.VK_F20:
				return NativeKeyEvent.VC_F20;
			case KeyEvent.VK_F21:
				return NativeKeyEvent.VC_F21;
			case KeyEvent.VK_F22:
				return NativeKeyEvent.VC_F22;
			case KeyEvent.VK_F23:
				return NativeKeyEvent.VC_F23;
			case KeyEvent.VK_F24:
				return NativeKeyEvent.VC_F24;
			case KeyEvent.VK_BACK_QUOTE:
				return NativeKeyEvent.VC_BACKQUOTE;
			case KeyEvent.VK_1:
				return NativeKeyEvent.VC_1;
			case KeyEvent.VK_2:
				return NativeKeyEvent.VC_2;
			case KeyEvent.VK_3:
				return NativeKeyEvent.VC_3;
			case KeyEvent.VK_4:
				return NativeKeyEvent.VC_4;
			case KeyEvent.VK_5:
				return NativeKeyEvent.VC_5;
			case KeyEvent.VK_6:
				return NativeKeyEvent.VC_6;
			case KeyEvent.VK_7:
				return NativeKeyEvent.VC_7;
			case KeyEvent.VK_8:
				return NativeKeyEvent.VC_8;
			case KeyEvent.VK_9:
				return NativeKeyEvent.VC_9;
			case KeyEvent.VK_0:
				return NativeKeyEvent.VC_0;
			case KeyEvent.VK_MINUS:
				return NativeKeyEvent.VC_MINUS;
			case KeyEvent.VK_EQUALS:
				return NativeKeyEvent.VC_EQUALS;
			case KeyEvent.VK_BACK_SPACE:
				return NativeKeyEvent.VC_BACKSPACE;
			case KeyEvent.VK_TAB:
				return NativeKeyEvent.VC_TAB;
			case KeyEvent.VK_CAPS_LOCK:
				return NativeKeyEvent.VC_CAPS_LOCK;
			case KeyEvent.VK_A:
				return NativeKeyEvent.VC_A;
			case KeyEvent.VK_B:
				return NativeKeyEvent.VC_B;
			case KeyEvent.VK_C:
				return NativeKeyEvent.VC_C;
			case KeyEvent.VK_D:
				return NativeKeyEvent.VC_D;
			case KeyEvent.VK_E:
				return NativeKeyEvent.VC_E;
			case KeyEvent.VK_F:
				return NativeKeyEvent.VC_F;
			case KeyEvent.VK_G:
				return NativeKeyEvent.VC_G;
			case KeyEvent.VK_H:
				return NativeKeyEvent.VC_H;
			case KeyEvent.VK_I:
				return NativeKeyEvent.VC_I;
			case KeyEvent.VK_J:
				return NativeKeyEvent.VC_J;
			case KeyEvent.VK_K:
				return NativeKeyEvent.VC_K;
			case KeyEvent.VK_L:
				return NativeKeyEvent.VC_L;
			case KeyEvent.VK_M:
				return NativeKeyEvent.VC_M;
			case KeyEvent.VK_N:
				return NativeKeyEvent.VC_N;
			case KeyEvent.VK_O:
				return NativeKeyEvent.VC_O;
			case KeyEvent.VK_P:
				return NativeKeyEvent.VC_P;
			case KeyEvent.VK_Q:
				return NativeKeyEvent.VC_Q;
			case KeyEvent.VK_R:
				return NativeKeyEvent.VC_R;
			case KeyEvent.VK_S:
				return NativeKeyEvent.VC_S;
			case KeyEvent.VK_T:
				return NativeKeyEvent.VC_T;
			case KeyEvent.VK_U:
				return NativeKeyEvent.VC_U;
			case KeyEvent.VK_V:
				return NativeKeyEvent.VC_V;
			case KeyEvent.VK_W:
				return NativeKeyEvent.VC_W;
			case KeyEvent.VK_X:
				return NativeKeyEvent.VC_X;
			case KeyEvent.VK_Y:
				return NativeKeyEvent.VC_Y;
			case KeyEvent.VK_Z:
				return NativeKeyEvent.VC_Z;
			case KeyEvent.VK_OPEN_BRACKET:
				return NativeKeyEvent.VC_OPEN_BRACKET;
			case KeyEvent.VK_CLOSE_BRACKET:
				return NativeKeyEvent.VC_CLOSE_BRACKET;
			case KeyEvent.VK_BACK_SLASH:
				return NativeKeyEvent.VC_BACK_SLASH;
			case KeyEvent.VK_SEMICOLON:
				return NativeKeyEvent.VC_SEMICOLON;
			case KeyEvent.VK_QUOTE:
				return NativeKeyEvent.VC_QUOTE;
			case KeyEvent.VK_ENTER:
				return NativeKeyEvent.VC_ENTER;
			case KeyEvent.VK_COMMA:
				return NativeKeyEvent.VC_COMMA;
			case KeyEvent.VK_PERIOD:
				return NativeKeyEvent.VC_PERIOD;
			case KeyEvent.VK_SLASH:
				return NativeKeyEvent.VC_SLASH;
			case KeyEvent.VK_SPACE:
				return NativeKeyEvent.VC_SPACE;
			case KeyEvent.VK_PRINTSCREEN:
				return NativeKeyEvent.VC_PRINTSCREEN;
			case KeyEvent.VK_SCROLL_LOCK:
				return NativeKeyEvent.VC_SCROLL_LOCK;
			case KeyEvent.VK_PAUSE:
				return NativeKeyEvent.VC_PAUSE;
			case KeyEvent.VK_INSERT:
				return NativeKeyEvent.VC_INSERT;
			case KeyEvent.VK_DELETE:
				return NativeKeyEvent.VC_DELETE;
			case KeyEvent.VK_HOME:
				return NativeKeyEvent.VC_HOME;
			case KeyEvent.VK_END:
				return NativeKeyEvent.VC_END;
			case KeyEvent.VK_PAGE_UP:
				return NativeKeyEvent.VC_PAGE_UP;
			case KeyEvent.VK_PAGE_DOWN:
				return NativeKeyEvent.VC_PAGE_DOWN;
			case KeyEvent.VK_UP:
				return NativeKeyEvent.VC_UP;
			case KeyEvent.VK_LEFT:
				return NativeKeyEvent.VC_LEFT;
			case KeyEvent.VK_CLEAR:
				return NativeKeyEvent.VC_CLEAR;
			case KeyEvent.VK_RIGHT:
				return NativeKeyEvent.VC_RIGHT;
			case KeyEvent.VK_DOWN:
				return NativeKeyEvent.VC_DOWN;
			case KeyEvent.VK_NUM_LOCK:
				return NativeKeyEvent.VC_NUM_LOCK;
			case KeyEvent.VK_SEPARATOR:
				return NativeKeyEvent.VC_SEPARATOR;
			case KeyEvent.VK_SHIFT:
				return NativeKeyEvent.VC_SHIFT;
			case KeyEvent.VK_CONTROL:
				return NativeKeyEvent.VC_CONTROL;
			case KeyEvent.VK_ALT:
				return NativeKeyEvent.VC_ALT;
			case KeyEvent.VK_META:
				return NativeKeyEvent.VC_META;
			case KeyEvent.VK_CONTEXT_MENU:
				return NativeKeyEvent.VC_CONTEXT_MENU;
			case KeyEvent.VK_KATAKANA:
				return NativeKeyEvent.VC_KATAKANA;
			case KeyEvent.VK_UNDERSCORE:
				return NativeKeyEvent.VC_UNDERSCORE;
			case KeyEvent.VK_KANJI:
				return NativeKeyEvent.VC_KANJI;
			case KeyEvent.VK_HIRAGANA:
				return NativeKeyEvent.VC_HIRAGANA;
			case KeyEvent.VK_HELP:
				return NativeKeyEvent.VC_SUN_HELP;
			case KeyEvent.VK_STOP:
				return NativeKeyEvent.VC_SUN_STOP;
			case KeyEvent.VK_PROPS:
				return NativeKeyEvent.VC_SUN_PROPS;
			case KeyEvent.VK_FIND:
				return NativeKeyEvent.VC_SUN_FIND;
			case KeyEvent.VK_AGAIN:
				return NativeKeyEvent.VC_SUN_AGAIN;
			case KeyEvent.VK_COPY:
				return NativeKeyEvent.VC_SUN_COPY;
			case KeyEvent.VK_CUT:
				return NativeKeyEvent.VC_SUN_CUT;

			default:
				return keyCode;
		}
	}

	// from https://gist.github.com/swillits/df648e87016772c7f7e5dbed2b345066
	private static final int NUMPAD_LOC = NativeKeyEvent.KEY_LOCATION_NUMPAD << 16;

	public static int convertRawMacKeyCode(int keyCode) {
		switch (keyCode) {
			case 0x37: // command
			case 0x36: // rightCommand
				return NativeKeyEvent.VC_META;
			case 0x3A: // option
			case 0x3D: // rightOption
				return NativeKeyEvent.VC_ALT;
			case 0x3B: // control
			case 0x3E: // rightControl
				return NativeKeyEvent.VC_CONTROL;
			case 0x38: // shift
			case 0x3C: // rightShift
				return NativeKeyEvent.VC_SHIFT;

			case 0x39:
				return NativeKeyEvent.VC_CAPS_LOCK;
			case 0x24:
				return NativeKeyEvent.VC_ENTER;
			case 0x30:
				return NativeKeyEvent.VC_TAB;
			case 0x31:
				return NativeKeyEvent.VC_SPACE;
			case 0x33:
				return NativeKeyEvent.VC_BACKSPACE;
			case 0x75:
				return NativeKeyEvent.VC_DELETE;
			case 0x35:
				return NativeKeyEvent.VC_ESCAPE;

			case 0x7B:
				return NativeKeyEvent.VC_LEFT;
			case 0x7C:
				return NativeKeyEvent.VC_RIGHT;
			case 0x7D:
				return NativeKeyEvent.VC_DOWN;
			case 0x7E:
				return NativeKeyEvent.VC_UP;

			case 0x72:
				return NativeKeyEvent.VC_SUN_HELP;
			case 0x73:
				return NativeKeyEvent.VC_HOME;
			case 0x74:
				return NativeKeyEvent.VC_PAGE_UP;
			case 0x77:
				return NativeKeyEvent.VC_END;
			case 0x79:
				return NativeKeyEvent.VC_PAGE_DOWN;

			case 0x7A:
				return NativeKeyEvent.VC_F1;
			case 0x78:
				return NativeKeyEvent.VC_F2;
			case 0x63:
				return NativeKeyEvent.VC_F3;
			case 0x76:
				return NativeKeyEvent.VC_F4;
			case 0x60:
				return NativeKeyEvent.VC_F5;
			case 0x61:
				return NativeKeyEvent.VC_F6;
			case 0x62:
				return NativeKeyEvent.VC_F7;
			case 0x64:
				return NativeKeyEvent.VC_F8;
			case 0x65:
				return NativeKeyEvent.VC_F9;
			case 0x6D:
				return NativeKeyEvent.VC_F10;
			case 0x67:
				return NativeKeyEvent.VC_F11;
			case 0x6F:
				return NativeKeyEvent.VC_F12;
			case 0x69:
				return NativeKeyEvent.VC_F13;
			case 0x6B:
				return NativeKeyEvent.VC_F14;
			case 0x71:
				return NativeKeyEvent.VC_F15;
			case 0x6A:
				return NativeKeyEvent.VC_F16;
			case 0x40:
				return NativeKeyEvent.VC_F17;
			case 0x4F:
				return NativeKeyEvent.VC_F18;
			case 0x50:
				return NativeKeyEvent.VC_F19;
			case 0x5A:
				return NativeKeyEvent.VC_F20;

			case 0x00:
				return NativeKeyEvent.VC_A;
			case 0x0B:
				return NativeKeyEvent.VC_B;
			case 0x08:
				return NativeKeyEvent.VC_C;
			case 0x02:
				return NativeKeyEvent.VC_D;
			case 0x0E:
				return NativeKeyEvent.VC_E;
			case 0x03:
				return NativeKeyEvent.VC_F;
			case 0x05:
				return NativeKeyEvent.VC_G;
			case 0x04:
				return NativeKeyEvent.VC_H;
			case 0x22:
				return NativeKeyEvent.VC_I;
			case 0x26:
				return NativeKeyEvent.VC_J;
			case 0x28:
				return NativeKeyEvent.VC_K;
			case 0x25:
				return NativeKeyEvent.VC_L;
			case 0x2E:
				return NativeKeyEvent.VC_M;
			case 0x2D:
				return NativeKeyEvent.VC_N;
			case 0x1F:
				return NativeKeyEvent.VC_O;
			case 0x23:
				return NativeKeyEvent.VC_P;
			case 0x0C:
				return NativeKeyEvent.VC_Q;
			case 0x0F:
				return NativeKeyEvent.VC_R;
			case 0x01:
				return NativeKeyEvent.VC_S;
			case 0x11:
				return NativeKeyEvent.VC_T;
			case 0x20:
				return NativeKeyEvent.VC_U;
			case 0x09:
				return NativeKeyEvent.VC_V;
			case 0x0D:
				return NativeKeyEvent.VC_W;
			case 0x07:
				return NativeKeyEvent.VC_X;
			case 0x10:
				return NativeKeyEvent.VC_Y;
			case 0x06:
				return NativeKeyEvent.VC_Z;

			case 0x1D:
				return NativeKeyEvent.VC_0;
			case 0x12:
				return NativeKeyEvent.VC_1;
			case 0x13:
				return NativeKeyEvent.VC_2;
			case 0x14:
				return NativeKeyEvent.VC_3;
			case 0x15:
				return NativeKeyEvent.VC_4;
			case 0x17:
				return NativeKeyEvent.VC_5;
			case 0x16:
				return NativeKeyEvent.VC_6;
			case 0x1A:
				return NativeKeyEvent.VC_7;
			case 0x1C:
				return NativeKeyEvent.VC_8;
			case 0x19:
				return NativeKeyEvent.VC_9;

			case 0x18:
				return NativeKeyEvent.VC_EQUALS;
			case 0x1B:
				return NativeKeyEvent.VC_MINUS;
			case 0x29:
				return NativeKeyEvent.VC_SEMICOLON;
			case 0x27:
				return NativeKeyEvent.VC_QUOTE;
			case 0x2B:
				return NativeKeyEvent.VC_COMMA;
			case 0x2F:
				return NativeKeyEvent.VC_PERIOD;
			case 0x2C:
				return NativeKeyEvent.VC_SLASH;
			case 0x2A:
				return NativeKeyEvent.VC_BACK_SLASH;
			case 0x32:
				return NativeKeyEvent.VC_BACKQUOTE;
			case 0x21:
				return NativeKeyEvent.VC_OPEN_BRACKET;
			case 0x1E:
				return NativeKeyEvent.VC_CLOSE_BRACKET;

			case 0x48:
				return NativeKeyEvent.VC_VOLUME_UP;
			case 0x49:
				return NativeKeyEvent.VC_VOLUME_DOWN;
			case 0x4A:
				return NativeKeyEvent.VC_VOLUME_MUTE;

			case 0x47:
				return NUMPAD_LOC | NativeKeyEvent.VC_CLEAR;
			case 0x4B:
				return NUMPAD_LOC | NativeKeyEvent.VC_SLASH;
			case 0x4C:
				return NUMPAD_LOC | NativeKeyEvent.VC_ENTER;
			case 0x4E:
				return NUMPAD_LOC | NativeKeyEvent.VC_MINUS;
			case 0x51:
				return NUMPAD_LOC | NativeKeyEvent.VC_EQUALS;
			case 0x52:
				return NUMPAD_LOC | NativeKeyEvent.VC_0;
			case 0x53:
				return NUMPAD_LOC | NativeKeyEvent.VC_1;
			case 0x54:
				return NUMPAD_LOC | NativeKeyEvent.VC_2;
			case 0x55:
				return NUMPAD_LOC | NativeKeyEvent.VC_3;
			case 0x56:
				return NUMPAD_LOC | NativeKeyEvent.VC_4;
			case 0x57:
				return NUMPAD_LOC | NativeKeyEvent.VC_5;
			case 0x58:
				return NUMPAD_LOC | NativeKeyEvent.VC_6;
			case 0x59:
				return NUMPAD_LOC | NativeKeyEvent.VC_7;
			case 0x5B:
				return NUMPAD_LOC | NativeKeyEvent.VC_8;
			case 0x5C:
				return NUMPAD_LOC | NativeKeyEvent.VC_9;

			default:
				return keyCode; // fall through for unknown or app-specific handling
		}
	}

}
