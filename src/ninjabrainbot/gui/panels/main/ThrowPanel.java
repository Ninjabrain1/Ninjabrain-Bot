package ninjabrainbot.gui.panels.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.IThrowSet;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.WrappedColor;
import ninjabrainbot.io.preferences.BooleanPreference;

/**
 * JComponent for showing a Throw.
 */
public class ThrowPanel extends ThemedPanel implements IDisposable {

	private static final long serialVersionUID = -1522335220282509326L;

	DivineContextPanel divineContextPanel;
	
	private ChunkPrediction lastTopPrediction;

	private int index;
	private IThrow t;
	private JLabel x;
	private JLabel z;
	private JLabel alpha;
	private JLabel correction;
	private JLabel error;
	private FlatButton removeButton;

	private boolean errorsEnabled;
	private int correctionSgn;
	private Color colorNeg, colorPos;

	private Subscription throwSetSubscription;
	private Subscription chunkPredictionModifiedSubscription;
	private Subscription chunkPredictionChangedSubscription;
	private Runnable whenVisibilityChanged;

	private WrappedColor negCol;
	private WrappedColor posCol;
	private WrappedColor lineCol;

	public ThrowPanel(StyleManager styleManager, IDataStateHandler dataStateHandler, IObservable<ChunkPrediction> topResult, int index, Runnable whenVisibilityChanged, BooleanPreference showAngleErrors) {
		super(styleManager);
		this.index = index;
		this.whenVisibilityChanged = whenVisibilityChanged;

		setOpaque(true);
		errorsEnabled = showAngleErrors.get();
		x = new JLabel((String) null, 0);
		z = new JLabel((String) null, 0);
		alpha = new JLabel((String) null, 0);
		correction = new JLabel((String) null, 0);
		error = new JLabel((String) null, 0);
		removeButton = new FlatButton(styleManager, "–");
		removeButton.setBackgroundColor(styleManager.currentTheme.COLOR_NEUTRAL);
		removeButton.setHoverColor(styleManager.currentTheme.COLOR_EXIT_BUTTON_HOVER);
		removeButton.setVisible(false);
		removeButton.addActionListener(p -> dataStateHandler.removeThrow(this.t));
		add(removeButton);
		add(x);
		add(z);
		add(alpha);
		add(correction);
		add(error);
		setLayout(null);
		updateVisibility();

		IThrowSet throwSet = dataStateHandler.getDataState().getThrowSet();
		setThrow(index < throwSet.size() ? throwSet.get(index) : null);
		throwSetSubscription = throwSet.whenElementAtIndexModified().subscribeEDT(t -> setThrow(t), index);

		setPrediction(topResult.get());
		chunkPredictionChangedSubscription = topResult.subscribe(result -> setPrediction(result));

		setBackgroundColor(styleManager.currentTheme.COLOR_NEUTRAL);
		setForegroundColor(styleManager.currentTheme.TEXT_COLOR_NEUTRAL);
		negCol = styleManager.currentTheme.COLOR_NEGATIVE;
		posCol = styleManager.currentTheme.COLOR_POSITIVE;
		lineCol = styleManager.currentTheme.COLOR_DIVIDER;
	}

	public void setPrediction(ChunkPrediction p) {
		lastTopPrediction = p;
		if (chunkPredictionModifiedSubscription != null)
			chunkPredictionModifiedSubscription.cancel();
		chunkPredictionModifiedSubscription = null;
		if (p != null)
			chunkPredictionModifiedSubscription = p.whenModified().subscribe(pred -> updateError(pred));
		updateError(p);
	}

	void setAngleErrorsEnabled(boolean e) {
		errorsEnabled = e;
		error.setVisible(errorsEnabled);
	}

