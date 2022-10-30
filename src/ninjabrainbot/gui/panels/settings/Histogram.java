package ninjabrainbot.gui.panels.settings;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;

import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.WrappedColor;
import ninjabrainbot.gui.style.StyleManager;

public class Histogram extends ThemedPanel {

	private static final long serialVersionUID = 4708333144993390247L;

	private int[] counts;
	private int maxCount;
	private float min, max;
	private int numBins;

	ArrayList<JLabel> ticks;
	ArrayList<Float> floatTicks;
	Color histColor = Color.black;
	Color lineColor = Color.black;
	int labelsHeight = 20;
	int margin = 20;

	private WrappedColor histCol;
	private WrappedColor lineCol;

	public Histogram(StyleManager styleManager, float min, float max, int numBins) {
		super(styleManager);
		this.min = min;
		this.max = max;
		this.numBins = numBins;
		counts = new int[numBins];
		maxCount = 0;
		ticks = new ArrayList<JLabel>();
		floatTicks = new ArrayList<Float>();
		addTick(styleManager, 0);
		addTick(styleManager, min);
		addTick(styleManager, max);
		
		histCol = styleManager.currentTheme.COLOR_SATURATED;
		lineCol = styleManager.currentTheme.COLOR_STRONGEST;
	}

	private void addTick(StyleManager styleManager, float tick) {
		ThemedLabel l = new ThemedLabel(styleManager, "" + tick);
		ticks.add(l);
		floatTicks.add(tick);
		add(l);
	}

	private void addData(float f) {
		float t = (f - min) / (max - min);
		int bin = (int) (t * numBins);
		if (bin < 0)
			bin = 0;
		if (bin >= numBins)
			bin = numBins - 1;
		counts[bin]++;
		if (counts[bin] > maxCount) {
			maxCount = counts[bin];
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int w = getWidth() - 2 * margin;
		int a = maxCount != 0 ? Math.min((getHeight() - labelsHeight) / maxCount, 60) : 0;
		float delta = (float) w / numBins;
		g.setColor(histColor);
		for (int i = 0; i < numBins; i++) {
			g.fillRect(margin + (int) (i * delta), getHeight() - labelsHeight, (int) ((i + 1) * delta) - (int) (i * delta), -counts[i] * a);
		}
		g.setColor(lineColor);
		g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight() - labelsHeight);
		g.drawLine(margin, getHeight() - labelsHeight, getWidth() - margin, getHeight() - labelsHeight);
		for (int i = 0; i < ticks.size(); i++) {
			float t = (floatTicks.get(i) - min) / (max - min);
			ticks.get(i).setLocation(margin + (int) (t * w) - ticks.get(i).getWidth() / 2, getHeight() - labelsHeight);
		}
	}

	@Override
	public void updateColors() {
		super.updateColors();
		histColor = histCol.color();
		lineColor = lineCol.color();
	}

	public void clear() {
		counts = new int[numBins];
		maxCount = 0;
	}

	public void setData(double[] angleErrors) {
		counts = new int[numBins];
		maxCount = 0;
		for (double d : angleErrors) {
			addData((float) d);
		}
		repaint();
	}

}
