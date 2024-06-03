package ninjabrainbot.gui.options;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;

import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class Histogram extends ThemedPanel {

	private int[] counts;
	private int maxCount;
	private float min;
	private float max;
	private final int numBins;

	final ArrayList<JLabel> ticks;
	final ArrayList<Float> floatTicks;
	Color histColor = Color.black;
	Color lineColor = Color.black;
	final int labelsHeight = 20;
	final int margin = 20;

	private final WrappedColor histCol;
	private final WrappedColor lineCol;
	private final StyleManager styleManager;

	public Histogram(StyleManager styleManager, int numBins) {
		super(styleManager);
		this.numBins = numBins;
		counts = new int[numBins];
		maxCount = 0;
		ticks = new ArrayList<>();
		floatTicks = new ArrayList<>();

		histCol = styleManager.currentTheme.COLOR_SATURATED;
		lineCol = styleManager.currentTheme.COLOR_DIVIDER_DARK;
		this.styleManager = styleManager;
	}

	private void updateBounds(double[] angleErrors) {
		max = 0;
		for (double error : angleErrors) {
			double positiveError = Math.abs(error);
			int digits = (int) Math.floor(Math.log10(positiveError));
			float roundedError = (float) (Math.ceil(positiveError / Math.pow(10, digits)) * Math.pow(10, digits));
			max = Math.max(max, roundedError);
		}
		min = -max;
	}

	private void updateTicks() {
		this.removeAll();
		ticks.clear();
		floatTicks.clear();

		addTick(0);
		addTick(min);
		addTick(max);
	}

	private void addTick(float tick) {
		ThemedLabel l = new ThemedLabel(this.styleManager, "" + tick);
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
			g.fillRect(margin + (int) (i * delta), getHeight() - labelsHeight - counts[i] * a, (int) ((i + 1) * delta) - (int) (i * delta), counts[i] * a);
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

	public void setData(double[] angleErrors) {
		counts = new int[numBins];
		maxCount = 0;
		updateBounds(angleErrors);
		updateTicks();
		for (double d : angleErrors) {
			addData((float) d);
		}
		repaint();
	}

}
