package ninjabrainbot.io;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

import ninjabrainbot.gui.GUI;

public class ClipboardReader implements Runnable {
	
	GUI gui;
	Clipboard clipboard;
	String lastClipboardString;
	
	public ClipboardReader(GUI gui) {
		this.gui = gui;
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		lastClipboardString = "";
	}

	@Override
	public void run() {
		while (true) {
			try {
				String clipboardString = (String) clipboard.getData(DataFlavor.stringFlavor);
				if (!lastClipboardString.equals(clipboardString)) {
					gui.onClipboardUpdated(clipboardString);
					// Set clipboard (allows the program to detect an identical F3+C later)
//					StringSelection selection = new StringSelection(clipboardString + " ");
//					clipboard.setContents(selection, selection);
					lastClipboardString = clipboardString;
				}
			} catch (Exception e) {
			}
			try {
				Thread.sleep(100); // Sleep 0.1 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
