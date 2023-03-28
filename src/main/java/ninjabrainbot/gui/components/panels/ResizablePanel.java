package ninjabrainbot.gui.components.panels;

import javax.swing.JPanel;

import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IModifiable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.event.SubscriptionHandler;

public class ResizablePanel extends JPanel implements IModifiable<ResizablePanel>, IDisposable {

	protected SubscriptionHandler sh = new SubscriptionHandler();
	protected ObservableProperty<ResizablePanel> whenSizeModified = new ObservableProperty<ResizablePanel>();

	@Override
	public void dispose() {
		sh.dispose();
	}

	@Override
	public ISubscribable<ResizablePanel> whenModified() {
		return whenSizeModified;
	}

}
