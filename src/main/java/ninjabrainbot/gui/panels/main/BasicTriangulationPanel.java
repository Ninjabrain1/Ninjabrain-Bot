package ninjabrainbot.gui.panels.main;

import java.awt.Dimension;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.gui.components.ColorMapLabel;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

class BasicTriangulationPanel extends ThemedPanel implements IDisposable {

	private static final long serialVersionUID = 5784318732643211103L;

	public static final String CERTAINTY_TEXT = I18n.get("certainty");

	private NinjabrainBotPreferences preferences;

	public ThemedLabel maintextLabel;
	public ColorMapLabel certaintyPanel;
	public ThemedLabel netherLabel;
	public ColorMapLabel currentAngleLabel;

	Subscription chunkPredictionSubscription;

	public BasicTriangulationPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager);
		this.preferences = preferences;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(0);
		maintextLabel = new ThemedLabel(styleManager, I18n.get("waiting_f3c"));
		certaintyPanel = new ColorMapLabel(styleManager, true);
		netherLabel = new ThemedLabel(styleManager, "");
		netherLabel.setVisible(preferences.showNetherCoords.get());
		currentAngleLabel = new ColorMapLabel(styleManager, true);
		maintextLabel.setAlignmentX(0);
		netherLabel.setAlignmentX(0);
		certaintyPanel.setAlignmentX(0);
		currentAngleLabel.setAlignmentX(0);
		add(maintextLabel);
		add(certaintyPanel);
		add(netherLabel);
		add(currentAngleLabel);
		setAngleUpdatesEnabled(preferences.showAngleUpdates.get());

		setBackgroundColor(styleManager.currentTheme.COLOR_SLIGHTLY_WEAK);
		maintextLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		netherLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		certaintyPanel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
	}

	public void setResult(ICalculatorResult result) {
		if (result != null) {
			if (result.success()) {
				ChunkPrediction prediction = result.getBestPrediction();
				setChunkPrediction(prediction);
				if (chunkPredictionSubscription != null)
					chunkPredictionSubscription.cancel();
				chunkPredictionSubscription = prediction.whenModified().subscribe(p -> setChunkPrediction(p));
			} else {
				maintextLabel.setText(I18n.get("could_not_determine"));
				certaintyPanel.setText(I18n.get("you_probably_misread"));
				certaintyPanel.setColoredText("", 0);
				netherLabel.setText("");
				currentAngleLabel.clear();
			}
		} else {
			maintextLabel.setText(I18n.get("waiting_f3c"));
			certaintyPanel.clear();
			netherLabel.setText("");
			currentAngleLabel.clear();
		}
	}

	private void setChunkPrediction(ChunkPrediction prediction) {
		maintextLabel.setText(prediction.format(preferences.strongholdDisplayType.get()));
		certaintyPanel.setText(CERTAINTY_TEXT);
		certaintyPanel.setColoredText(String.format(Locale.US, "%.1f%%", prediction.chunk.weight * 100.0), (float) prediction.chunk.weight);
		netherLabel.setText(I18n.get("nether_coordinates", prediction.chunk.x * 2, prediction.chunk.z * 2, prediction.getNetherDistance()));
		currentAngleLabel.setText(prediction.formatTravelAngle(true));
		currentAngleLabel.setColoredText(prediction.formatTravelAngleDiff(), prediction.getTravelAngleDiffColor());
	}

	public void setAngleUpdatesEnabled(boolean b) {
		currentAngleLabel.setVisible(b);
	}

	@Override
	public void updateColors() {
		super.updateColors();
		certaintyPanel.updateColor();
		currentAngleLabel.updateColor();
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		int numLabels = currentAngleLabel.isVisible() ? 4 : 3;
		setPreferredSize(new Dimension(0, numLabels * (styleManager.size.PADDING + styleManager.size.TEXT_SIZE_MEDIUM) + (numLabels - 1) * styleManager.size.PADDING_THIN));
		setBorder(new EmptyBorder(styleManager.size.PADDING_THIN, styleManager.size.PADDING, styleManager.size.PADDING_THIN, styleManager.size.PADDING));
		super.updateSize(styleManager);
	}

	@Override
	public void dispose() {
		if (chunkPredictionSubscription != null)
			chunkPredictionSubscription.cancel();
	}

}