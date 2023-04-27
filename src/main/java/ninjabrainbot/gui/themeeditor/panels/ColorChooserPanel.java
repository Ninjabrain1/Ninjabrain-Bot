package ninjabrainbot.gui.themeeditor.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JPanel;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.ObservableProperty;

public class ColorChooserPanel extends JPanel {
	private static final int WIDTH = 256;
	private static final int HEIGHT = WIDTH;
	private static final int BAR_WIDTH = 20;
	private final ColorPanel colorPanel;
	private final ColorBar colorBar;
	private final ObservableField<Color> color;

	public ColorChooserPanel() {
		setOpaque(false);
		color = new ObservableField<>();
		colorPanel = new ColorPanel(WIDTH, HEIGHT);
		colorBar = new ColorBar(BAR_WIDTH, HEIGHT);
		setColor(Color.BLACK);
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		add(colorPanel, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(5));
		add(colorBar, BorderLayout.LINE_END);

		colorBar.whenCursorChanged().subscribe(this::onBarUpdated);
		colorPanel.whenCursorChanged().subscribe(this::onPanelUpdated);
	}

	public ISubscribable<Color> whenColorChanged() {
		return color;
	}

	private void onBarUpdated(Point p) {
		Color c = colorBar.getColor(p);
		colorPanel.setColorPropertyValue(c);
		color.set(colorPanel.getColor());
	}

	private void onPanelUpdated(Point p) {
		Color c = colorPanel.getColor(p);
		colorBar.setColorPropertyValue(c);
		color.set(c);
	}

	public void setColor(Color c) {
		if (colorsAreSame(c, color.get()))
			return;
		colorPanel.setCursor(c);
		colorPanel.setColorPropertyValue(c);
		colorBar.setCursor(c);
		colorBar.setColorPropertyValue(c);
		color.set(c);
	}

	private boolean colorsAreSame(Color a, Color b) {
		if (a == null || b == null)
			return a == b;
		return a.getRGB() == b.getRGB();
	}

	public Color getColor() {
		return color.get();
	}

}

abstract class ColorPanelBase extends JPanel {

	public final int width;
	public final int height;
	protected Point cursor_ = new Point();
	private final ObservableProperty<Point> cursor = new ObservableProperty<>();
	private BufferedImage img = null;

	public ColorPanelBase(int width, int height) {
		this.width = width;
		this.height = height;
		MouseAdapter mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseResponse(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseResponse(e);
			}

			private void mouseResponse(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				x = Math.max(0, x);
				y = Math.max(0, y);
				x = Math.min(width - 1, x);
				y = Math.min(height - 1, y);
				cursor_ = new Point(x, y);
				cursor.notifySubscribers(cursor_);
				repaint();
			}
		};
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	public BufferedImage getImg() {
		return img;
	}

	public ISubscribable<Point> whenCursorChanged() {
		return cursor;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public Color getColor(Point p) {
		Color c = null;
		if (getImg() != null) {
			int rgb = getImg().getRGB(p.x, p.y);
			c = new Color(rgb);
		}
		return c;
	}

	public Color getColor() {
		return getColor(cursor_);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null) {
			g.drawImage(img, 0, 0, this);
		}
	}

	public void setColorPropertyValue(Color color) {
		setImg(createImage(color, width, height));
		repaint();
	}

	public abstract void setCursor(Color color);

	public abstract BufferedImage createImage(Color color, int w, int h);

}

class ColorBar extends ColorPanelBase {

	public ColorBar(int prefW, int prefH) {
		super(prefW, prefH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		int y = cursor_.y;
		g.drawLine(0, y, width, y);
	}

	public BufferedImage createImage(Color color, int w, int h) {
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				float hue = (h - (float) i) / (float) h;
				int rgb = Color.getHSBColor(hue, 1f, 1f).getRGB();
				img.setRGB(j, i, rgb);
			}
		}
		return img;
	}

	@Override
	public void setCursor(Color color) {
		float hue = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
		cursor_.y = (int) (height * (1f - hue));
	}

}

class ColorPanel extends ColorPanelBase {

	private static final int CURSOR_RADIUS = 8;

	public ColorPanel(int prefW, int prefH) {
		super(prefW, prefH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		int x = cursor_.x;
		int y = cursor_.y;
		int x1 = x - CURSOR_RADIUS;
		int y1 = y - CURSOR_RADIUS;
		int x2 = x + CURSOR_RADIUS;
		int y2 = y + CURSOR_RADIUS;
		g.drawLine(x1, y, x2, y);
		g.drawLine(x, y1, x, y2);
	}

	public BufferedImage createImage(Color color, int w, int h) {
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		float hue = Color.RGBtoHSB(red, green, blue, null)[0];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				float s = ((float) j) / (float) (w - 1);
				float b = (h - 1 - (float) i) / (float) (h - 1);
				int rgb = Color.getHSBColor(hue, s, b).getRGB();
				img.setRGB(j, i, rgb);
			}
		}
		return img;
	}

	@Override
	public void setCursor(Color color) {
		float[] hsv = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		cursor_.y = (int) ((height - 1) * (1f - hsv[2]));
		cursor_.x = (int) ((width - 1) * hsv[1]);
	}

}
