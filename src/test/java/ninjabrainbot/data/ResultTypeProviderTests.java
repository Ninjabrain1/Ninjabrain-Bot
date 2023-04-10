package ninjabrainbot.data;

import ninjabrainbot.data.actions.ActionExecutor;
import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.calculator.CalculatorSettings;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.common.IDetailedPlayerPosition;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.EnderEyeThrowFactory;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.data.calculator.endereye.IStandardDeviationHandler;
import ninjabrainbot.data.calculator.endereye.StandardDeviationHandler;
import ninjabrainbot.data.domainmodel.DomainModel;
import ninjabrainbot.data.input.FossilInputHandler;
import ninjabrainbot.data.input.PlayerPositionInputHandler;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.util.FakeCoordinateInputSource;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultTypeProviderTests {

	NinjabrainBotPreferences preferences;
	DomainModel domainModel;
	ActionExecutor actionExecutor;
	IStandardDeviationHandler standardDeviationHandler;

	@BeforeEach
	void setup() {
		preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		domainModel = new DomainModel();
		actionExecutor = new ActionExecutor(domainModel);
		standardDeviationHandler = new StandardDeviationHandler(preferences);
	}

	@Test
	void resultTypeUpdatesCorrectly() {
		CalculatorSettings calculatorSettings = new CalculatorSettings(preferences);
		IDataState dataState = new DataState(new Calculator(calculatorSettings), domainModel);

		FakeCoordinateInputSource coordinateInputSource = new FakeCoordinateInputSource();
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState(), standardDeviationHandler);
		new PlayerPositionInputHandler(coordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory);
		new FossilInputHandler(coordinateInputSource, dataState, actionExecutor);

		ObservableProperty<IDetailedPlayerPosition> playerPositionStream = coordinateInputSource.whenNewDetailedPlayerPositionInputted;
		ObservableProperty<Fossil> fossilStream = coordinateInputSource.whenNewFossilInputted;

		assertEquals(dataState.resultType().get(), ResultType.NONE);

		fossilStream.notifySubscribers(new Fossil(1));
		assertEquals(dataState.resultType().get(), ResultType.DIVINE);
		assertEquals(dataState.divineResult().get().fossil.x, 1);

		playerPositionStream.notifySubscribers(TestUtils.createPlayerPositionInNether(213, 142, -45));
		assertEquals(dataState.resultType().get(), ResultType.BLIND);
		assertEquals(dataState.blindResult().get().evaluation(), BlindResult.EXCELLENT);
		double highrollProbability = dataState.blindResult().get().highrollProbability;
		assertTrue(highrollProbability > 0.4);

		playerPositionStream.notifySubscribers(TestUtils.createPlayerPosition(2000, 1000, -45));
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		double distanceAfterFirstThrow = dataState.calculatorResult().get().getBestPrediction().getOverworldDistance();

		playerPositionStream.notifySubscribers(TestUtils.createPlayerPositionLookDown(2100, 1100, -45));
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		double distanceAfterSecondThrow = dataState.calculatorResult().get().getBestPrediction().getOverworldDistance();
		assertTrue(distanceAfterSecondThrow < distanceAfterFirstThrow);

		playerPositionStream.notifySubscribers(TestUtils.createPlayerPosition(2000, 1000, 45));
		assertEquals(dataState.resultType().get(), ResultType.FAILED);
		assertFalse(dataState.calculatorResult().get().success());

//		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		assertEquals(dataState.calculatorResult().get().getBestPrediction().getOverworldDistance(), distanceAfterSecondThrow);

//		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		assertEquals(dataState.calculatorResult().get().getBestPrediction().getOverworldDistance(), distanceAfterFirstThrow);

//		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.BLIND);
		assertEquals(dataState.blindResult().get().evaluation(), BlindResult.EXCELLENT);
		assertEquals(dataState.blindResult().get().highrollProbability, highrollProbability);

//		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.DIVINE);
		assertEquals(dataState.divineResult().get().fossil.x, 1);

//		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.NONE);
	}

}
