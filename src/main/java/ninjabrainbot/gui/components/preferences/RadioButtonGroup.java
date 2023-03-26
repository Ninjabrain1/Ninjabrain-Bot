package ninjabrainbot.gui.components.preferences;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;

class RadioButtonGroup extends ThemedPanel {

	private static final long serialVersionUID = 7355615566096074105L;

	RadioButtonGroup(StyleManager styleManager, IMultipleChoicePreferenceDataType[] options, IMultipleChoicePreferenceDataType selected) {
		this(styleManager, options, selected, false);
	}

	RadioButtonGroup(StyleManager styleManager, IMultipleChoicePreferenceDataType[] options, IMultipleChoicePreferenceDataType selected, boolean verticalRadioButtons) {
		super(styleManager);
		setOpaque(false);
		ButtonGroup group = new ButtonGroup();
		setLayout(new BoxLayout(this, verticalRadioButtons ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS));
		for (IMultipleChoicePreferenceDataType option : options) {
			ActionListener listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onChanged(option);
				}
			};
			JRadioButton button = new ThemedRadioButton(styleManager, option.choiceName());
			if (selected == option)
				button.setSelected(true);
			button.addActionListener(listener);
			group.add(button);
			add(button);
			add(Box.createHorizontalStrut(10));
		}
	}

	public void onChanged(IMultipleChoicePreferenceDataType newValue) {

	}

}

class ThemedRadioButton extends JRadioButton implements ThemedComponent {

	private static final long serialVersionUID = 528589569573225972L;

	private static ImageIcon icon = new ImageIcon(Main.class.getResource("/radio_icon.png"));
	private static ImageIcon selected_icon = new ImageIcon(Main.class.getResource("/radio_selected_icon.png"));
	private static ImageIcon pressed_icon = new ImageIcon(Main.class.getResource("/radio_pressed_icon.png"));
	private static ImageIcon rollover_icon = new ImageIcon(Main.class.getResource("/radio_rollover_icon.png"));
	private static ImageIcon selected_rollover_icon = new ImageIcon(Main.class.getResource("/radio_selected_rollover_icon.png"));

	private WrappedColor bgCol;
	private WrappedColor fgCol;

	public ThemedRadioButton(StyleManager styleManager, String text) {
		super(text);
		styleManager.registerThemedComponent(this);
		setOpaque(false);
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