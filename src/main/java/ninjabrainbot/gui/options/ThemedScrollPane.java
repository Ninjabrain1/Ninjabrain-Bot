package ninjabrainbot.gui.options;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class ThemedScrollPane extends JScrollPane implements ThemedComponent {

	private static final long serialVersionUID = 136357780584264L;
	public boolean bold;

	private WrappedColor bgCol;
	private WrappedColor barCol;
	private WrappedColor barBgCol;

	public ThemedScrollPane(StyleManager styleManager, Component c) {
		super(c, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		styleManager.registerThemedComponent(this);

		bgCol = styleManager.currentTheme.COLOR_NEUTRAL;
		barCol = styleManager.currentTheme.COLOR_STRONGEST;
		barBgCol = styleManager.currentTheme.COLOR_SLIGHTLY_STRONG;

		getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
		getVerticalScrollBar().setUnitIncrement(16);
		getVerticalScrollBar().setUI(createUI());
	}

	public void updateSize(StyleManager styleManager) {
	}

	@Override
	public void updateColors() {
		Color bg = getBackgroundColor();
		setBackground(bg);
		getVerticalScrollBar().setUI(createUI());
	}

	public void setBackgroundColor(WrappedColor color) {
		bgCol = color;
	}

	protected Color getBackgroundColor() {
		return bgCol.color();
	}

	private BasicScrollBarUI createUI() {
		return new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = barCol.color();
				this.trackColor = barBgCol.color();
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
				if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
					return;
				}

				int w = thumbBounds.width;
				int h = thumbBounds.height;

				g.translate(thumbBounds.x, thumbBounds.y);

				g.setColor(thumbColor);
				g.fillRect(0, 0, w, h);

				g.translate(-thumbBounds.x, -thumbBounds.y);
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return new ZeroSizeButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return new ZeroSizeButton();
			}
		};
	}

}

class ZeroSizeButton extends JButton {
	private static final long serialVersionUID = -2116256276789716547L;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension();
	}
}
