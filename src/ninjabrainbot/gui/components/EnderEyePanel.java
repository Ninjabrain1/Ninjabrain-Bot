package ninjabrainbot.gui.components;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ninjabrainbot.calculator.DivineContext;
import ninjabrainbot.calculator.Throw;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.TextAnimator;

public class EnderEyePanel extends JPanel implements ThemedComponent {

	private static final long serialVersionUID = 5595933968395207468L;

	public static final int DEFAULT_SHOWN_THROWS = 3;
	
	private ThrowPanelHeader throwPanelHeader;
	private ThrowPanel[] throwPanels;
	private DivineContextPanel divineContextPanel;
	
	private TextAnimator textAnimator;

	public EnderEyePanel(GUI gui) {
		gui.registerThemedComponent(this);
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		throwPanelHeader = new ThrowPanelHeader(gui);
		add(throwPanelHeader);
		divineContextPanel = new DivineContextPanel(gui);
		add(divineContextPanel);
		throwPanels = new ThrowPanel[GUI.MAX_THROWS];
		for (int i = 0; i < GUI.MAX_THROWS; i++) {
			throwPanels[i] = new ThrowPanel(gui);
			add(throwPanels[i]);
		}
		textAnimator = new TextAnimator(gui, 200);
	}
	
	public void setAngleErrorsEnabled(boolean b) {
		throwPanelHeader.setAngleErrorsEnabled(b);
		for (ThrowPanel p : throwPanels) {
			p.setAngleErrorsEnabled(b);
		}
	}

	public void setThrow(int i, Throw t) {
		throwPanels[i].setThrow(t);
		textAnimator.setJThrowPanel(throwPanels[i]);
	}

	public void setErrors(double[] errors) {
		for (int i = 0; i < throwPanels.length; i++) {
			ThrowPanel throwPanel = throwPanels[i];
			if (errors != null && i < errors.length)
				throwPanel.setError(errors[i]);
			else
				throwPanel.setError("");
		}
	}

	public void setThrows(List<Throw> eyeThrows, DivineContext dc) {
		divineContextPanel.setDivineContext(dc);
		for (int i = 0; i < throwPanels.length; i++) {
			ThrowPanel throwPanel = throwPanels[i];
			throwPanel.setThrow(i < eyeThrows.size() ? eyeThrows.get(i) : null);
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
	
}
