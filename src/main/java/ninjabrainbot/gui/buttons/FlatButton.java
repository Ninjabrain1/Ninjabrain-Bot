package ninjabrainbot.gui.buttons;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

/**
 * Custom button with 'flat' graphics, otherwise behaves like a JButton.
 */
public class FlatButton extends JButton implements ThemedComponent {

	private final boolean dimTextWhenNotHovered;
	private final ImageIcon img;
	protected Color bgCol, hoverCol, fgCol, fgHoverCol;

	private WrappedColor bgColor;
	private WrappedColor fgHoverColor;
	private WrappedColor hoverColor;
	private final WrappedColor iconColor;

	public FlatButton(StyleManager styleManager, ImageIcon img) {
		super();
		this.img = img;
		dimTextWhenNotHovered = false;
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		setBorder(null);
		setFocusable(false);
		setOpaque(true);

		bgColor = styleManager.currentTheme.COLOR_HEADER;
		fgHoverColor = styleManager.currentTheme.TEXT_COLOR_HEADER;
		hoverColor = styleManager.currentTheme.COLOR_SLIGHTLY_STRONG;
		iconColor = styleManager.currentTheme.TEXT_COLOR_TITLE;

		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				setBackground(isEnabled() ? hoverCol : bgCol);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				setBackground(bgCol);
			}
		});
		styleManager.registerThemedComponent(this);
	}

	public FlatButton(StyleManager styleManager, String text) {
		this(styleManager, text, false);
	}

	public FlatButton(StyleManager styleManager, String text, boolean dimTextWhenNotHovered) {
		super(text);
		this.dimTextWhenNotHovered = dimTextWhenNotHovered;
		this.img = null;
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		setBorder(new EmptyBorder(4, 8, 4, 8));
		setFocusable(false);
		setOpaque(true);

		bgColor = styleManager.currentTheme.COLOR_HEADER;
		fgHoverColor = styleManager.currentTheme.TEXT_COLOR_HEADER;
		hoverColor = styleManager.currentTheme.COLOR_SLIGHTLY_STRONG;
		iconColor = styleManager.currentTheme.TEXT_COLOR_TITLE;

		setForeground(Color.WHITE);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		// Change color on hover
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				setBackground(isEnabled() ? hoverCol : bgCol);
				setForeground(isEnabled() ? fgHoverCol : fgCol);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				setBackground(bgCol);
				setForeground(fgCol);
			}
		});
		styleManager.registerThemedComponent(this);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
	}

	private void setColors(final Color backgroundColor, final Color hoverColor, final Color foregroundColor, final Color foregroundHoverColor) {
		setBackground(backgroundColor);
		this.bgCol = backgroundColor;
		this.hoverCol = hoverColor;
		this.fgCol = foregroundColor;
		this.fgHoverCol = foregroundHoverColor;
	}

	public void setBackgroundColor(final Color backgroundColor) {
		setBackground(backgroundColor);
		this.bgCol = backgroundColor;
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), true));
	}

	@Override
	public void updateColors() {
		Color bg = getBackgroundColor();
		Color hg = getHoverColor();
		setColors(bg, hg, fgHoverColor.interpolate(getBackgroundColor(), dimTextWhenNotHovered ? 0.4f : 0), getForegroundColor());
		setForeground(fgCol);
		if (img != null)
			setIcon(createIcon(img, iconColor.color()));
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
		fgHoverColor = color;
	}

	protected Color getBackgroundColor() {
		return bgColor.color();
	}

	protected Color getHoverColor() {
		return hoverColor.color();
	}

	protected Color getForegroundColor() {
		return fgHoverColor.color();
	}

	private ImageIcon createIcon(ImageIcon img, Color c) {
		BufferedImage bi = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TRANSLUCENT);
		Graphics graphics = bi.createGraphics();
		img.paintIcon(null, graphics, 0, 0);
		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				int argb = bi.getRGB(i, j);
				int a = c.getAlpha() * (argb >> 24);
				int r = c.getRed() * ((argb >> 16) & 0b11111111);
				int g = c.getGreen() * ((argb >> 8) & 0b11111111);
				int b = c.getBlue() * (argb & 0b11111111);
				bi.setRGB(i, j, (a / 255 << 24) | (r / 255 << 16) | (g / 255 << 8) | (b / 255));
			}
		}
		graphics.dispose();
		return new ImageIcon(bi);
	}

}
