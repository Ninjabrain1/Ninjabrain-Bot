package ninjabrainbot.gui.components;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class RefreshWindowOnMonitorChangeListener implements ComponentListener {

	private final Window frame;
	private GraphicsDevice[] allDisplays;
	private int currentDisplayIndex;
	boolean disabled = false;

	public RefreshWindowOnMonitorChangeListener(Window window) {
		this.frame = window;
		try {
			allDisplays = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
			this.currentDisplayIndex = getDisplayIndex();
		} catch (Exception e) {
			System.out.println("Encountered exception in RefreshWindowOnMonitorChangeListener, disabling the listener.");
			e.printStackTrace();
			window.removeComponentListener(this);
			this.currentDisplayIndex = -1;
			disabled = true;
		}
	}

	@Override
	public void componentMoved(ComponentEvent evt) {
		if (disabled)
			return;
		int displayIndex = getDisplayIndex();
		if (displayIndex != currentDisplayIndex) {
			frame.repaint();
			currentDisplayIndex = displayIndex;
		}
	}

	private int getDisplayIndex() {
		GraphicsDevice curDisplay = frame.getGraphicsConfiguration().getDevice();
		for (int i = 0; i < allDisplays.length; i++) {
			if (allDisplays[i].equals(curDisplay)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void componentShown(ComponentEvent evt) {
	}

	@Override
	public void componentResized(ComponentEvent evt) {
	}

	@Override
	public void componentHidden(ComponentEvent evt) {
	}
}