	private void updateError(ChunkPrediction p) {
		error.setText(t == null || p == null ? null : String.format(Locale.US, "%.3f", p.getAngleError(t)));
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
		if (correction != null)
			correction.setFont(font);
		if (error != null)
			error.setFont(font);
		if (removeButton != null)
			removeButton.setFont(font);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int w = width - 2 * 0 - height;
		if (!errorsEnabled) {
			if (this.x != null)
				this.x.setBounds(0, 0, w / 3, height);
			if (this.z != null)
				this.z.setBounds(0 + w / 3, 0, w / 3, height);
			if (this.alpha != null) {
				if (correctionSgn != 0) {
					int w1 = w / 3 * 3 / 4;
					int dx = w / 3 * 1 / 8;
					this.alpha.setBounds(0 + 2 * w / 3 - dx, 0, w1, height);
					this.alpha.setHorizontalAlignment(SwingConstants.RIGHT);
					this.correction.setBounds(0 + 2 * w / 3 + w1 - dx, 0, w1, height);
					this.correction.setHorizontalAlignment(SwingConstants.LEFT);
				} else {
					this.alpha.setBounds(0 + 2 * w / 3, 0, w / 3, height);
					this.alpha.setHorizontalAlignment(SwingConstants.CENTER);
				}
			}
			if (this.removeButton != null)
				this.removeButton.setBounds(w, 0, height, height - 1);
		} else {
			if (this.x != null)
				this.x.setBounds(0, 0, w / 4, height);
			if (this.z != null)
				this.z.setBounds(0 + w / 4, 0, w / 4, height);
			if (this.alpha != null) {
				if (correctionSgn != 0) {
					int w1 = w / 4 * 3 / 4;
					int dx = w / 4 * 1 / 8;
					this.alpha.setBounds(0 + 2 * w / 4 - dx, 0, w1, height);
					this.alpha.setHorizontalAlignment(SwingConstants.RIGHT);
					this.correction.setBounds(0 + 2 * w / 4 + w1 - dx, 0, w1, height);
					this.correction.setHorizontalAlignment(SwingConstants.LEFT);
				} else {
					this.alpha.setBounds(0 + 2 * w / 4, 0, w / 4, height);
					this.alpha.setHorizontalAlignment(SwingConstants.CENTER);
				}
			}
			if (this.error != null)
				this.error.setBounds(0 + 3 * w / 4, 0, w / 4, height);
			if (this.removeButton != null)
				this.removeButton.setBounds(w, 0, height, height - 1);
		}
		error.setVisible(errorsEnabled);
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
		if (correction != null)
			correction.setForeground(correctionSgn > 0 ? colorPos : colorNeg);
		if (error != null)
			error.setForeground(fg);
	}

	@Override
	public void updateColors() {
		colorNeg = negCol.color();
		colorPos = posCol.color();
		setBorder(new MatteBorder(0, 0, 1, 0, lineCol.color()));
		super.updateColors();
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		super.updateSize(styleManager);
		setPreferredSize(new Dimension(styleManager.size.WIDTH, styleManager.size.TEXT_SIZE_SMALL + styleManager.size.PADDING_THIN * 2));
	}

	private void setThrow(IThrow t) {
		if (this.t != t) {
			this.t = t;
		}
		if (t == null) {
			x.setText(null);
			z.setText(null);
			alpha.setText(null);
			correction.setText(null);
			error.setText(null);
			removeButton.setVisible(false);
			correctionSgn = 0;
		} else {
			x.setText(String.format(Locale.US, "%.2f", t.x()));
			z.setText(String.format(Locale.US, "%.2f", t.z()));
			alpha.setText(String.format(Locale.US, "%.2f", t.alpha_0()));
			correctionSgn = Math.abs(t.correction()) < 1e-7 ? 0 : (t.correction() > 0 ? 1 : -1);
			if (correctionSgn != 0) {
				correction.setText(String.format(Locale.US, t.correction() > 0 ? "+%.2f" : "%.2f", t.correction()));
				correction.setForeground(t.correction() > 0 ? colorPos : colorNeg);
			} else {
				correction.setText(null);
			}
			updateError(lastTopPrediction);
			removeButton.setVisible(true);
		}
		updateVisibility();
		repaint(); // Update dot
	}

	void updateVisibility() {
		int k = (divineContextPanel != null && divineContextPanel.isVisible()) ? 1 : 0;
		boolean newVisibility = index < 3 - k || hasThrow();
		if (newVisibility != isVisible()) {
			setVisible(newVisibility);
			if (whenVisibilityChanged != null)
				whenVisibilityChanged.run();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Paint dot if special std
		if (t != null && t.getStdProfileNumber() != 0) {
			if (t.getStdProfileNumber() == 1) {
				g.setColor(Color.RED);
			} else if (t.getStdProfileNumber() == 2) {
				g.setColor(Color.CYAN);
			}
			int a = 3;
			int b = 2;
			g.fillRect(b, b, a, a);
		}
	}

	private boolean hasThrow() {
		return t != null;
	}

	@Override
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}

	@Override
	public void dispose() {
		throwSetSubscription.cancel();
		chunkPredictionChangedSubscription.cancel();
		if (chunkPredictionModifiedSubscription != null)
			chunkPredictionModifiedSubscription.cancel();
	}

	public void setDivineContextPanel(DivineContextPanel divineContextPanel) {
		this.divineContextPanel = divineContextPanel;
		updateVisibility();
	}

}
