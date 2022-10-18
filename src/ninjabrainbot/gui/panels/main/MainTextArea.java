package ninjabrainbot.gui.panels.main;

import java.awt.CardLayout;
import java.awt.Dimension;

import ninjabrainbot.Main;
import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.ResultType;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.gui.panels.ResizablePanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class MainTextArea extends ResizablePanel {

	private static final long serialVersionUID = 5680882946230890993L;

	private final String BLIND = "BLIND", DIVINE = "DIVINE", TRIANGULATION = "TRI", TRIANGULATION_DETAILED = "DET";

	IDataState dataState;

	StyleManager styleManager;

	BasicTriangulationPanel basicTriangulation;
	DetailedTriangulationPanel detailedTriangulation;
	BlindPanel blind;
	DivinePanel divine;

	boolean idle;
	CardLayout layout;

	public MainTextArea(StyleManager styleManager, IDataState dataState) {
		this.styleManager = styleManager;
		this.dataState = dataState;
		layout = new CardLayout();
		idle = true;
		setLayout(layout);
		setAlignmentX(0);
		basicTriangulation = new BasicTriangulationPanel(styleManager);
		detailedTriangulation = new DetailedTriangulationPanel(styleManager);
		blind = new BlindPanel(styleManager);
		divine = new DivinePanel(styleManager);
		add(basicTriangulation, TRIANGULATION);
		add(detailedTriangulation, TRIANGULATION_DETAILED);
		add(blind, BLIND);
		add(divine, DIVINE);
		setOpaque(false);
		layout.show(this, Main.preferences.view.get() == NinjabrainBotPreferences.BASIC ? TRIANGULATION : TRIANGULATION_DETAILED);
		setupSubscriptions();

		setResult(dataState.calculatorResult().get());
		setResult(dataState.blindResult().get());
		setResult(dataState.divineResult().get());
		updateResult();
	}

	private void setupSubscriptions() {
		// Settings
		sh.add(Main.preferences.showNetherCoords.whenModified().subscribe(b -> setNetherCoordsEnabled(b)));
		sh.add(Main.preferences.showAngleUpdates.whenModified().subscribe(b -> setAngleUpdatesEnabled(b)));
		sh.add(Main.preferences.view.whenModified().subscribe(__ -> onViewTypeChanged()));
		// Data state
		sh.add(dataState.calculatorResult().subscribeEDT(result -> setResult(result)));
		sh.add(dataState.blindResult().subscribeEDT(result -> setResult(result)));
		sh.add(dataState.divineResult().subscribeEDT(result -> setResult(result)));
		sh.add(dataState.resultType().subscribeEDT(__ -> updateResult()));
	}

	private void onViewTypeChanged() {
		ICalculatorResult result = dataState.calculatorResult().get();
		if (Main.preferences.view.get() == NinjabrainBotPreferences.BASIC || (result != null && !result.success())) {
			basicTriangulation.setResult(result);
			basicTriangulation.updateColors(styleManager);
		} else {
			detailedTriangulation.setResult(result);
			detailedTriangulation.updateColors(styleManager);
		}
		updateResult();
		revalidate();
		whenSizeModified.notifySubscribers(this);
	}

	private void updateResult() {
		ResultType rt = dataState.resultType().get();
		idle = false;
		switch (rt) {
		case NONE:
			layout.show(this, Main.preferences.view.get() == NinjabrainBotPreferences.BASIC ? TRIANGULATION : TRIANGULATION_DETAILED);
			idle = true;
			break;
		case FAILED:
			layout.show(this, TRIANGULATION);
			break;
		case TRIANGULATION:
			layout.show(this, Main.preferences.view.get() == NinjabrainBotPreferences.BASIC ? TRIANGULATION : TRIANGULATION_DETAILED);
			break;
		case BLIND:
			layout.show(this, BLIND);
			break;
		case DIVINE:
			layout.show(this, DIVINE);
			break;
		}
	}

	private void setResult(ICalculatorResult result) {
		if (Main.preferences.view.get() == NinjabrainBotPreferences.BASIC || (result != null && !result.success())) {
			basicTriangulation.setResult(result);
		} else {
			detailedTriangulation.setResult(result);
			detailedTriangulation.updateColors(styleManager);
		}
	}

	private void setResult(BlindResult result) {
		blind.setResult(result);
		blind.updateColors(styleManager);
	}

	private void setResult(DivineResult result) {
		divine.setResult(result);
		divine.updateColors(styleManager);
	}

	private void setNetherCoordsEnabled(boolean b) {
		basicTriangulation.netherLabel.setVisible(b);
	}

	private void setAngleUpdatesEnabled(boolean b) {
		basicTriangulation.setAngleUpdatesEnabled(b);
		detailedTriangulation.setAngleUpdatesEnabled(b);
		whenSizeModified.notifySubscribers(this);
	}

	@Override
	public Dimension getPreferredSize() {
		if (Main.preferences.view.get() == NinjabrainBotPreferences.BASIC) {
			return basicTriangulation.getPreferredSize();
		} else {
			return detailedTriangulation.getPreferredSize();
		}
	}

	public boolean isIdle() {
		return idle;
	}

	@Override
	public void dispose() {
		super.dispose();
		detailedTriangulation.dispose();
		basicTriangulation.dispose();
	}

}
