package ninjabrainbot.model.information;

import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IOverworldPosition;
import ninjabrainbot.model.datastate.common.OverworldPosition;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.environmentstate.IEnvironmentState;
import ninjabrainbot.model.environmentstate.StandardDeviationSettings;
import ninjabrainbot.util.Coords;
import ninjabrainbot.util.I18n;

public class NextThrowDirectionInformationProvider extends InformationMessageProvider {

	private final IDataState dataState;
	private final IObservable<StandardDeviationSettings> standardDeviationSettings;

	public NextThrowDirectionInformationProvider(IDataState dataState, IEnvironmentState environmentState, NinjabrainBotPreferences preferences) {
		super(preferences.informationDirectionHelpEnabled);
		this.dataState = dataState;
		this.standardDeviationSettings = environmentState.standardDeviationSettings();
		disposeHandler.add(dataState.calculatorResult().subscribe(this::raiseInformationMessageChanged));
		disposeHandler.add(preferences.sigma.whenModified().subscribe(this::raiseInformationMessageChanged));
		disposeHandler.add(preferences.sigmaManual.whenModified().subscribe(this::raiseInformationMessageChanged));
		disposeHandler.add(dataState.resultType().subscribe(this::raiseInformationMessageChanged));
		disposeHandler.add(standardDeviationSettings.subscribe(this::raiseInformationMessageChanged));
	}

	@Override
	protected boolean shouldShowInformationMessage() {
		if (dataState.resultType().get() != ResultType.TRIANGULATION)
			return false;

		ICalculatorResult calculatorResult = dataState.calculatorResult().get();
		if (calculatorResult == null || !calculatorResult.success() || dataState.getThrowList().size() == 0)
			return false;

		double bestCertainty = calculatorResult.getBestPrediction().chunk.weight;
		return 0.05 < bestCertainty && bestCertainty < 0.95;
	}

	@Override
	protected InformationMessage getInformationMessage() {
		IEnderEyeThrow lastThrow = dataState.getThrowList().getLast();
		List<Chunk> predictions = new ArrayList<>();
		double cumulativeProbability = 0;
		for (Chunk predictedChunk : dataState.calculatorResult().get().getTopChunks()) {
			if (cumulativeProbability > 0.99)
				break;
			cumulativeProbability += predictedChunk.weight;
			predictions.add(predictedChunk);
		}
		double phiRight = (lastThrow.horizontalAngle() + 90.0) * Math.PI / 180.0;
		double phiLeft = (lastThrow.horizontalAngle() - 90.0) * Math.PI / 180.0;
		int rightDistance = (int) Math.ceil(binarySearchSidewaysDistanceFor99PercentLowestPossibleCertainty(predictions, lastThrow, phiRight));
		int leftDistance = (int) Math.ceil(binarySearchSidewaysDistanceFor99PercentLowestPossibleCertainty(predictions, lastThrow, phiLeft));
		return new InformationMessage(InformationType.Info, I18n.get("information.go_left_x_block_or_right_y_blocks", leftDistance, rightDistance));
	}

	private double binarySearchSidewaysDistanceFor99PercentLowestPossibleCertainty(List<Chunk> predictions, IEnderEyeThrow lastThrow, double phiSideways) {
		double expectedTopCertainty = 0;
		double sidewaysDistance = 0;
		double sidewaysDistanceIncrement = 5.0;
		boolean binarySearching = false;
		while (sidewaysDistanceIncrement > 0.1) {
			sidewaysDistance += sidewaysDistanceIncrement * (expectedTopCertainty > 0.95 ? -1.0 : 1.0);
			double newX = lastThrow.xInOverworld() + Coords.getX(sidewaysDistance, phiSideways);
			double newZ = lastThrow.zInOverworld() + Coords.getZ(sidewaysDistance, phiSideways);
			expectedTopCertainty = getExpectedTopCertainty(predictions, new OverworldPosition(newX, newZ), lastThrow.getExpectedStandardDeviationForNextEnderEyeThrow(standardDeviationSettings.get()));
			if (expectedTopCertainty > 0.95)
				binarySearching = true;
			if (binarySearching)
				sidewaysDistanceIncrement *= 0.5;
		}
		return sidewaysDistance;
	}

	private double getExpectedTopCertainty(List<Chunk> predictions, IOverworldPosition lastThrow, double standardDeviation) {
		double expectedCertaintyAfterThrow = 0;
		double totalOriginalCertainty = 0;
		for (Chunk assumedStrongholdChunk : predictions) {
			double phiToStronghold = Coords.getPhi(assumedStrongholdChunk.eightEightX() - lastThrow.xInOverworld(), assumedStrongholdChunk.eightEightZ() - lastThrow.zInOverworld());
			double certaintyThatPredictionHitsStronghold = 0;
			double totalCertaintyAfterSecondThrow = 0;
			for (Chunk otherChunk : predictions) {
				if (otherChunk == assumedStrongholdChunk) {
					// 0.9 = approximate expected likelihood (if this is not included the
					// calculation assumes 0 measurement error for the chunk that is assumed to
					// contain the stronghold, but that is not realistic). A bit hacky but there's
					// no need to be super precise in this calculation.
					totalCertaintyAfterSecondThrow += assumedStrongholdChunk.weight * 0.9;
					certaintyThatPredictionHitsStronghold += assumedStrongholdChunk.weight * 0.9;
					continue;
				}
				double phiToPrediction = Coords.getPhi(otherChunk.eightEightX() - lastThrow.xInOverworld(), otherChunk.eightEightZ() - lastThrow.zInOverworld());
				double errorLikelihood = measurementErrorPdf(phiToPrediction - phiToStronghold, standardDeviation);
				totalCertaintyAfterSecondThrow += otherChunk.weight * errorLikelihood;
				if (assumedStrongholdChunk.isNeighboring(otherChunk)) {
					certaintyThatPredictionHitsStronghold += otherChunk.weight * errorLikelihood;
				}
			}
			double newCertaintyForRealStronghold = certaintyThatPredictionHitsStronghold / totalCertaintyAfterSecondThrow;
			expectedCertaintyAfterThrow += newCertaintyForRealStronghold * assumedStrongholdChunk.weight;
			totalOriginalCertainty += assumedStrongholdChunk.weight;
		}
		return expectedCertaintyAfterThrow / totalOriginalCertainty;
	}

	private double measurementErrorPdf(double errorInRadians, double sigma) {
		double error = errorInRadians * 180.0 / Math.PI;
		return Math.exp(-error * error / (2 * sigma * sigma));
	}

}
