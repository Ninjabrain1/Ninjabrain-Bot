package ninjabrainbot.gui.mainwindow.main;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.input.IButtonInputHandler;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.util.I18n;

public class MainButtonPanel extends ThemedPanel {

	private final WrappedColor borderCol;

	public MainButtonPanel(StyleManager styleManager, IDataState dataState, IButtonInputHandler buttonInputHandler) {
		super(styleManager);
		setOpaque(true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(0);
		ThemedLabel throwsLabel = new ThemedLabel(styleManager, I18n.get("ender_eye_throws"), true);
		throwsLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_HEADER);
		FlatButton resetButton = getResetButton(styleManager, buttonInputHandler);
		FlatButton undoButton = getUndoButton(styleManager, buttonInputHandler);
		add(throwsLabel);
		add(Box.createHorizontalGlue());
		add(undoButton);
		add(resetButton);

		borderCol = styleManager.currentTheme.COLOR_DIVIDER_DARK;
		setBackgroundColor(styleManager.currentTheme.COLOR_HEADER);
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setPreferredSize(new Dimension(0, 24));
		setBorder(getBorder(styleManager.size.PADDING));
		super.updateSize(styleManager);
	}

	@Override
	public void updateColors() {
		super.updateColors();
		setBorder(getBorder(lastPadding));
	}

	private int lastPadding;

	private Border getBorder(int padding) {
		lastPadding = padding;
		Border b1 = new MatteBorder(1, 0, 0, 0, borderCol.color());
		Border b2 = new EmptyBorder(0, padding, 0, 0);
		return BorderFactory.createCompoundBorder(b1, b2);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}

	private FlatButton getResetButton(StyleManager styleManager, IButtonInputHandler buttonInputHandler) {
		FlatButton button = new FlatButton(styleManager, I18n.get("reset"));
		button.addActionListener(p -> buttonInputHandler.onResetButtonPressed());
		return button;
	}

	private FlatButton getUndoButton(StyleManager styleManager, IButtonInputHandler buttonInputHandler) {
		FlatButton button = new FlatButton(styleManager, I18n.get("undo"));
		button.addActionListener(p -> buttonInputHandler.onUndoButtonPressed());
		return button;
	}

}
