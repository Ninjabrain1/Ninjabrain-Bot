package ninjabrainbot.gui.mainwindow.main;

import java.awt.CardLayout;
import java.awt.Dimension;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.ResultType;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.gui.components.panels.ResizablePanel;
import ninjabrainbot.gui.mainwindow.alladvancements.AllAdvancementsPanel;
import ninjabrainbot.gui.mainwindow.triangulation.BasicTriangulationPanel;
import ninjabrainbot.gui.mainwindow.triangulation.DetailedTriangulationPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.MainViewType;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class MainTextArea extends ResizablePanel {

	private final String BLIND = "BLIND", DIVINE = "DIVINE", TRIANGULATION = "TRI", TRIANGULATION_DETAILED = "DET", ALL_ADVANCEMENTS = "AA";

	private NinjabrainBotPreferences preferences;

	IDataState dataState;

	BasicTriangulationPanel basicTriangulation;
	DetailedTriangulationPanel detailedTriangulation;
	BlindPanel blind;
	DivinePanel divine;
	AllAdvancementsPanel allAdvancements;

	boolean idle;
	CardLayout layout;

	public MainTextArea(StyleManager styleManager, NinjabrainBotPreferences preferences, IDataState dataState) {
		this.preferences = preferences;
		this.dataState = dataState;
		layout = new CardLayout();
		idle = true;
		setLayout(layout);
		setAlignmentX(0);
		basicTriangulation = new BasicTriangulationPanel(styleManager, preferences);
		detailedTriangulation = new DetailedTriangulationPanel(styleManager, preferences);
		blind = new BlindPanel(styleManager);
		divine = new DivinePanel(styleManager);
		allAdvancements = new AllAdvancementsPanel(styleManager, dataState.allAdvancementDataState());
		add(basicTriangulation, TRIANGULATION);
		add(detailedTriangulation, TRIANGULATION_DETAILED);
		add(blind, BLIND);
		add(divine, DIVINE);
		add(allAdvancements, ALL_ADVANCEMENTS);
		setOpaque(false);
		layout.show(this, preferences.view.get() == MainViewType.BASIC ? TRIANGULATION : TRIANGULATION_DETAILED);
		setupSubscriptions();

		setResult(dataState.calculatorResult().get());
		setResult(dataState.blindResult().get());
		setResult(dataState.divineResult().get());
		updateResult();
	}

	private void setupSubscriptions() {
		// Settings
		disposeHandler.add(preferences.showNetherCoords.whenModified().subscribe(this::setNetherCoordsEnabled));
		disposeHandler.add(preferences.showAngleUpdates.whenModified().subscribe(this::setAngleUpdatesEnabled));
		disposeHandler.add(preferences.view.whenModified().subscribe(__ -> onViewTypeChanged()));
		// Data state
		disposeHandler.add(dataState.calculatorResult().subscribeEDT(this::setResult));
		disposeHandler.add(dataState.blindResult().subscribeEDT(this::setResult));
		disposeHandler.add(dataState.divineResult().subscribeEDT(this::setResult));
		disposeHandler.add(dataState.resultType().subscribeEDT(__ -> updateResult()));
	}

	private void onViewTypeChanged() {
		ICalculatorResult result = dataState.calculatorResult().get();
		if (preferences.view.get() == MainViewType.BASIC || (result != null && !result.success())) {
			basicTriangulation.setResult(result);
			basicTriangulation.updateColors();
		} else {
			detailedTriangulation.setResult(result);
			detailedTriangulation.updateColors();
		}
		updateResult();
		revalidate();
		whenSizeModified.notifySubscribers(this);
	}

	private void updateResult() {
		ResultType resultType = dataState.resultType().get();
		idle = false;
		switch (resultType) {
			case NONE:
				layout.show(this, preferences.view.get() == MainViewType.BASIC ? TRIANGULATION : TRIANGULATION_DETAILED);
				idle = true;
				break;
			case FAILED:
				layout.show(this, TRIANGULATION);
				break;
			case TRIANGULATION:
				layout.show(this, preferences.view.get() == MainViewType.BASIC ? TRIANGULATION : TRIANGULATION_DETAILED);
				break;
			case BLIND:
				layout.show(this, BLIND);
				break;
			case DIVINE:
				layout.show(this, DIVINE);
				break;
			case ALL_ADVANCEMENTS:
				layout.show(this, ALL_ADVANCEMENTS);
		}
	}

	private void setResult(ICalculatorResult result) {
		if (preferences.view.get() == MainViewType.BASIC || (result != null && !result.success())) {
			basicTriangulation.setResult(result);
			basicTriangulation.updateColors();
		} else {
			detailedTriangulation.setResult(result);
		}
	}

	private void setResult(BlindResult result) {
		blind.setResult(result);
		blind.updateColors();
	}

	private void setResult(DivineResult result) {
		divine.setResult(result);
		divine.updateColors();
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
		if (preferences.view.get() == MainViewType.BASIC) {
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
