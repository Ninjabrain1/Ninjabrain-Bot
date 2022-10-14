package ninjabrainbot.gui.components;

import javax.swing.BoxLayout;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.IThrowSet;
import ninjabrainbot.calculator.divine.IDivineContext;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.TextAnimator;

public class EnderEyePanel extends ResizablePanel implements ThemedComponent {

	private static final long serialVersionUID = 5595933968395207468L;

	public static final int DEFAULT_SHOWN_THROWS = 3;

	private ThrowPanelHeader throwPanelHeader;
	private ThrowPanel[] throwPanels;
	private DivineContextPanel divineContextPanel;

	private TextAnimator textAnimator;

	public EnderEyePanel(GUI gui, IThrowSet throwSet, IDivineContext divineContext) {
		gui.registerThemedComponent(this);
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		throwPanelHeader = new ThrowPanelHeader(gui);
		add(throwPanelHeader);
		throwPanels = new ThrowPanel[throwSet.maxCapacity()];
		divineContextPanel = new DivineContextPanel(gui, divineContext, () -> whenDivineContextVisibilityUpdated());
		add(divineContextPanel);
		for (int i = 0; i < throwSet.maxCapacity(); i++) {
			throwPanels[i] = new ThrowPanel(gui, throwSet, i, () -> whenSizeModified.notifySubscribers(this));
			add(throwPanels[i]);
		}
		throwPanels[2].setDivineContextPanel(divineContextPanel);
		textAnimator = new TextAnimator(gui, 200);
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
	public void updateColors(GUI gui) {
	}

	@Override
	public void updateSize(GUI gui) {
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
