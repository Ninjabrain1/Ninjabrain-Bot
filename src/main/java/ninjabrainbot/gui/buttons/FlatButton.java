package ninjabrainbot.gui.buttons;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.components.labels.ThemedIcon;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

/**
 * Custom button with 'flat' graphics, otherwise behaves like a JButton.
 */
public class FlatButton extends JButton implements ThemedComponent {

	private static final long serialVersionUID = 3274726146609442471L;

	protected ThemedLabel label; // Graphical element
	protected Color bgCol, hoverCol;

	private WrappedColor bgColor;
	private WrappedColor fgColor;
	private WrappedColor hoverColor;

	public FlatButton(StyleManager styleManager, ImageIcon img) {
		super();
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		setBorder(null);
		setFocusable(false);

		bgColor = styleManager.currentTheme.COLOR_HEADER;
		fgColor = styleManager.currentTheme.TEXT_COLOR_HEADER;
		hoverColor = styleManager.currentTheme.COLOR_SLIGHTLY_STRONG;

		label = new ThemedIcon(styleManager, img);
		label.setOpaque(true);
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.add(label);
		this.setLayout(null);
		// Change color on hover
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				label.setBackground(isEnabled() ? hoverCol : bgCol);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				label.setBackground(bgCol);
			}
		});
		styleManager.registerThemedComponent(this);
	}

	public FlatButton(StyleManager styleManager, String text) {
		super();
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		setBorder(null);
		setFocusable(false);

		bgColor = styleManager.currentTheme.COLOR_HEADER;
		fgColor = styleManager.currentTheme.TEXT_COLOR_HEADER;
		hoverColor = styleManager.currentTheme.COLOR_SLIGHTLY_STRONG;

		label = new ThemedLabel(styleManager, text);
		label.setOpaque(true);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.add(label);
		this.setLayout(null);
		// Change color on hover
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				label.setBackground(isEnabled() ? hoverCol : bgCol);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				label.setBackground(bgCol);
			}
		});
		styleManager.registerThemedComponent(this);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (label != null) {
			label.setFont(font);
		}
	}

	@Override
	public void setText(String text) {
		label.setText(text);
	}

	public void setColors(final Color backgroundColor, final Color hoverColor) {
		label.setBackground(backgroundColor);
		this.bgCol = backgroundColor;
		this.hoverCol = hoverColor;
	}

	public void setBackgroundColor(final Color backgroundColor) {
		label.setBackground(backgroundColor);
		this.bgCol = backgroundColor;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = label.getPreferredSize();
		dim.height += 8;
		dim.width += 16;
		return dim;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		label.setBounds(0, 0, width, height);
		super.setBounds(x, y, width, height);
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), true));
	}

	@Override
	public void updateColors() {
		Color bg = getBackgroundColor();
		Color hg = getHoverColor();
		setColors(bg, hg);
		label.setForeground(getForegroundColor());
	}

	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_MEDIUM;
	}

	public void setBackgroundColor(WrappedColor color) {
		bgColor = color;
	}

	public void setHoverColor(WrappedColor color) {
		hoverColor = color;
	}

	public void setForegroundColor(WrappedColor color) {
		fgColor = color;
	}

	protected Color getBackgroundColor() {
		return bgColor.color();
	}

	protected Color getHoverColor() {
		return hoverColor.color();
	}

	protected Color getForegroundColor() {
		return fgColor.color();
	}

}
