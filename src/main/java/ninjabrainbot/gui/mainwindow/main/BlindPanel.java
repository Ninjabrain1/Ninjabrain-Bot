package ninjabrainbot.gui.mainwindow.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.gui.components.labels.ColorMapLabel;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Pair;

class BlindPanel extends ThemedPanel {

	public ColorMapLabel evalLabel;
	public ColorMapLabel certaintyPanel;
	public ThemedLabel distanceLabel;

	public BlindPanel(StyleManager styleManager) {
		super(styleManager);
		setLayout(new GridBagLayout());
		setAlignmentX(0);
		evalLabel = new ColorMapLabel(styleManager, true);
		distanceLabel = new ThemedLabel(styleManager, "");
		certaintyPanel = new ColorMapLabel(styleManager, false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1;
		add(evalLabel, gbc);
		add(certaintyPanel, gbc);
		add(distanceLabel, gbc);
		gbc.weighty = 1;
		add(Box.createGlue(), gbc);

		setBackgroundColor(styleManager.currentTheme.COLOR_SLIGHTLY_WEAK);
		evalLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		certaintyPanel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		distanceLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
	}

	public void setResult(BlindResult result) {
		if (result == null) {
			evalLabel.clear();
			certaintyPanel.clear();
			distanceLabel.setText("");
			return;
		}
		Pair<Float, String> eval = result.evaluation();
		evalLabel.setText(I18n.get("blind_coords", result.x, result.z));
		evalLabel.setColoredText(I18n.get(eval.snd), eval.fst);
		certaintyPanel.setText(I18n.get("chance_of", (int) result.highrollThreshold));
		certaintyPanel.setColoredText(String.format(Locale.US, "%.1f%% ", result.highrollProbability * 100), (float) (result.highrollProbability / result.optHighrollProb));
		distanceLabel.setText(eval == BlindResult.EXCELLENT ? "" : I18n.get("blind_direction", result.improveDirection * 180.0 / Math.PI, result.improveDistance));
	}

	@Override
	public void updateColors() {
		super.updateColors();
		certaintyPanel.updateColor();
		evalLabel.updateColor();
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setPreferredSize(new Dimension(0, 3 * (styleManager.size.PADDING + styleManager.size.TEXT_SIZE_MEDIUM) + 2 * styleManager.size.PADDING_THIN));
		setBorder(new EmptyBorder(styleManager.size.PADDING_THIN, styleManager.size.PADDING, styleManager.size.PADDING_THIN, styleManager.size.PADDING));
		super.updateSize(styleManager);
	}

}