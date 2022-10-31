package ninjabrainbot.gui.panels.main;

import javax.swing.BoxLayout;

import ninjabrainbot.Main;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.gui.components.TextAnimator;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.panels.ResizablePanel;
import ninjabrainbot.gui.style.StyleManager;

public class EnderEyePanel extends ResizablePanel implements ThemedComponent {

	private static final long serialVersionUID = 5595933968395207468L;

	public static final int DEFAULT_SHOWN_THROWS = 3;

	private ThrowPanelHeader throwPanelHeader;
	private ThrowPanel[] throwPanels;
	private DivineContextPanel divineContextPanel;

	private TextAnimator textAnimator;

	public EnderEyePanel(StyleManager styleManager, IDataStateHandler dataStateHandler, IDivineContext divineContext) {
		styleManager.registerThemedComponent(this);
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		throwPanelHeader = new ThrowPanelHeader(styleManager);
		add(throwPanelHeader);
		throwPanels = new ThrowPanel[dataStateHandler.getDataState().getThrowSet().maxCapacity()];
		divineContextPanel = new DivineContextPanel(styleManager, divineContext, () -> whenDivineContextVisibilityUpdated());
		add(divineContextPanel);
		for (int i = 0; i < dataStateHandler.getDataState().getThrowSet().maxCapacity(); i++) {
			throwPanels[i] = new ThrowPanel(styleManager, dataStateHandler, i, () -> whenSizeModified.notifySubscribers(this));
			add(throwPanels[i]);
		}
		throwPanels[2].setDivineContextPanel(divineContextPanel);
		textAnimator = new TextAnimator(styleManager, 200);
		// Subscriptions
		sh.add(Main.preferences.showAngleErrors.whenModified().subscribe(b -> setAngleErrorsEnabled(b)));
	}

	private void whenDivineContextVisibilityUpdated() {
		throwPanels[2].updateVisibility();
		whenSizeModified.notifySubscribers(this);
	}

	private void setAngleErrorsEnabled(boolean b) {
		throwPanelHeader.setAngleErrorsEnabled(b);
		for (ThrowPanel p : throwPanels) {
			p.setAngleErrorsEnabled(b);
		}
	}

	@Override
	public void updateColors() {
	}

	@Override
	public void updateSize(StyleManager styleManager) {
	}

	@Override
	public void dispose() {
		super.dispose();
		for (ThrowPanel p : throwPanels) {
			p.dispose();
		}
		divineContextPanel.dispose();
	}
}