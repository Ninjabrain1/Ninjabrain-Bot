package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.TriangulationResult;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.Theme;

public class MainTextArea extends ThemedPanel {

	private static final long serialVersionUID = 5680882946230890993L;
	
	public JLabel maintextLabel;
	public JLabel certaintytextLabel;
	public JLabel certaintyLabel;
	public JLabel netherLabel;

	public static final String CERTAINTY_TEXT = "Certainty: ";
	private double lastCertainty = 0.0;

	public MainTextArea(GUI gui) {
		super(gui);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(0);
		maintextLabel = new ThemedLabel(gui, "Waiting for F3+C...");
		certaintytextLabel = new ThemedLabel(gui, "");
		certaintyLabel = new ThemedLabel(gui, "") {
			private static final long serialVersionUID = -6995689057641195351L;
			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.CERTAINTY_COLOR_MAP.get(lastCertainty);
			}
		};
		JPanel certaintyPanel = new JPanel();
		certaintyPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		certaintyPanel.setOpaque(false);
		certaintyPanel.add(certaintytextLabel);
		certaintyPanel.add(certaintyLabel);
		netherLabel = new ThemedLabel(gui, "");
		netherLabel.setVisible(Main.preferences.showNetherCoords.get());
		maintextLabel.setAlignmentX(0);
		netherLabel.setAlignmentX(0);
		certaintyPanel.setAlignmentX(0);
		add(maintextLabel);
		add(certaintyPanel);
		add(netherLabel);
	}
	
	public void setResult(TriangulationResult result) {
		if (result != null) {
			if (result.success) {
				lastCertainty = result.weight;
				maintextLabel.setText(result.format());
				certaintytextLabel.setText(CERTAINTY_TEXT);
				certaintyLabel.setText(String.format(Locale.US, "%.1f%%", result.weight*100.0));
				netherLabel.setText(String.format(Locale.US, "Nether coordinates: (%d, %d)", result.x*2, result.z*2));
			} else {
				maintextLabel.setText("Could not determine the stronghold chunk.");
				certaintytextLabel.setText("You probably misread one of the eyes.");
				certaintyLabel.setText("");
				netherLabel.setText("");
			}
		} else {
			maintextLabel.setText("Waiting for F3+C...");
			certaintytextLabel.setText("");
			certaintyLabel.setText("");
			netherLabel.setText("");
		}
	}
	
	@Override
	public void updateColors(GUI gui) {
		super.updateColors(gui);
		certaintyLabel.setForeground(gui.theme.CERTAINTY_COLOR_MAP.get(lastCertainty));
	}
	
	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_NEUTRAL;
	}
	
	@Override
	public void updateSize(GUI gui) {
		setPreferredSize(new Dimension(0, 3 * (gui.size.PADDING + gui.size.TEXT_SIZE_MEDIUM) + 2 * gui.size.PADDING_THIN));
		setBorder(new EmptyBorder(gui.size.PADDING_THIN, gui.size.PADDING, gui.size.PADDING_THIN, gui.size.PADDING));
		super.updateSize(gui);
	}

}
