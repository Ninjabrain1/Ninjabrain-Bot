package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

import ninjabrainbot.Main;
import ninjabrainbot.gui.ColumnLayout;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

/**
 * JComponent for showing a Throw.
 */
public class ChunkPanelHeader extends ThemedPanel {

	private static final long serialVersionUID = -5271531617019225933L;
	
	private JLabel location;
	private JLabel certainty;
	private JLabel distance;
	private JLabel nether;
	private JLabel angle;
	private JLabel[] labels;
	
	GUI gui;

	public ChunkPanelHeader(GUI gui) {
		super(gui, true);
		this.gui = gui;
		setOpaque(true);
		location = new JLabel("", 0);
		certainty = new JLabel(I18n.get("certainty_2"), 0);
		distance = new JLabel(I18n.get("dist"), 0);
		nether = new JLabel(I18n.get("nether"), 0);
		angle = new JLabel(I18n.get("angle"), 0);
		labels = new JLabel[] {location, certainty, distance, nether, angle};
		ColumnLayout layout = new ColumnLayout(0);
		layout.setRelativeWidth(location, 2f);
		layout.setRelativeWidth(nether, 1.8f);
		layout.setRelativeWidth(angle, 2.5f);
		setLayout(layout);
		add(location);
		add(certainty);
		add(distance);
		add(nether);
		updateHeaderText();
		setAngleUpdatesEnabled(Main.preferences.showAngleUpdates.get());
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
	
	public void updateHeaderText() {
		location.setText(Main.preferences.strongholdDisplayType.get() == NinjabrainBotPreferences.CHUNK ? I18n.get("chunk") : I18n.get("location"));
	}

	public void setAngleUpdatesEnabled(boolean b) {
		if (b) {
			add(angle);
		} else {
			remove(angle);
		}
	}
	
	@Override
	public void updateColors(GUI gui) {
		setBorder(new MatteBorder(0, 0, 2, 0, gui.theme.COLOR_STRONGEST));
		super.updateColors(gui);
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
		return theme.COLOR_STRONG;
	}
	
	@Override
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_STRONG;
	}
	
}
