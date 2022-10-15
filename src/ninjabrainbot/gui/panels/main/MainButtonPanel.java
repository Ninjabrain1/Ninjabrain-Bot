package ninjabrainbot.gui.panels.main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.util.I18n;

public class MainButtonPanel extends ThemedPanel {
	
	private static final long serialVersionUID = -8143875137607726122L;
	
	IDataState dataState;
	IDataStateHandler dataStateHandler;
	
	private JLabel throwsLabel;
	private FlatButton resetButton;
	private FlatButton undoButton;
	
	public MainButtonPanel(StyleManager styleManager, IDataState dataState, IDataStateHandler dataStateHandler) {
		super(styleManager);
		this.dataState = dataState;
		this.dataStateHandler = dataStateHandler;
		setOpaque(true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(0);
		throwsLabel = new ThemedLabel(styleManager, I18n.get("ender_eye_throws"), true) {
			private static final long serialVersionUID = -9014547502923743608L;
			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.TEXT_COLOR_NEUTRAL;
			}
		};
		resetButton = getResetButton(styleManager);
		undoButton = getUndoButton(styleManager);
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
	public void updateSize(StyleManager styleManager) {
		setPreferredSize(new Dimension(0, 24));
		setBorder(getBorder(styleManager));
		super.updateSize(styleManager);
	}
	
	@Override
	public void updateColors(StyleManager styleManager) {
		super.updateColors(styleManager);
		setBorder(getBorder(styleManager));
	}
	
	private Border getBorder(StyleManager styleManager) {
		Border b1 = new MatteBorder(1, 0, 0, 0, styleManager.theme.COLOR_STRONGEST);
		Border b2 = new EmptyBorder(0, styleManager.size.PADDING, 0, 0);
		return BorderFactory.createCompoundBorder(b1, b2);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}
	
	private FlatButton getResetButton(StyleManager styleManager) {
		FlatButton button = new FlatButton(styleManager, I18n.get("reset"));
		button.addActionListener(p -> dataStateHandler.reset());
		return button;
	}
	
	private FlatButton getUndoButton(StyleManager styleManager) {
		FlatButton button = new FlatButton(styleManager, I18n.get("undo"));
		button.addActionListener(p -> dataStateHandler.undo());
		return button;
	}
	
}
