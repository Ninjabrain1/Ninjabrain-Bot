package ninjabrainbot.gui.components;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.BlindResult;
import ninjabrainbot.calculator.TriangulationResult;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.Theme;

public class MainTextArea extends JPanel {

	private static final long serialVersionUID = 5680882946230890993L;
	
	BasicTriangulationPanel basicTriangulation;
	BlindPanel blind;
	
	private final String BLIND = "BLIND", TRIANGULATION = "TRI"; 
	
	CardLayout layout;
	
	public MainTextArea(GUI gui) {
		layout = new CardLayout();
		setLayout(layout);
		setAlignmentX(0);
		basicTriangulation = new BasicTriangulationPanel(gui);
		blind = new BlindPanel(gui);
		add(basicTriangulation, TRIANGULATION);
		add(blind, BLIND);
		setOpaque(false);
	}
	
	public void setResult(TriangulationResult result, GUI gui) {
		basicTriangulation.setResult(result);
		basicTriangulation.updateColors(gui);
		layout.show(this, TRIANGULATION);
	}
	
	public void setResult(BlindResult result, GUI gui) {
		blind.setResult(result);
		blind.updateColors(gui);
		layout.show(this, BLIND);
	}

	public void setNetherCoordsEnabled(boolean b) {
		basicTriangulation.netherLabel.setVisible(b);
	}

	public void updateColors(GUI gui) {
		basicTriangulation.updateColors(gui);
		blind.updateColors(gui);
	}
	
	public void onReset() {
		layout.show(this, TRIANGULATION);
	}

}
class BasicTriangulationPanel extends ThemedPanel {

	private static final long serialVersionUID = 5784318732643211103L;

	public JLabel maintextLabel;
	public ColorMapLabel certaintyPanel;
	public JLabel netherLabel;

	public static final String CERTAINTY_TEXT = "Certainty: ";
	
	public BasicTriangulationPanel(GUI gui) {
		super(gui);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(0);
		maintextLabel = new ThemedLabel(gui, "Waiting for F3+C...");
		certaintyPanel = new ColorMapLabel(gui, true);
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
				maintextLabel.setText(result.format());
				certaintyPanel.setText(CERTAINTY_TEXT);
				certaintyPanel.setColoredText(String.format(Locale.US, "%.1f%%", result.weight*100.0), (float) result.weight);
				netherLabel.setText(String.format(Locale.US, "Nether coordinates: (%d, %d)", result.x*2, result.z*2));
			} else {
				maintextLabel.setText("Could not determine the stronghold chunk.");
				certaintyPanel.setText("You probably misread one of the eyes.");
				certaintyPanel.setColoredText("", 0);
				netherLabel.setText("");
			}
		} else {
			maintextLabel.setText("Waiting for F3+C...");
			certaintyPanel.setText("");
			certaintyPanel.setColoredText("", 0);
			netherLabel.setText("");
		}
	}
	
	@Override
	public void updateColors(GUI gui) {
		super.updateColors(gui);
		certaintyPanel.updateColor(gui);
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
class BlindPanel extends ThemedPanel {

	private static final long serialVersionUID = 5784318732643211103L;

	public ColorMapLabel evalLabel;
	public ColorMapLabel certaintyPanel;
	public JLabel distanceLabel;
	
	public BlindPanel(GUI gui) {
		super(gui);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(0);
		evalLabel = new ColorMapLabel(gui, true);
		distanceLabel = new ThemedLabel(gui, "");
		certaintyPanel = new ColorMapLabel(gui, false);
		add(evalLabel);
		add(certaintyPanel);
		add(distanceLabel);
	}
	
	public void setResult(BlindResult result) {
		evalLabel.setText(String.format(Locale.US, "Blind coords (%.0f, %.0f) are ", result.x, result.z));
		evalLabel.setColoredText(String.format(Locale.US, "%s", result.evaluation().snd), result.evaluation().fst);
		certaintyPanel.setText(String.format(Locale.US, " chance of <%d block blind", (int) result.highrollThreshold));
		certaintyPanel.setColoredText(String.format(Locale.US, "%.1f%%", result.highrollProbability*100), (float) result.highrollProbability / 0.1f);
		distanceLabel.setText(String.format(Locale.US, "Average distance to stronghold: %.0f blocks", result.avgDistance));
	}
	
	@Override
	public void updateColors(GUI gui) {
		super.updateColors(gui);
		certaintyPanel.updateColor(gui);
		evalLabel.updateColor(gui);
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
class ColorMapLabel extends JPanel {
	
	private static final long serialVersionUID = 8926205242557099213L;
	
	public JLabel textLabel;
	public JLabel coloredLabel;

	private double lastColor = 0.0;

	public ColorMapLabel(GUI gui, boolean textFirst) {
		textLabel = new ThemedLabel(gui, "");
		coloredLabel = new ThemedLabel(gui, "") {
			private static final long serialVersionUID = -6995689057641195351L;
			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.CERTAINTY_COLOR_MAP.get(lastColor);
			}
		};
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setOpaque(false);
		if (textFirst) {
			add(textLabel);
			add(coloredLabel);
		} else {
			add(coloredLabel);
			add(textLabel);
		}
		setAlignmentX(0);
	}
	
	void setColoredText(String text, float color) {
		lastColor = color;
		coloredLabel.setText(text);
	}
	
	void setText(String text) {
		textLabel.setText(text);
	}
	
	void updateColor(GUI gui) {
		coloredLabel.setForeground(gui.theme.CERTAINTY_COLOR_MAP.get(lastColor));
	}
	
}