package ninjabrainbot.gui.components.preferences;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Objects;

import javax.swing.*;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

class RadioButtonGroup<T extends IMultipleChoiceOption> extends ThemedPanel {

	final ButtonGroup group;

	RadioButtonGroup(StyleManager styleManager, T[] options, T selected, boolean verticalRadioButtons) {
		super(styleManager);
		setOpaque(false);
		group = new ButtonGroup();

		setLayout(new BoxLayout(this, verticalRadioButtons ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));
		int entriesPerColumn = 10;
		int numberOfColumns = options.length / entriesPerColumn + 1;

		for (int j = 0; j < numberOfColumns; j++) {
			JPanel column = new ThemedPanel(styleManager);
			column.setLayout(new BoxLayout(column, verticalRadioButtons ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS));
			if (j != 0)
				add(Box.createHorizontalStrut(16));
			add(column);
			for (int k = 0; k < entriesPerColumn; k++) {
				int i = j * entriesPerColumn + k;
				if (i >= options.length)
					break;
				T option = options[i];
				ActionListener listener = e -> onChanged(option);
				JRadioButton button = new ThemedRadioButton(styleManager, option.choiceName());
				if (selected == option)
					button.setSelected(true);
				button.addActionListener(listener);
				group.add(button);
				column.add(button);
				if (i != options.length - 1)
					column.add(verticalRadioButtons ? Box.createVerticalStrut(OptionsFrame.PADDING) : Box.createHorizontalStrut(10));
			}
			column.add(Box.createGlue());
		}
	}

	public void onChanged(T newValue) {

	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		Enumeration<AbstractButton> elements = this.group.getElements();
		while (elements.hasMoreElements()) {
			elements.nextElement().setEnabled(enabled);
		}
	}
}

class ThemedRadioButton extends JRadioButton implements ThemedComponent {

	private static final ImageIcon icon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/radio_icon.png")));
	private static final ImageIcon selected_icon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/radio_selected_icon.png")));
	private static final ImageIcon pressed_icon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/radio_pressed_icon.png")));
	private static final ImageIcon rollover_icon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/radio_rollover_icon.png")));
	private static final ImageIcon selected_rollover_icon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/radio_selected_rollover_icon.png")));

	private WrappedColor bgCol;
	private WrappedColor fgCol;

	public ThemedRadioButton(StyleManager styleManager, String text) {
		super(text);
		styleManager.registerThemedComponent(this);
		setOpaque(false);
		setMargin(new Insets(-2, -2, -2, -2));
		setBorderPainted(false);
		setFocusPainted(false);
		setFocusable(false);
		// Icons
		setIcon(icon);
		setSelectedIcon(selected_icon);
		setPressedIcon(pressed_icon);
		setRolloverIcon(rollover_icon);
		setRolloverSelectedIcon(selected_rollover_icon);

		bgCol = styleManager.currentTheme.COLOR_NEUTRAL;
		fgCol = styleManager.currentTheme.TEXT_COLOR_NEUTRAL;
	}

	public final void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), true));
	}

	@Override
	public final void updateColors() {
		Color bg = getBackgroundColor();
		setBackground(bg);
		Color fg = getForegroundColor();
		setForeground(fg);
	}

	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}

	public void setBackgroundColor(WrappedColor color) {
		bgCol = color;
	}

	public void setForegroundColor(WrappedColor color) {
		fgCol = color;
	}

	protected Color getBackgroundColor() {
		return bgCol.color();
	}

	protected Color getForegroundColor() {
		return fgCol.color();
	}

}