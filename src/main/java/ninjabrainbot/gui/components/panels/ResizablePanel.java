package ninjabrainbot.gui.components.panels;

import javax.swing.JPanel;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IModifiable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class ResizablePanel extends JPanel implements IModifiable<ResizablePanel>, IDisposable {

	protected DisposeHandler disposeHandler = new DisposeHandler();
	protected ObservableProperty<ResizablePanel> whenSizeModified = new ObservableProperty<ResizablePanel>();

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

	@Override
	public ISubscribable<ResizablePanel> whenModified() {
		return whenSizeModified;
	}

}
