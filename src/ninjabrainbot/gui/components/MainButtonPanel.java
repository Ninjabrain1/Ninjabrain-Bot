package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.Theme;

public class MainButtonPanel extends ThemedPanel {
	
	private static final long serialVersionUID = -8143875137607726122L;
	
	private JLabel throwsLabel;
	private FlatButton resetButton;
	private FlatButton undoButton;
	
	public MainButtonPanel(GUI gui) {
		super(gui);
		setOpaque(true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(0);
		throwsLabel = new ThemedLabel(gui, "Ender eye throws:", true) {
			private static final long serialVersionUID = -9014547502923743608L;
			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.TEXT_COLOR_NEUTRAL;
			}
		};
		resetButton = getResetButton(gui);
		undoButton = getUndoButton(gui);
		add(throwsLabel);
		add(Box.createHorizontalGlue());
		add(undoButton);
		add(resetButton);
	}
	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONG;
	}
	@Override
	public void updateSize(GUI gui) {
		setPreferredSize(new Dimension(0, 24));
		setBorder(getBorder(gui));
		super.updateSize(gui);
	}
	
	@Override
	public void updateColors(GUI gui) {
		super.updateColors(gui);
		setBorder(getBorder(gui));
	}
	
	private Border getBorder(GUI gui) {
		Border b1 = new MatteBorder(1, 0, 0, 0, gui.theme.COLOR_STRONGEST);
		Border b2 = new EmptyBorder(0, gui.size.PADDING, 0, 0);
		return BorderFactory.createCompoundBorder(b1, b2);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}
	
	private FlatButton getResetButton(GUI gui) {
		FlatButton button = new FlatButton(gui, "Reset");
		button.addActionListener(p -> gui.resetThrows());
		return button;
	}
	
	private FlatButton getUndoButton(GUI gui) {
		FlatButton button = new FlatButton(gui, "Undo");
		button.addActionListener(p -> gui.undo());
		return button;
	}
	
}
