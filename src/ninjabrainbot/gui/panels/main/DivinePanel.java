package ninjabrainbot.gui.panels.main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.data.stronghold.Ring;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.ColumnLayout;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.util.I18n;

class DivinePanel extends ThemedPanel {

	private static final long serialVersionUID = 8846911396318732368L;

	private JPanel panels[];
	public JLabel fossilLabel;
	public JLabel safeLabels[];
	public JLabel highrollLabels[];

	public DivinePanel(StyleManager styleManager) {
		super(styleManager);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(0);
		int n = Ring.get(0).numStrongholds;
		JPanel panel0 = new ThemedPanel(styleManager);
		ColumnLayout layout0 = new ColumnLayout(0);
		panels = new JPanel[3];
		panel0.setLayout(layout0);
		panel0.setOpaque(false);
		panels[0] = panel0;
		fossilLabel = new ThemedLabel(styleManager, "");
		fossilLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		layout0.setRelativeWidth(fossilLabel, 0.9f);
		panel0.add(fossilLabel, 0);
		for (int i = 0; i < n; i++) {
			panel0.add(new ThemedLabel(styleManager, "s" + (i + 1), true, true));
		}
		JPanel panel1 = new ThemedPanel(styleManager);
		ColumnLayout layout1 = new ColumnLayout(0);
		panel1.setLayout(layout1);
		panel1.setOpaque(false);
		panels[1] = panel1;
		JLabel safeLabel = new ThemedLabel(styleManager, I18n.get("divine_safe"));
		safeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		layout1.setRelativeWidth(safeLabel, 0.9f);
		panel1.add(safeLabel);
		safeLabels = new ThemedLabel[n];
		for (int i = 0; i < n; i++) {
			JLabel s = new ThemedLabel(styleManager, "", false, true);
			safeLabels[i] = s;
			panel1.add(s);
		}
		JPanel panel2 = new ThemedPanel(styleManager);
		ColumnLayout layout2 = new ColumnLayout(0);
		panel2.setLayout(layout2);
		panel2.setOpaque(false);
		panels[2] = panel2;
		JLabel highrollLabel = new ThemedLabel(styleManager, I18n.get("divine_highroll"));
		highrollLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		layout2.setRelativeWidth(highrollLabel, 0.9f);
		panel2.add(highrollLabel);
		highrollLabels = new ThemedLabel[n];
		for (int i = 0; i < n; i++) {
			JLabel h = new ThemedLabel(styleManager, "", false, true);
			highrollLabels[i] = h;
			panel2.add(h);
		}
		add(panel0);
		add(panel1);
		add(panel2);
		add(Box.createGlue());
	}

	public void setResult(DivineResult result) {
		if (result == null)
			return;
		fossilLabel.setText(I18n.get("fossil_number", result.fossil.x));
		for (int i = 0; i < Ring.get(0).numStrongholds; i++) {
			safeLabels[i].setText(result.safe[i].toString());
			highrollLabels[i].setText(result.highroll[i].toString());
		}
	}

	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_NEUTRAL;
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setPreferredSize(new Dimension(0, 3 * (styleManager.size.PADDING + styleManager.size.TEXT_SIZE_MEDIUM) + 2 * styleManager.size.PADDING_THIN));
		setBorder(new EmptyBorder(styleManager.size.PADDING_THIN, styleManager.size.PADDING, styleManager.size.PADDING_THIN, styleManager.size.PADDING));
		for (JPanel p : panels) {
			p.setMaximumSize(new Dimension(1000, styleManager.size.TEXT_SIZE_MEDIUM));
		}
		super.updateSize(styleManager);
	}

}