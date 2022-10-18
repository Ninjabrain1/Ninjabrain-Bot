package ninjabrainbot.gui.panels.main;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;

import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;

class DetailedTriangulationPanel extends ThemedPanel implements IDisposable {

	private static final long serialVersionUID = -9022636765337872342L;

	private static final int NUM_DETAILED_PANELS = 5;

	private ChunkPanelHeader header;
	private List<ChunkPanel> panels;

	public DetailedTriangulationPanel(StyleManager styleManager) {
		super(styleManager);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(0);
		header = new ChunkPanelHeader(styleManager);
		add(header);
		panels = new ArrayList<ChunkPanel>();
		for (int i = 0; i < NUM_DETAILED_PANELS; i++) {
			ChunkPanel panel = new ChunkPanel(styleManager);
			panels.add(panel);
			add(panel);
		}
	}

	public void setResult(ICalculatorResult result) {
		header.updateHeaderText();
		if (result == null) {
			for (ChunkPanel p : panels) {
				p.setPrediction(null);
			}
			return;
		}
		List<ChunkPrediction> predictions = result.getTopPredictions();
		for (int i = 0; i < NUM_DETAILED_PANELS; i++) {
			ChunkPanel p = panels.get(i);
			p.setPrediction(predictions.get(i));
		}
	}

	public void setAngleUpdatesEnabled(boolean b) {
		header.setAngleUpdatesEnabled(b);
		panels.forEach(p -> p.setAngleUpdatesEnabled(b));
	}

	@Override
	public void updateColors(StyleManager styleManager) {
		super.updateColors(styleManager);
		for (int i = 0; i < NUM_DETAILED_PANELS; i++) {
			ChunkPanel p = panels.get(i);
			p.updateColors(styleManager);
		}
	}

	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_NEUTRAL;
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setPreferredSize(new Dimension(0, (1 + NUM_DETAILED_PANELS) * (styleManager.size.PADDING + styleManager.size.TEXT_SIZE_MEDIUM)));
		super.updateSize(styleManager);
	}

	@Override
	public void dispose() {
		header.dispose();
	}

}