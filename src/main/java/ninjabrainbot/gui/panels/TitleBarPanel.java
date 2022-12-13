package ninjabrainbot.gui.panels;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JButton;

import ninjabrainbot.gui.style.StyleManager;

/**
 * A JPanel that allows the user to drag the application window.
 */
public class TitleBarPanel extends ThemedPanel {
	private static final long serialVersionUID = 1284709910722728189L;
	private Point initialClick;

	ArrayList<JButton> buttons;

	public TitleBarPanel(StyleManager styleManager, final Window frame) {
		super(styleManager);
		buttons = new ArrayList<JButton>();
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				initialClick = e.getPoint();
				getComponentAt(initialClick);
			}
		});
		setLayout(null);
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int thisX = frame.getLocation().x;
				int thisY = frame.getLocation().y;

				int xMoved = e.getX() - initialClick.x;
				int yMoved = e.getY() - initialClick.y;

				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				frame.setLocation(X, Y);
			}
		});
		setBackgroundColor(styleManager.currentTheme.COLOR_STRONGEST);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setBounds(width - (i + 1) * height, 0, height, height);
		}
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setPreferredSize(new Dimension(styleManager.size.WIDTH, styleManager.size.TEXT_SIZE_TITLE_LARGE + styleManager.size.PADDING_TITLE * 2 + 1));
		super.updateSize(styleManager);
	}

	public JButton addButton(JButton button) {
		add(button);
		buttons.add(button);
		return button;
	}

}