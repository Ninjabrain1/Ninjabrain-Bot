package ninjabrainbot.gui.panels.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.Subscription;

/**
 * JComponent for showing a Throw.
 */
public class DivineContextPanel extends ThemedPanel implements IDisposable {

	private static final long serialVersionUID = -1522335220282509326L;

	private IDivineContext dc;
	private JLabel label;
	private FlatButton removeButton;

	Subscription fossilSubscription;
	Runnable whenVisibilityChanged;

	public DivineContextPanel(StyleManager styleManager, IDivineContext dc, Runnable whenVisibilityChanged) {
		super(styleManager);
		setOpaque(true);
		label = new JLabel((String) null, 0);
		removeButton = new FlatButton(styleManager, "–") {
			static final long serialVersionUID = -7702064148275208581L;

			@Override
			public Color getHoverColor(Theme theme) {
				return theme.COLOR_REMOVE_BUTTON_HOVER;
			}

			@Override
			public Color getBackgroundColor(Theme theme) {
				return theme.COLOR_STRONGER;
			}
		};
		add(removeButton);
		add(label);
		setLayout(null);
		onFossilChanged(dc.getFossil());
		removeButton.addActionListener(p -> dc.resetFossil());
		fossilSubscription = dc.whenFossilChanged().subscribeEDT(fossil -> onFossilChanged(fossil));
		this.whenVisibilityChanged = whenVisibilityChanged;
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (label != null)
			label.setFont(font);
		if (removeButton != null)
			removeButton.setFont(font);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int w = width - height;
		if (this.label != null)
			this.label.setBounds(0, 0, w, height);
		if (this.removeButton != null)
			this.removeButton.setBounds(w, 0, height, height - 1);
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (label != null)
			label.setForeground(fg);
	}

	@Override
	public void updateColors(StyleManager styleManager) {
		setBorder(new MatteBorder(0, 0, 1, 0, styleManager.theme.COLOR_STRONGEST));
		super.updateColors(styleManager);
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		super.updateSize(styleManager);
		setPreferredSize(new Dimension(styleManager.size.WIDTH, styleManager.size.TEXT_SIZE_SMALL + styleManager.size.PADDING_THIN * 2));
	}

	public boolean hasDivineContext() {
		return dc != null;
	}

	@Override
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}

	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONGER;
	}

	@Override
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_SLIGHTLY_STRONG;
	}

	@Override
	public void dispose() {
		fossilSubscription.cancel();
	}

	private void onFossilChanged(Fossil fossil) {
		label.setText(fossil == null ? null : (I18n.get("divine") + I18n.get("fossil_number", fossil.x)));
		boolean newVisibility = fossil != null;
		if (newVisibility != isVisible()) {
			setVisible(newVisibility);
			if (whenVisibilityChanged != null)
				whenVisibilityChanged.run();
		}
	}

}
