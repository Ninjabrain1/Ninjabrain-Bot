package ninjabrainbot.gui.buttons;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;

/**
 * Custom button with 'flat' graphics, otherwise behaves like a JButton.
 */
public class FlatButton extends JButton implements ThemedComponent {
	
	private static final long serialVersionUID = 3274726146609442471L;
	
	protected JLabel label; // Graphical element
	protected Color bgCol, hoverCol;
	private ImageIcon icon, icon_inverted;
	
	public FlatButton(StyleManager styleManager, ImageIcon img) {
		super();
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		setBorder(null);
		setFocusable(false);
		
		BufferedImage bi = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		// paint the Icon to the BufferedImage.
		img.paintIcon(null, g, 0, 0);
		g.dispose();
		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				bi.setRGB(i, j, ((200 << 16) | (200 << 8) | 200) ^ bi.getRGB(i, j));
			}			
		}
		icon = img;
		icon_inverted = new ImageIcon(bi);
		label = new JLabel(icon);
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
		
		label = new JLabel(text);
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
	public void updateColors(StyleManager styleManager) {
		Color bg = getBackgroundColor(styleManager.theme);
		Color hg = getHoverColor(styleManager.theme);
		setColors(bg, hg);
		label.setForeground(getForegroundColor(styleManager.theme));
		label.setIcon(styleManager.theme.BLACK_ICONS ? icon_inverted : icon);
	}
	
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_MEDIUM;
	}
	
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONG;
	}
	
	public Color getHoverColor(Theme theme) {
		return theme.COLOR_SLIGHTLY_STRONG;
	}
	
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_STRONG;
	}
	
}
