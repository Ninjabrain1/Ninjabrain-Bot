package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;

public class ThemedTabbedPane extends ThemedPanel {
	
	private static final long serialVersionUID = -3291029177930511395L;
	
	ArrayList<TabButton> tabs;
	GUI gui;
	JPanel tabPanel;
	JPanel mainPanel;
	
	public ThemedTabbedPane(GUI gui) {
		super(gui);
		this.gui = gui;
		tabs = new ArrayList<TabButton>();
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(null);
		tabPanel = new ThemedOpaquePanel(gui) {
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
		TabButton tabButton = new TabButton(this.gui, this, title, component);
		tabs.add(tabButton);
		tabPanel.add(tabButton);
	}
	
	void setVisible(TabButton tab) {
		for (TabButton t : tabs) {
			t.setComponentVisible(tab == t);
		}
	}

	@Override
	public void updateSize(GUI gui) {
		setFont(gui.fontSize(getTextSize(gui.size), true));
	}

	@Override
	public void updateColors(GUI gui) {
		setBackground(getBackgroundColor(gui.theme));
		setForeground(getForegroundColor(gui.theme));
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
	
	public TabButton(GUI gui, ThemedTabbedPane parent, String title, JComponent component) {
		super(gui, title);
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
	public void updateColors(GUI gui) {
		super.updateColors(gui);
		a = this.hoverCol;
		b = this.bgCol;
		c = gui.theme.COLOR_NEUTRAL;
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
