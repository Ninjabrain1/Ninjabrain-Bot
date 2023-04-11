package ninjabrainbot.gui.mainwindow.triangulation;

import java.awt.Dimension;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.model.datastate.ICalculatorResult;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.gui.components.labels.ColorMapLabel;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.StrongholdDisplayType;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class BasicTriangulationPanel extends ThemedPanel implements IDisposable {

	public static final String CERTAINTY_TEXT = I18n.get("certainty");

	private final NinjabrainBotPreferences preferences;

	public final ThemedLabel mainTextLabel;
	public final ColorMapLabel certaintyPanel;
	public final ThemedLabel netherLabel;
	public final ColorMapLabel currentAngleLabel;

	Subscription chunkPredictionSubscription;

	public BasicTriangulationPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager);
		this.preferences = preferences;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(0);
		mainTextLabel = new ThemedLabel(styleManager, I18n.get("waiting_f3c"));
		certaintyPanel = new ColorMapLabel(styleManager, true);
		netherLabel = new ThemedLabel(styleManager, "");
		netherLabel.setVisible(preferences.showNetherCoords.get());
		currentAngleLabel = new ColorMapLabel(styleManager, true);
		mainTextLabel.setAlignmentX(0);
		netherLabel.setAlignmentX(0);
		certaintyPanel.setAlignmentX(0);
		currentAngleLabel.setAlignmentX(0);
		add(mainTextLabel);
		add(certaintyPanel);
		add(netherLabel);
		add(currentAngleLabel);
		setAngleUpdatesEnabled(preferences.showAngleUpdates.get());

		setBackgroundColor(styleManager.currentTheme.COLOR_SLIGHTLY_WEAK);
		mainTextLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		netherLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		certaintyPanel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
	}

	public void setResult(ICalculatorResult result) {
		if (result != null) {
			if (result.success()) {
				ChunkPrediction prediction = result.getBestPrediction();
				setChunkPrediction(prediction);
				if (chunkPredictionSubscription != null)
					chunkPredictionSubscription.dispose();
				chunkPredictionSubscription = prediction.whenRelativePlayerPositionChanged().subscribeEDT(__ -> setChunkPrediction(prediction));
			} else {
				mainTextLabel.setText(I18n.get("could_not_determine"));
				certaintyPanel.setText(I18n.get("you_probably_misread"));
				certaintyPanel.setColoredText("", 0);
				netherLabel.setText("");
				currentAngleLabel.clear();
			}
		} else {
			mainTextLabel.setText(I18n.get("waiting_f3c"));
			certaintyPanel.clear();
			netherLabel.setText("");
			currentAngleLabel.clear();
		}
	}

	private void setChunkPrediction(ChunkPrediction prediction) {
		mainTextLabel.setText(formatStrongholdCoords(prediction, preferences.strongholdDisplayType.get()));
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
			chunkPredictionSubscription.dispose();
	}

	private static String formatStrongholdCoords(ChunkPrediction chunkPrediction, StrongholdDisplayType strongholdDisplayType) {
		Chunk chunk = chunkPrediction.chunk;
		int distance = chunkPrediction.getOverworldDistance();
		switch (strongholdDisplayType) {
			case FOURFOUR:
				return I18n.get("location_blocks", chunk.fourFourX(), chunk.fourFourZ(), distance);
			case EIGHTEIGHT:
				return I18n.get("location_blocks", chunk.eightRightX(), chunk.eightEightZ(), distance);
			case CHUNK:
				return I18n.get("chunk_blocks", chunk.x, chunk.z, distance);
			default:
				break;
		}
		return I18n.get("chunk_blocks", chunk.x, chunk.z, distance);
	}

}