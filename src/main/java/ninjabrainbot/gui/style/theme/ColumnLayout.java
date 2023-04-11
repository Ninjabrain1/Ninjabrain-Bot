package ninjabrainbot.gui.style.theme;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.HashSet;

public class ColumnLayout implements LayoutManager, java.io.Serializable {

	final int horizontalGap;

	final HashMap<Component, Float> relativeWidths;
	final HashSet<Component> componentsWithReservedSpaceWhenHidden;

	public ColumnLayout(int horizontalGap) {
		this.horizontalGap = horizontalGap;
		relativeWidths = new HashMap<>();
		componentsWithReservedSpaceWhenHidden = new HashSet<>();
	}

	public void setRelativeWidth(Component component, float width) {
		setRelativeWidth(component, width, false);
	}

	public void setRelativeWidth(Component component, float width, boolean reserveSpaceWhenHidden) {
		relativeWidths.put(component, width);
		if (reserveSpaceWhenHidden)
			componentsWithReservedSpaceWhenHidden.add(component);
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
			int numberOfComponents = parent.getComponentCount();
			int w = 0;
			int h = 0;
			for (int i = 0; i < numberOfComponents; i++) {
				Component comp = parent.getComponent(i);
				Dimension d = comp.getPreferredSize();
				if (w < d.width) {
					w = d.width;
				}
				if (h < d.height) {
					h = d.height;
				}
			}
			return new Dimension(insets.left + insets.right + numberOfComponents * w + (numberOfComponents - 1) * horizontalGap, insets.top + insets.bottom + h);
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
			int numberOfComponents = parent.getComponentCount();

			if (numberOfComponents == 0) {
				return;
			}
			float totalWeight = 0;
			for (Component component : parent.getComponents()) {
				if (component.isVisible() || componentsWithReservedSpaceWhenHidden.contains(component)) {
					totalWeight += relativeWidths.getOrDefault(component, 1f);
				}
			}
			int totalGapsWidth = (numberOfComponents - 1) * horizontalGap;
			int widthWOInsets = parent.getWidth() - (insets.left + insets.right);
			int widthOnComponent = (int) ((widthWOInsets - totalGapsWidth) / totalWeight);
			int extraWidthAvailable = (int) ((widthWOInsets - (widthOnComponent * totalWeight + totalGapsWidth)) / 2);

			int totalGapsHeight = 0;
			int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
			int heightOnComponent = (heightWOInsets - totalGapsHeight);
			int extraHeightAvailable = (heightWOInsets - (heightOnComponent + totalGapsHeight)) / 2;
			for (int componentIndex = 0, x = insets.left + extraWidthAvailable; componentIndex < numberOfComponents; componentIndex++) {
				Component component = parent.getComponent(componentIndex);
				int componentWidth = (int) (widthOnComponent * relativeWidths.getOrDefault(component, 1f));
				if (componentIndex == numberOfComponents - 1)
					x = widthWOInsets - componentWidth;
				parent.getComponent(componentIndex).setBounds(x, insets.top + extraHeightAvailable, componentWidth, heightOnComponent);
				x += componentWidth + horizontalGap;
			}
		}
	}

}
