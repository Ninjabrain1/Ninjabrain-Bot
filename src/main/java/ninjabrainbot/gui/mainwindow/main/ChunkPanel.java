package ninjabrainbot.gui.mainwindow.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Locale;

import javax.swing.border.MatteBorder;

import ninjabrainbot.data.stronghold.Chunk;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.gui.components.labels.ColorMapLabel;
import ninjabrainbot.gui.components.labels.ColoredLabel;
import ninjabrainbot.gui.components.labels.ILabel;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.ColumnLayout;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.MultipleChoicePreference;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.StrongholdDisplayType;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

/**
 * JComponent for showing a Throw.
 */
public class ChunkPanel extends ThemedPanel implements IDisposable {

	private MultipleChoicePreference<StrongholdDisplayType> strongholdDisplayType;

	private ChunkPrediction currentPrediction;

	private ThemedLabel location;
	private ColoredLabel certainty;
	private ThemedLabel distance;
	private ThemedLabel nether;
	private ColorMapLabel angle;
	private ILabel[] labels;

	StyleManager styleManager;
	double lastColor;

	private Subscription chunkPredictionSubscription;
	private Subscription strongholdDisplayTypeChangedSubscription;

	private WrappedColor borderCol;

	public ChunkPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		this(styleManager, preferences, null);
	}

	public ChunkPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, ChunkPrediction p) {
		super(styleManager);
		this.styleManager = styleManager;
		strongholdDisplayType = preferences.strongholdDisplayType;
		setOpaque(true);
		location = new ThemedLabel(styleManager, true);
		certainty = new ColoredLabel(styleManager, true);
		distance = new ThemedLabel(styleManager, true);
		nether = new ThemedLabel(styleManager, true);
		angle = new ColorMapLabel(styleManager, true, true);
		labels = new ILabel[] { location, certainty, distance, nether, angle };
		ColumnLayout layout = new ColumnLayout(0);
		layout.setRelativeWidth(location, 2f);
		layout.setRelativeWidth(nether, 1.8f);
		layout.setRelativeWidth(angle, 2.5f);
		setLayout(layout);
		add(location);
		add(certainty);
		add(distance);
		add(nether);
		add(angle);
		setPrediction(p);
		setAngleUpdatesEnabled(preferences.showAngleUpdates.get());
		strongholdDisplayTypeChangedSubscription = preferences.strongholdDisplayType.whenModified().subscribe(__ -> setPrediction(currentPrediction));

		borderCol = styleManager.currentTheme.COLOR_DIVIDER;
		setBackgroundColor(styleManager.currentTheme.COLOR_SLIGHTLY_WEAK);
		setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);

		location.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		certainty.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		distance.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		nether.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (labels != null) {
			for (ILabel l : labels) {
				l.setFont(font);
			}
		}
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (labels != null) {
			for (ILabel l : labels) {
				if (l != null)
					l.setForeground(fg);
			}
		}
	}

	public void setAngleUpdatesEnabled(boolean b) {
		angle.setVisible(b);
	}

	@Override
	public void updateColors() {
		setBorder(new MatteBorder(0, 0, 1, 0, borderCol.color()));
		super.updateColors();
		angle.updateColor();
		certainty.updateColors();
	}

	public void setPrediction(ChunkPrediction chunkPrediction) {
		currentPrediction = chunkPrediction;
		if (chunkPredictionSubscription != null)
			chunkPredictionSubscription.cancel();
		if (chunkPrediction == null) {
			for (ILabel l : labels) {
				if (l != null) {
					l.setText("");
					if (l instanceof ColorMapLabel) {
						((ColorMapLabel) l).setColoredText("", 0);
					}
				}
			}
		} else {
			setText(chunkPrediction);
			chunkPrediction.whenRelativePlayerPositionChanged().subscribe(__ -> setText(chunkPrediction));
		}
	}

	private void setText(ChunkPrediction chunkPrediction) {
		location.setText(formatStrongholdCoords(chunkPrediction.chunk, strongholdDisplayType.get()));
		certainty.setText(chunkPrediction.formatCertainty(), (float) chunkPrediction.chunk.weight);
		distance.setText(chunkPrediction.formatDistanceInPlayersDimension());
		nether.setText(chunkPrediction.formatNether());
		angle.setText(chunkPrediction.formatTravelAngle(false));
		angle.setColoredText(chunkPrediction.formatTravelAngleDiff(), chunkPrediction.getTravelAngleDiffColor());
		lastColor = chunkPrediction.chunk.weight;
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		super.updateSize(styleManager);
		setPreferredSize(new Dimension(styleManager.size.WIDTH, styleManager.size.TEXT_SIZE_MEDIUM + styleManager.size.PADDING_THIN * 2));
	}

	@Override
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_MEDIUM;
	}

	@Override
	public void dispose() {
		if (chunkPredictionSubscription != null)
			chunkPredictionSubscription.cancel();
		strongholdDisplayTypeChangedSubscription.cancel();
	}

	private static String formatStrongholdCoords(Chunk chunk, StrongholdDisplayType strongholdDisplayType) {
		switch (strongholdDisplayType) {
			case FOURFOUR:
				return String.format(Locale.US, "(%d, %d)", chunk.fourfourX(), chunk.fourfourZ());
			case EIGHTEIGHT:
				return String.format(Locale.US, "(%d, %d)", chunk.eighteightX(), chunk.eighteightZ());
			case CHUNK:
				return String.format(Locale.US, "(%d, %d)", chunk.x, chunk.z);
			default:
				break;
		}
		return String.format(Locale.US, "(%d, %d)", chunk.x, chunk.z);
	}

}
