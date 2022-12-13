package ninjabrainbot.gui.settings.themeeditor.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FramePreviewPanel extends JPanel {

	private static final long serialVersionUID = -2260572252331237605L;

	private JFrame frame;

	private BufferedImage img;

	public FramePreviewPanel(JFrame frame) {
		this.frame = frame;
		frame.setSize(frame.getPreferredSize());
		frame.addNotify();
		frame.doLayout();
	}

	public void postInit() {
		img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
		renderImage();
	}

	public void renderImage() {
		frame.getContentPane().paint(img.createGraphics());
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
	}

	@Override
	public Dimension getPreferredSize() {
		return frame.getSize();
	}

	public void dispose() {
		frame.dispose();
	}

}
