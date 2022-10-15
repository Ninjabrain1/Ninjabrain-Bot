package ninjabrainbot.gui.panels.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.border.MatteBorder;

import ninjabrainbot.Main;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.gui.components.ColorMapLabel;
import ninjabrainbot.gui.components.ILabel;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.ColumnLayout;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.Subscription;

/**
 * JComponent for showing a Throw.
 */
public class ChunkPanel extends ThemedPanel implements IDisposable {

	private static final long serialVersionUID = -1522335220282509326L;

	private ThemedLabel location;
	private ThemedLabel certainty;
	private ThemedLabel distance;
	private ThemedLabel nether;
	private ColorMapLabel angle;
	private ILabel[] labels;

	StyleManager styleManager;
	double lastColor;

	Subscription chunkPredictionSubscription;

	public ChunkPanel(StyleManager styleManager) {
		this(styleManager, null);
	}

	public ChunkPanel(StyleManager styleManager, ChunkPrediction p) {
		super(styleManager);
		this.styleManager = styleManager;
		setOpaque(true);
		location = new ThemedLabel(styleManager, true);
		certainty = new ThemedLabel(styleManager, true) {
			private static final long serialVersionUID = -6995689057641195351L;

			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.CERTAINTY_COLOR_MAP.get(lastColor);
			}
		};
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
		setPrediciton(p);
		setAngleUpdatesEnabled(Main.preferences.showAngleUpdates.get());
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
	public void updateColors(StyleManager styleManager) {
		setBorder(new MatteBorder(0, 0, 1, 0, styleManager.theme.COLOR_STRONGER));
		super.updateColors(styleManager);
		angle.updateColor(styleManager);
		certainty.updateColors(styleManager);
	}

	public void setPrediciton(ChunkPrediction p) {
		if (chunkPredictionSubscription != null)
			chunkPredictionSubscription.cancel();
		if (p == null) {
			for (ILabel l : labels) {
				if (l != null) {
					l.setText("");
					if (l instanceof ColorMapLabel) {
						((ColorMapLabel) l).setColoredText("", 0);
					}
				}
			}
		} else {
			setText(p);
			p.whenModified().subscribe(pred -> setText(pred));
		}
	}
	
	private void setText(ChunkPrediction p) {
		location.setText(p.formatLocation());
		certainty.setText(p.formatCertainty());
		certainty.setForeground(styleManager.theme.CERTAINTY_COLOR_MAP.get(p.chunk.weight));
		distance.setText(p.formatDistance());
		nether.setText(p.formatNether());
		angle.setText(p.formatTravelAngle(false));
		angle.setColoredText(p.formatTravelAngleDiff(), p.getTravelAngleDiffColor());
		lastColor = p.chunk.weight;
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
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_SLIGHTLY_WEAK;
	}

	@Override
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_SLIGHTLY_STRONG;
	}
	
	@Override
	public void dispose() {
		if (chunkPredictionSubscription != null)
			chunkPredictionSubscription.cancel();
	}

}
