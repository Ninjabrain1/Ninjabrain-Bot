package ninjabrainbot.gui.panels;

import javax.swing.JPanel;

import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.IModifiable;
import ninjabrainbot.util.ISubscribable;
import ninjabrainbot.util.ObservableProperty;
import ninjabrainbot.util.SubscriptionHandler;

public class ResizablePanel extends JPanel implements IModifiable<ResizablePanel>, IDisposable {

	private static final long serialVersionUID = 6569520433615429424L;

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
