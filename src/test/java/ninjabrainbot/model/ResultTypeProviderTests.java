package ninjabrainbot.model;

import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.model.actions.ActionExecutor;
import ninjabrainbot.model.datastate.DataState;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.F3IData;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.model.domainmodel.DomainModel;
import ninjabrainbot.model.environmentstate.EnvironmentState;
import ninjabrainbot.model.input.F3ILocationInputHandler;
import ninjabrainbot.model.input.PlayerPositionInputHandler;
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
	IDataState dataState;

	@BeforeEach
	void setup() {
		preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		domainModel = new DomainModel();
		actionExecutor = new ActionExecutor(domainModel);
		dataState = new DataState(domainModel, new EnvironmentState(domainModel, preferences));
		domainModel.finishInitialization();
	}

	@Test
	void resultTypeUpdatesCorrectly() {
		FakeCoordinateInputSource coordinateInputSource = new FakeCoordinateInputSource();
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState());
		new PlayerPositionInputHandler(coordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory);
		new F3ILocationInputHandler(coordinateInputSource, dataState, actionExecutor, preferences);

		ObservableProperty<IDetailedPlayerPosition> playerPositionStream = coordinateInputSource.whenNewDetailedPlayerPositionInputted;
		ObservableProperty<F3IData> f3iStream = coordinateInputSource.whenNewF3IInputted;

		assertEquals(dataState.resultType().get(), ResultType.NONE);

		f3iStream.notifySubscribers(new F3IData(1, 0, 0));
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

		domainModel.undoUnderWriteLock();
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		assertEquals(dataState.calculatorResult().get().getBestPrediction().getOverworldDistance(), distanceAfterSecondThrow);

		domainModel.undoUnderWriteLock();
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		assertEquals(dataState.calculatorResult().get().getBestPrediction().getOverworldDistance(), distanceAfterFirstThrow);

		domainModel.undoUnderWriteLock();
		assertEquals(dataState.resultType().get(), ResultType.BLIND);
		assertEquals(dataState.blindResult().get().evaluation(), BlindResult.EXCELLENT);
		assertEquals(dataState.blindResult().get().highrollProbability, highrollProbability);

		domainModel.undoUnderWriteLock();
		assertEquals(dataState.resultType().get(), ResultType.DIVINE);
		assertEquals(dataState.divineResult().get().fossil.x, 1);

		domainModel.undoUnderWriteLock();
		assertEquals(dataState.resultType().get(), ResultType.NONE);
	}

}
