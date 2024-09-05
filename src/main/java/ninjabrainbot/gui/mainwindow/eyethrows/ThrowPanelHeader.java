package ninjabrainbot.gui.mainwindow.eyethrows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.BooleanPreference;
import ninjabrainbot.model.datastate.endereye.NormalEnderEyeThrow;
import ninjabrainbot.util.I18n;

/**
 * JComponent for showing a Throw.
 */
public class ThrowPanelHeader extends ThemedPanel {

	private final JLabel x;
	private final JLabel z;
	private final JLabel alpha;
	private final JLabel error;

	private boolean errorsEnabled;

	final WrappedColor lineColor;

	public ThrowPanelHeader(StyleManager styleManager, BooleanPreference showAngleErrors) {
		this(styleManager, showAngleErrors, null);
	}

	public ThrowPanelHeader(StyleManager styleManager, BooleanPreference showAngleErrors, NormalEnderEyeThrow t) {
		super(styleManager, true);
		setOpaque(true);
		errorsEnabled = showAngleErrors.get();
		x = new JLabel("x", SwingConstants.CENTER);
		z = new JLabel("z", SwingConstants.CENTER);
		alpha = new JLabel(I18n.get("angle"), SwingConstants.CENTER);
		error = new JLabel(I18n.get("error"), SwingConstants.CENTER);
		add(x);
		add(z);
		add(alpha);
		add(error);
		setLayout(null);
		lineColor = styleManager.currentTheme.COLOR_DIVIDER_DARK;
		setBackgroundColor(styleManager.currentTheme.COLOR_HEADER);
		setForegroundColor(styleManager.currentTheme.TEXT_COLOR_HEADER);
	}

	public void setAngleErrorsEnabled(boolean e) {
		errorsEnabled = e;
		error.setVisible(errorsEnabled);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (x != null)
			x.setFont(font);
		if (z != null)
			z.setFont(font);
		if (alpha != null)
			alpha.setFont(font);
		if (error != null)
			error.setFont(font);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int w = width - height;
		int y0 = -1;
		if (!errorsEnabled) {
			if (this.x != null)
				this.x.setBounds(0, y0, w / 3, height);
			if (this.z != null)
				this.z.setBounds(w / 3, y0, w / 3, height);
			if (this.alpha != null)
				this.alpha.setBounds(2 * w / 3, y0, w / 3, height);
		} else {
			if (this.x != null)
				this.x.setBounds(0, y0, w / 4, height);
			if (this.z != null)
				this.z.setBounds(w / 4, y0, w / 4, height);
			if (this.alpha != null)
				this.alpha.setBounds(2 * w / 4, y0, w / 4, height);
			if (this.error != null)
				this.error.setBounds(3 * w / 4, y0, w / 4, height);
		}
		error.setVisible(errorsEnabled);
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		super.updateSize(styleManager);
		setPreferredSize(new Dimension(styleManager.size.WIDTH, styleManager.size.TEXT_SIZE_SMALL + styleManager.size.PADDING_THIN * 2));
	}

	@Override
	public void updateColors() {
		super.updateColors();
		setBorder(new MatteBorder(0, 0, 2, 0, lineColor.color()));
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (x != null)
			x.setForeground(fg);
		if (z != null)
			z.setForeground(fg);
		if (alpha != null)
			alpha.setForeground(fg);
		if (error != null)
			error.setForeground(fg);
	}

	@Override
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}

}
