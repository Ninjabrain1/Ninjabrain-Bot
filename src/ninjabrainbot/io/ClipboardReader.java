package ninjabrainbot.io;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.concurrent.atomic.AtomicBoolean;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;

public class ClipboardReader implements Runnable {
	
	GUI gui;
	Clipboard clipboard;
	String lastClipboardString;
	
	private AtomicBoolean forceReadLater;
	
	public ClipboardReader(GUI gui) {
		this.gui = gui;
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		lastClipboardString = "";
		forceReadLater = new AtomicBoolean(false);
	}
	
	public void forceRead() {
		forceReadLater.set(true);
		System.out.println("asd");
	}
	
	@Override
	public void run() {
		while (true) {
			boolean read = !Main.preferences.altClipboardReader.get();
			if (Main.preferences.altClipboardReader.get() && forceReadLater.get()) {
				read = true;
				// Sleep 0.1 seconds to let the game update the clipboard
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (read) {
				try {
					String clipboardString = (String) clipboard.getData(DataFlavor.stringFlavor);
					if (!lastClipboardString.equals(clipboardString)) {
						gui.onClipboardUpdated(clipboardString);
						lastClipboardString = clipboardString;
					}
				} catch (Exception e) {
				}
			}
			// Sleep 0.1 seconds
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
