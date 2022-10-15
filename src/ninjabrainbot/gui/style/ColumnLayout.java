package ninjabrainbot.gui.style;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.HashMap;

public class ColumnLayout implements LayoutManager, java.io.Serializable {

	private static final long serialVersionUID = -2688561683995591776L;

	int hgap;
	
	HashMap<Component, Float> relativeWidths;
	
	public ColumnLayout(int hgap) {
		this.hgap = hgap;
		relativeWidths = new HashMap<Component, Float>();
	}
	
	public void setRelativeWidth(Component comp, float w) {
		relativeWidths.put(comp, w);
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int w = 0;
			int h = 0;
			for (int i = 0; i < ncomponents; i++) {
				Component comp = parent.getComponent(i);
				Dimension d = comp.getPreferredSize();
				if (w < d.width) {
					w = d.width;
				}
				if (h < d.height) {
					h = d.height;
				}
			}
			return new Dimension(insets.left + insets.right + ncomponents * w + (ncomponents - 1) * hgap,
					insets.top + insets.bottom + h);
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(0, 0);
	}

	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int ncols = ncomponents;

			if (ncomponents == 0) {
				return;
			}
			float totalWeight = 0;
			for (Component c : parent.getComponents()) {
				if (c.isVisible()) {
					totalWeight += relativeWidths.getOrDefault(c, 1f);
				}
			}
			int totalGapsWidth = (ncols - 1) * hgap;
			int widthWOInsets = parent.getWidth() - (insets.left + insets.right);
			int widthOnComponent = (int) ((widthWOInsets - totalGapsWidth) / totalWeight);
			int extraWidthAvailable = (int) ((widthWOInsets - (widthOnComponent * totalWeight + totalGapsWidth)) / 2);

			int totalGapsHeight = 0;
			int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
			int heightOnComponent = (heightWOInsets - totalGapsHeight);
			int extraHeightAvailable = (heightWOInsets - (heightOnComponent + totalGapsHeight)) / 2;
			for (int c = 0, x = insets.left + extraWidthAvailable; c < ncols; c++) {
				Component component = parent.getComponent(c);
				int componentWidth = (int) (widthOnComponent * relativeWidths.getOrDefault(component, 1f));
				if (c < ncomponents) {
					parent.getComponent(c).setBounds(x, insets.top + extraHeightAvailable, componentWidth, heightOnComponent);
				}
				x += componentWidth + hgap;
			}
		}
	}

}
