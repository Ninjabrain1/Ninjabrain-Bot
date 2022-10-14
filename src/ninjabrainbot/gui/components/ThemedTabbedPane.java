package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ninjabrainbot.gui.StyleManager;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;

public class ThemedTabbedPane extends ThemedPanel {
	
	private static final long serialVersionUID = -3291029177930511395L;
	
	ArrayList<TabButton> tabs;
	StyleManager styleManager;
	JPanel tabPanel;
	JPanel mainPanel;
	
	public ThemedTabbedPane(StyleManager styleManager) {
		super(styleManager);
		this.styleManager = styleManager;
		tabs = new ArrayList<TabButton>();
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(null);
		tabPanel = new ThemedOpaquePanel(styleManager) {
			private static final long serialVersionUID = -9131114034270325589L;
			@Override
			public Color getBackgroundColor(Theme theme) {
				return theme.COLOR_STRONGER;
			}
		};
		tabPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		tabPanel.setOpaque(true);
		mainPanel.setOpaque(false);
		add(tabPanel);
		add(mainPanel);
	}
	
	public void addTab(String title, JComponent component) {
		mainPanel.add(component);
		component.setVisible(tabs.size() == 0);
		TabButton tabButton = new TabButton(this.styleManager, this, title, component);
		tabs.add(tabButton);
		tabPanel.add(tabButton);
	}
	
	void setVisible(TabButton tab) {
		for (TabButton t : tabs) {
			t.setComponentVisible(tab == t);
		}
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), true));
	}

	@Override
	public void updateColors(StyleManager styleManager) {
		setBackground(getBackgroundColor(styleManager.theme));
		setForeground(getForegroundColor(styleManager.theme));
	}
	
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_MEDIUM;
	}
	
	public Color getBackgroundColor(Theme theme) {
		return null;
	}
	
	public Color getForegroundColor(Theme theme) {
		return null;
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int tabPanelHeight = tabPanel.getPreferredSize().height;
		tabPanel.setBounds(0, 0, width, tabPanelHeight);
		mainPanel.setBounds(0, tabPanelHeight, width, height - tabPanelHeight);
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (tabs.size() == 0)
			return super.getPreferredSize();
		Dimension reference = tabs.get(0).component.getPreferredSize();
		return new Dimension(reference.width, reference.height + tabPanel.getPreferredSize().height);
	}
	
}
class TabButton extends FlatButton {

	private static final long serialVersionUID = 1874343984790503904L;
	
	ThemedTabbedPane parent;
	JComponent component;
	
	Color a, b, c;
	
	public TabButton(StyleManager styleManager, ThemedTabbedPane parent, String title, JComponent component) {
		super(styleManager, title);
		this.parent = parent;
		this.component = component;
		label.setCursor(null);
		addActionListener(p -> onClicked());
	}
	
	void setComponentVisible(boolean bool) {
		component.setVisible(bool);
		refreshColor();
	}
	
	private void onClicked() {
		parent.setVisible(this);
	}
	
	@Override
	public void updateColors(StyleManager styleManager) {
		super.updateColors(styleManager);
		a = this.hoverCol;
		b = this.bgCol;
		c = styleManager.theme.COLOR_NEUTRAL;
		refreshColor();
	}
	
	private void refreshColor() {
		if (component.isVisible()) {
			this.hoverCol = c;
			this.bgCol = c;
		} else {
			this.hoverCol = a;
			this.bgCol = b;
		}
		setColors(bgCol, hoverCol);
	}
	
	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONGER;
	}
	
}
