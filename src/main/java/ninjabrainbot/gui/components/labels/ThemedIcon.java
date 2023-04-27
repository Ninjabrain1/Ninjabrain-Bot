package ninjabrainbot.gui.components.labels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class ThemedIcon extends ThemedLabel {

	private final ImageIcon icon;

	private final WrappedColor iconColor;

	public ThemedIcon(StyleManager styleManager, ImageIcon img) {
		super(styleManager);
		setHorizontalAlignment(SwingConstants.CENTER);

		iconColor = styleManager.currentTheme.TEXT_COLOR_TITLE;
		icon = img;
	}

	@Override
	public void updateColors() {
		super.updateColors();
		setIcon(createIcon(icon, iconColor.color()));
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
