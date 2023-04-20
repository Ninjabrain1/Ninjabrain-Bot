package ninjabrainbot.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.layout.Divider;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.options.Histogram;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.calibrator.Calibrator;
import ninjabrainbot.model.datastate.calibrator.ICalibratorFactory;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.util.I18n;

public class CalibrationDialog extends ThemedDialog {

	final Calibrator calibrator;

	final StyleManager styleManager;
	final NinjabrainBotPreferences preferences;
	final JFrame owner;
	final InstructionLabel[] labels;
	final ErrorTextArea errors;
	final Histogram hist;
	final JPanel rightPanel;
	final JLabel std;
	static final int errorAreaWidth = 100;
	private final IActionExecutor actionExecutor;

	public CalibrationDialog(StyleManager styleManager, NinjabrainBotPreferences preferences, ICalibratorFactory calibratorFactory, IActionExecutor actionExecutor, JFrame owner) {
		super(styleManager, preferences, owner, I18n.get("calibrator.title"));
		this.actionExecutor = actionExecutor;
		styleManager.registerThemedDialog(this);
		this.styleManager = styleManager;
		this.preferences = preferences;
		this.owner = owner;
		actionExecutor.disable();
		calibrator = disposeHandler.add(calibratorFactory.createCalibrator());

		JPanel panel2 = new JPanel();
		panel2.setOpaque(false);
		panel2.setLayout(new BorderLayout());

		JPanel panel = new StackPanel(OptionsFrame.PADDING, panel2);
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING));
		add(panel);

		labels = new InstructionLabel[] { new InstructionLabel(styleManager, I18n.get("calibrator.command_label")), new InstructionLabel(styleManager, I18n.get("calibrator.throw_label")),
				new InstructionLabel(styleManager, I18n.get("calibrator.measure_label")), };
		ThemedLabel explanation = new ThemedLabel(styleManager, "<html><div style='text-align: center;'>" + I18n.get("calibrator.explanation") + "</div></html>") {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		explanation.setAlignmentX(0.5f);
		panel.add(explanation);
		panel.add(new Divider(styleManager));
		for (InstructionLabel l : labels) {
			panel.add(l);
		}
		errors = new ErrorTextArea(styleManager, new JTextArea());
		errors.setPreferredSize(new Dimension(errorAreaWidth, errorAreaWidth));
		rightPanel = new JPanel();
		rightPanel.setOpaque(false);
		rightPanel.setLayout(new GridLayout(3, 1, 0, 0));
		std = new ThemedLabel(styleManager);
		std.setOpaque(false);
		std.setPreferredSize(new Dimension(errorAreaWidth, errorAreaWidth));
		JLabel l1 = new ThemedLabel(styleManager, I18n.get("calibrator.l1"));
		JButton done = new FlatButton(styleManager, I18n.get("calibrator.done"));
		done.addActionListener(p -> done());
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		std.setHorizontalAlignment(SwingConstants.CENTER);
		rightPanel.add(l1);
		rightPanel.add(std);
		rightPanel.add(done);
		hist = new Histogram(styleManager, -0.03f, 0.03f, 11);
		panel2.add(errors, BorderLayout.LINE_START);
		panel2.add(hist, BorderLayout.CENTER);
		panel2.add(rightPanel, BorderLayout.LINE_END);
		startCalibrating();
		disposeHandler.add(calibrator.whenModified().subscribeEDT(this::updateUI));
	}

	public void startCalibrating() {
		std.setText("-");
		setHighlighted(0);
	}

	private void done() {
		if (calibrator.isStrongholdDetermined()) {
			float std = (float) calibrator.getSTD(preferences.mcVersion.get());
			preferences.sigma.set(std);
		}
		dispose();
	}

	private void setHighlighted(int i) {
		for (int j = 0; j < labels.length; j++) {
			labels[j].setHighlighted(i == j);
			labels[j].updateColors();
		}
	}

	private void updateUI() {
		int stage = 0;
		if (calibrator.isReady()) {
			if (calibrator.getNumThrows() > 0) {
				stage = 2;
			} else {
				stage = 1;
			}
		}
		setHighlighted(stage);
		updateHistogram();
	}

	private void updateHistogram() {
		if (calibrator.isStrongholdDetermined()) {
			std.setText(String.format("%.4f", calibrator.getSTD(preferences.mcVersion.get())));
			StringBuilder b = new StringBuilder();
			double[] angleErrors = calibrator.getErrors(preferences.mcVersion.get());
			IReadOnlyList<IEnderEyeThrow> eyeThrows = calibrator.getThrows();
			for (int i = 0; i < angleErrors.length; i++) {
				IEnderEyeThrow t = eyeThrows.get(i);
				double e = angleErrors[i];
				if (Math.abs(t.correction()) > 1e-7) {
					b.append(String.format(I18n.get("angle") + (t.correction() < 0 ? ": %.3f %.3f\n" : ": %.3f +%.3f\n"), t.horizontalAngle() - t.correction(), t.correction()));
				} else {
					b.append(String.format(I18n.get("angle") + ": %.2f\n", t.horizontalAngle()));
				}
				b.append(String.format(I18n.get("error") + ": %.4f\n", e));
			}
			errors.area.setText(b.toString());
			hist.setData(angleErrors);
		} else {
			StringBuilder b = new StringBuilder();
			IReadOnlyList<IEnderEyeThrow> eyeThrows = calibrator.getThrows();
			for (IEnderEyeThrow t : eyeThrows) {
				if (Math.abs(t.correction()) > 1e-7) {
					b.append(String.format(t.correction() < 0 ? "Angle: %.3f %.3f\n" : "Angle: %.3f +%.3f\n", t.horizontalAngle() - t.correction(), t.correction()));
				} else {
					b.append(String.format("Angle: %.2f\n", t.horizontalAngle()));
				}
			}
			errors.area.setText(b.toString());
		}
	}

	@Override
	public void updateBounds(StyleManager styleManager) {
		super.updateBounds(styleManager);
		setSize(styleManager.size.WIDTH * 2, styleManager.size.WIDTH * 3 / 2);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	@Override
	protected void onExitButtonClicked() {
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		actionExecutor.enable();
	}
}

