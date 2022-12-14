package ninjabrain.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ninjabrainbot.data.DataStateHandler;
import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ResultType;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.util.TestUtils;

public class DataStateHandlerTests {

	NinjabrainBotPreferences preferences;

	@BeforeEach
	void setup() {
		preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
	}

	@Test
	void dataStateResultUpdatesCorrectly() {
		ObservableProperty<IThrow> throwStream = new ObservableProperty<IThrow>();
		ObservableProperty<Fossil> fossilStream = new ObservableProperty<Fossil>();

		DataStateHandler dataStateHandler = new DataStateHandler(preferences);
		dataStateHandler.addThrowStream(throwStream);
		dataStateHandler.addFossilStream(fossilStream);
		IDataState dataState = dataStateHandler.getDataState();

		assertEquals(dataState.resultType().get(), ResultType.NONE);

		fossilStream.notifySubscribers(new Fossil(1));
		assertEquals(dataState.resultType().get(), ResultType.DIVINE);
		assertEquals(dataState.divineResult().get().fossil.x, 1);

		throwStream.notifySubscribers(TestUtils.createThrowNether(213, 142, -45));
		assertEquals(dataState.resultType().get(), ResultType.BLIND);
		assertEquals(dataState.blindResult().get().evaluation(), BlindResult.EXCELLENT);
		double highrollProbaility = dataState.blindResult().get().highrollProbability;
		assertTrue(highrollProbaility > 0.4);

		throwStream.notifySubscribers(TestUtils.createThrow(2000, 1000, -45));
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		double distanceAfterFirstThrow = dataState.calculatorResult().get().getBestPrediction().getDistance();

		throwStream.notifySubscribers(TestUtils.createThrowLookDown(2100, 1100, -45));
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		double distanceAfterSecondThrow = dataState.calculatorResult().get().getBestPrediction().getDistance();
		assertTrue(distanceAfterSecondThrow < distanceAfterFirstThrow);

		throwStream.notifySubscribers(TestUtils.createThrow(2000, 1000, 45));
		assertEquals(dataState.resultType().get(), ResultType.FAILED);
		assertFalse(dataState.calculatorResult().get().success());

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		assertEquals(dataState.calculatorResult().get().getBestPrediction().getDistance(), distanceAfterSecondThrow);

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.TRIANGULATION);
		assertTrue(dataState.calculatorResult().get().success());
		assertEquals(dataState.calculatorResult().get().getBestPrediction().getDistance(), distanceAfterFirstThrow);

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.BLIND);
		assertEquals(dataState.blindResult().get().evaluation(), BlindResult.EXCELLENT);
		assertEquals(dataState.blindResult().get().highrollProbability, highrollProbaility);

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.DIVINE);
		assertEquals(dataState.divineResult().get().fossil.x, 1);

		dataStateHandler.undo();
		assertEquals(dataState.resultType().get(), ResultType.NONE);
	}

}
