package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

import ninjabrainbot.calculator.ChunkPrediction;
import ninjabrainbot.gui.ColumnLayout;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;

/**
 * JComponent for showing a Throw.
 */
public class ChunkPanel extends ThemedPanel {

	private static final long serialVersionUID = -1522335220282509326L;
	
	private JLabel location;
	private JLabel distance;
	private JLabel nether;
	private JLabel certainty;
	private JLabel angle;
	private JLabel[] labels;
	
	GUI gui;
	double lastColor;

	public ChunkPanel(GUI gui) {
		this(gui, null);
	}

	public ChunkPanel(GUI gui, ChunkPrediction p) {
		super(gui);
		this.gui = gui;
		setOpaque(true);
		location = new JLabel((String) null, 0);
		certainty = new ThemedLabel(gui, (String) null) {
			private static final long serialVersionUID = -6995689057641195351L;
			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.CERTAINTY_COLOR_MAP.get(lastColor);
			}
		};
		certainty.setHorizontalAlignment(0);
		distance = new JLabel((String) null, 0);
		nether = new JLabel((String) null, 0);
		angle = new ThemedLabel(gui, null) {
			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.CERTAINTY_COLOR_MAP.get(lastColor);
			}
		};
		labels = new JLabel[] {location, distance, nether, certainty, angle};
		ColumnLayout layout = new ColumnLayout(0);
		layout.setRelativeWidth(distance, 0.5f);
		layout.setRelativeWidth(certainty, 0.5f);
		layout.setRelativeWidth(angle, 1.2f);
		setLayout(layout);
		add(location);
		add(distance);
		add(nether);
		add(certainty);
		add(angle);
		setPrediciton(p);
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (labels != null) {
			for (JLabel l : labels) {
				l.setFont(font);
			}
		}
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (labels != null) {
			for (JLabel l : labels) {
				if (l != null)
					l.setForeground(fg);
			}
		}
	}
	
	@Override
	public void updateColors(GUI gui) {
		setBorder(new MatteBorder(0, 0, 1, 0, gui.theme.COLOR_STRONGER));
		super.updateColors(gui);
	}
	
	public void setPrediciton(ChunkPrediction p) {
		if (p == null) {
			for (JLabel l : labels) {
				if (l != null)
					l.setText("");
			}
		} else {
			location.setText(p.formatLocation());
			certainty.setText(p.formatCertainty());
			distance.setText(p.formatDistance());
			nether.setText(p.formatNether());
			angle.setText(p.formatTravelAngle(false));
			certainty.setForeground(gui.theme.CERTAINTY_COLOR_MAP.get(p.weight));
			angle.setForeground(gui.theme.CERTAINTY_COLOR_MAP.get(p.getTravelAngleDiffColor()));
			lastColor = p.weight;
		}
	}
	
	@Override
	public void updateSize(GUI gui) {
		super.updateSize(gui);
		setPreferredSize(new Dimension(gui.size.WIDTH, gui.size.TEXT_SIZE_MEDIUM + gui.size.PADDING_THIN * 2));
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

	
}
