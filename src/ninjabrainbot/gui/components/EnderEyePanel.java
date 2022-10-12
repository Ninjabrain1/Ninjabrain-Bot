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
		divineContextPanel = new DivineContextPanel(gui, divineContext);
		add(divineContextPanel);
		throwPanels = new ThrowPanel[throwSet.maxCapacity()];
		for (int i = 0; i < throwSet.maxCapacity(); i++) {
			throwPanels[i] = new ThrowPanel(gui, throwSet, i);
			add(throwPanels[i]);
		}
		textAnimator = new TextAnimator(gui, 200);
		// Subscriptions
		sh.add(Main.preferences.showAngleErrors.whenModified().subscribe(b -> setAngleErrorsEnabled(b)));
	}
	
	private void setAngleErrorsEnabled(boolean b) {
		throwPanelHeader.setAngleErrorsEnabled(b);
		for (ThrowPanel p : throwPanels) {
			p.setAngleErrorsEnabled(b);
		}
		whenSizeModified.notifySubscribers(this);
	}

	private void setErrors(double[] errors) {
		for (int i = 0; i < throwPanels.length; i++) {
			ThrowPanel throwPanel = throwPanels[i];
			if (errors != null && i < errors.length)
				throwPanel.setError(errors[i]);
			else
				throwPanel.setError("");
		}
	}

	@Override
	public void updateSize(GUI gui) {
		divineContextPanel.setVisible(divineContextPanel.hasDivineContext());
		int k = divineContextPanel.hasDivineContext() ? 1 : 0;
		for (int i = 0; i < throwPanels.length; i++) {
			ThrowPanel throwPanel = throwPanels[i];
			throwPanel.setVisible(i < DEFAULT_SHOWN_THROWS - k || throwPanel.hasThrow());
		}
	}

	@Override
	public void updateColors(GUI gui) {
	}
	
	@Override
	public void dispose() {
		super.dispose();
		for (ThrowPanel p : throwPanels) {
			p.dispose();
		}
	}
	
}
