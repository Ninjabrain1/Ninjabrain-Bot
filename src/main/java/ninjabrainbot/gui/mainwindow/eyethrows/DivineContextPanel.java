package ninjabrainbot.gui.mainwindow.eyethrows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.input.IButtonInputHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.util.I18n;

/**
 * JComponent for showing a Throw.
 */
public class DivineContextPanel extends ThemedPanel implements IDisposable {

	private IDivineContext dc;
	private final JLabel label;
	private final FlatButton removeButton;

	final Subscription fossilSubscription;
	final Runnable whenVisibilityChanged;

	private final WrappedColor borderCol;

	public DivineContextPanel(StyleManager styleManager, IDivineContext divineContext, IButtonInputHandler buttonInputHandler, Runnable whenVisibilityChanged) {
		super(styleManager);
		setOpaque(true);
		label = new JLabel((String) null, SwingConstants.CENTER);
		removeButton = new FlatButton(styleManager, "-");
		removeButton.setBackgroundColor(styleManager.currentTheme.COLOR_DIVIDER);
		removeButton.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_STRONG);
		removeButton.setHoverColor(styleManager.currentTheme.COLOR_EXIT_BUTTON_HOVER);
		add(removeButton);
		add(label);
		setLayout(null);
		onFossilChanged(divineContext.getFossil());
		removeButton.addActionListener(__ -> buttonInputHandler.onRemoveFossilButtonPressed());
		fossilSubscription = divineContext.fossil().subscribeEDT(this::onFossilChanged);
		this.whenVisibilityChanged = whenVisibilityChanged;

		borderCol = styleManager.currentTheme.COLOR_DIVIDER_DARK;
		setBackgroundColor(styleManager.currentTheme.COLOR_DIVIDER);
		setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_STRONG);
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
	public void updateColors() {
		setBorder(new MatteBorder(0, 0, 1, 0, borderCol.color()));
		super.updateColors();
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
	public void dispose() {
		fossilSubscription.dispose();
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
