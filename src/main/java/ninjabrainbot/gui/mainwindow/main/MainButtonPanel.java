package ninjabrainbot.gui.mainwindow.main;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ninjabrainbot.gui.components.layout.StretchPanel;
import ninjabrainbot.model.input.IButtonInputHandler;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.util.I18n;

public class MainButtonPanel extends StretchPanel {

	private final WrappedColor borderCol;

	public MainButtonPanel(StyleManager styleManager, IButtonInputHandler buttonInputHandler) {
		super(styleManager, true);
		setOpaque(true);
//		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(0);
		ThemedLabel throwsLabel = new ThemedLabel(styleManager, I18n.get("ender_eye_throws"), true);
		throwsLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_HEADER);
		add(throwsLabel);
		add(Box.createHorizontalGlue());
		add(getUndoButton(styleManager, buttonInputHandler));
		add(getRedoButton(styleManager, buttonInputHandler));
		add(getResetButton(styleManager, buttonInputHandler));

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
		FlatButton button = new FlatButton(styleManager, I18n.get("reset"), true);
		button.addActionListener(p -> buttonInputHandler.onResetButtonPressed());
		return button;
	}

	private FlatButton getUndoButton(StyleManager styleManager, IButtonInputHandler buttonInputHandler) {
		FlatButton button = new FlatButton(styleManager, I18n.get("undo"), true);
		button.addActionListener(p -> buttonInputHandler.onUndoButtonPressed());
		return button;
	}

	private FlatButton getRedoButton(StyleManager styleManager, IButtonInputHandler buttonInputHandler) {
		FlatButton button = new FlatButton(styleManager, I18n.get("redo"), true);
		button.addActionListener(p -> buttonInputHandler.onRedoButtonPressed());
		return button;
	}

}