class InstructionLabel extends ThemedLabel {

	private boolean highlighted;

	final WrappedColor highlightedCol;

	public InstructionLabel(StyleManager styleManager, String text) {
		super(styleManager, "<html><div style='text-align: center;'>" + text + "</div></html>");
		setHorizontalAlignment(SwingConstants.CENTER);
		setAlignmentX(0.5f);

		highlightedCol = styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK;
		setForegroundColor(styleManager.currentTheme.TEXT_COLOR_WEAK);
	}

	public void setHighlighted(boolean b) {
		highlighted = b;
	}

	@Override
	public Color getForegroundColor() {
		return highlighted ? highlightedCol.color() : super.getForegroundColor();
	}

}

class ErrorTextArea extends JScrollPane implements ThemedComponent {

	final JTextArea area;

	private WrappedColor bgCol;
	private WrappedColor fgCol;

	public ErrorTextArea(StyleManager styleManager, JTextArea textArea) {
		super(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		area = textArea;
		area.setEditable(false);
		area.setOpaque(false);
		getViewport().setOpaque(false);
		area.setBorder(null);
		setOpaque(false);
		styleManager.registerThemedComponent(this);

		bgCol = styleManager.currentTheme.COLOR_DIVIDER;
		fgCol = styleManager.currentTheme.TEXT_COLOR_NEUTRAL;
		setBorder(BorderFactory.createLineBorder(getBackgroundColor(), 1));
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		area.setFont(styleManager.fontSize(getTextSize(styleManager.size), true));
	}

	@Override
	public void updateColors() {
		area.setForeground(getForegroundColor());
		setBorder(BorderFactory.createLineBorder(getBackgroundColor(), 1));
	}

	public void setBackgroundColor(WrappedColor color) {
		bgCol = color;
	}

	public void setForegroundColor(WrappedColor color) {
		fgCol = color;
	}

	protected Color getBackgroundColor() {
		return bgCol.color();
	}

	protected Color getForegroundColor() {
		return fgCol.color();
	}

	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_TINY;
	}

}
