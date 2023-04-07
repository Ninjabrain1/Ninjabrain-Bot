package ninjabrainbot.data;

import ninjabrainbot.data.actions.ActionExecutor;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.input.FossilInputHandler;
import ninjabrainbot.data.input.ThrowInputHandler;
import ninjabrainbot.data.temp.DomainModel;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.util.FakeActiveInstanceProvider;
import ninjabrainbot.util.FakeClipboardProvider;
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

	@BeforeEach
	void setup() {
		preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		domainModel = new DomainModel();
		actionExecutor = new ActionExecutor(domainModel);
	}

	@Test
	void resultTypeUpdatesCorrectly() {
		ObservableProperty<IThrow> throwStream = new ObservableProperty<>();
		ObservableProperty<Fossil> fossilStream = new ObservableProperty<>();

		DataStateHandler dataStateHandler = new DataStateHandler(preferences, new FakeClipboardProvider(), new FakeActiveInstanceProvider());

		IDataState dataState = dataStateHandler.getDataState();
		ThrowInputHandler throwInputHandler = new ThrowInputHandler(throwStream, dataState, actionExecutor, preferences);
		FossilInputHandler fossilInputHandler = new FossilInputHandler(fossilStream, dataState, actionExecutor);

		assertEquals(dataState.resultType().get(), ResultType.NONE);

		fossilStream.notifySubscribers(new Fossil(1));
		assertEquals(dataState.resultType().get(), ResultType.DIVINE);
		assertEquals(dataState.divineResult().get().fossil.x, 1);

		throwStream.notifySubscribers(TestUtils.createThrowNether(213, 142, -45));
		assertEquals(dataState.resultType().get(), ResultType.BLIND);
		assertEquals(dataState.blindResult().get().evaluation(), BlindResult.EXCELLENT);
		double highrollProbability = dataState.blindResult().get().highrollProbability;
		assertTrue(highrollProbability > 0.4);

		throwStream.notifySubscribers(TestUtils.createThrow(2000, 1000, -45));
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		double distanceAfterFirstThrow = dataState.calculatorResult().get().getBestPrediction().getOverworldDistance();

		throwStream.notifySubscribers(TestUtils.createThrowLookDown(2100, 1100, -45));
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		double distanceAfterSecondThrow = dataState.calculatorResult().get().getBestPrediction().getOverworldDistance();
		assertTrue(distanceAfterSecondThrow < distanceAfterFirstThrow);

		throwStream.notifySubscribers(TestUtils.createThrow(2000, 1000, 45));
		assertEquals(dataState.resultType().get(), ResultType.FAILED);
		assertFalse(dataState.calculatorResult().get().success());

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		assertEquals(dataState.calculatorResult().get().getBestPrediction().getOverworldDistance(), distanceAfterSecondThrow);

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		assertEquals(dataState.calculatorResult().get().getBestPrediction().getOverworldDistance(), distanceAfterFirstThrow);

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.BLIND);
		assertEquals(dataState.blindResult().get().evaluation(), BlindResult.EXCELLENT);
		assertEquals(dataState.blindResult().get().highrollProbability, highrollProbability);

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.DIVINE);
		assertEquals(dataState.divineResult().get().fossil.x, 1);

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.NONE);
	}

}
