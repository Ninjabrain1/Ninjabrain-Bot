package ninjabrainbot.gui.components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import ninjabrainbot.gui.style.StyleManager;

public class ThemedIcon extends ThemedLabel {

	private static final long serialVersionUID = 6219710623002938323L;

	private ImageIcon icon, icon_inverted;

	public ThemedIcon(StyleManager styleManager, ImageIcon img) {
		super(styleManager);
		setIcon(img);
		setHorizontalAlignment(SwingConstants.LEFT);
		
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
	}
	
	@Override
	public void updateColors(StyleManager styleManager) {
		super.updateColors(styleManager);
		setIcon(styleManager.theme.BLACK_ICONS ? icon_inverted : icon);
	}

}
