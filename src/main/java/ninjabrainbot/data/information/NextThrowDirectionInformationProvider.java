package ninjabrainbot.data.information;

import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.ResultType;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.common.IOverworldPosition;
import ninjabrainbot.data.calculator.common.OverworldPosition;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.stronghold.Chunk;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.Coords;
import ninjabrainbot.util.I18n;

public class NextThrowDirectionInformationProvider extends InformationMessageProvider {

	private final IDataState dataState;

	public NextThrowDirectionInformationProvider(IDataState dataState, NinjabrainBotPreferences preferences) {
		super(preferences.informationDirectionHelpEnabled);
		this.dataState = dataState;
		disposeHandler.add(dataState.calculatorResult().subscribe(this::raiseInformationMessageChanged));
		disposeHandler.add(preferences.sigma.whenModified().subscribe(this::raiseInformationMessageChanged));
		disposeHandler.add(preferences.sigmaAlt.whenModified().subscribe(this::raiseInformationMessageChanged));
		disposeHandler.add(dataState.resultType().subscribe(this::raiseInformationMessageChanged));
	}

	@Override
	protected boolean shouldShowInformationMessage() {
		if (dataState.resultType().get() != ResultType.TRIANGULATION)
			return false;

		ICalculatorResult calculatorResult = dataState.calculatorResult().get();
		if (calculatorResult == null || !calculatorResult.success() || dataState.getThrowSet().size() == 0)
			return false;
		return calculatorResult.getBestPrediction().chunk.weight < 0.95;
	}

	@Override
	protected InformationMessage getInformationMessage() {
		IThrow lastThrow = dataState.getThrowSet().getLast();
		List<Chunk> predictions = new ArrayList<>();
		for (Chunk predictedChunk : dataState.calculatorResult().get().getTopChunks()) {
			if (predictedChunk.weight < 0.01)
				break;
			predictions.add(predictedChunk);
		}
		double phiRight = (lastThrow.alpha() + 90.0) * Math.PI / 180.0;
		double phiLeft = (lastThrow.alpha() - 90.0) * Math.PI / 180.0;
		int rightDistance = (int) Math.ceil(binarySearchSidewaysDistanceFor99PercentLowestPossibleCertainty(predictions, lastThrow, phiRight));
		int leftDistance = (int) Math.ceil(binarySearchSidewaysDistanceFor99PercentLowestPossibleCertainty(predictions, lastThrow, phiLeft));
		return new InformationMessage(InformationType.Info, I18n.get("information.go_left_x_block_or_right_y_blocks", leftDistance, rightDistance));
	}

	private double binarySearchSidewaysDistanceFor99PercentLowestPossibleCertainty(List<Chunk> predictions, IThrow lastThrow, double phiSideways) {
		double lowestPossibleCertainty = 0;
		double sidewaysDistance = 0;
		double sidewaysDistanceIncrement = 5.0;
		boolean binarySearching = false;
		while (sidewaysDistanceIncrement > 0.1) {
			sidewaysDistance += sidewaysDistanceIncrement * (lowestPossibleCertainty > 0.99 ? -1.0 : 1.0);
			double newX = lastThrow.xInOverworld() + Coords.getX(sidewaysDistance, phiSideways);
			double newZ = lastThrow.zInOverworld() + Coords.getZ(sidewaysDistance, phiSideways);
			lowestPossibleCertainty = getLowestPossibleCertainty(predictions, new OverworldPosition(newX, newZ), lastThrow.getStd());
			if (lowestPossibleCertainty > 0.99)
				binarySearching = true;
			if (binarySearching)
				sidewaysDistanceIncrement *= 0.5;
		}
		return sidewaysDistance;
	}

	private double getLowestPossibleCertainty(List<Chunk> predictions, IOverworldPosition lastThrow, double standardDeviation) {
		double lowestPossibleCertainty = 1.0;
		for (Chunk assumedStrongholdChunk : predictions) {
			double phiToStronghold = Coords.getPhi(assumedStrongholdChunk.eighteightX() - lastThrow.xInOverworld(), assumedStrongholdChunk.eighteightZ() - lastThrow.zInOverworld());
			double originalCertainty = assumedStrongholdChunk.weight;
			double totalCertaintyAfterSecondThrow = 0;
			for (Chunk otherChunk : predictions) {
				if (otherChunk == assumedStrongholdChunk) {
					// 0.9 = approximate expected likelihood (if this is not included the
					// calculation assumes 0 measurement error for the chunk that is assumed to
					// contain the stronghold, but that is not realistic). A bit hacky but there's
					// no need to be super precise in this calculation.
					originalCertainty *= 0.9;
					totalCertaintyAfterSecondThrow += originalCertainty;
					continue;
				}
				if (assumedStrongholdChunk.isNeighboring(otherChunk))
					continue;
				double phiToPrediction = Coords.getPhi(otherChunk.eighteightX() - lastThrow.xInOverworld(), otherChunk.eighteightZ() - lastThrow.zInOverworld());
				double errorLikelihood = measurementErrorPdf(phiToPrediction - phiToStronghold, standardDeviation);
				totalCertaintyAfterSecondThrow += otherChunk.weight * errorLikelihood;
			}
			double newCertaintyForRealStronghold = originalCertainty / totalCertaintyAfterSecondThrow;
			if (newCertaintyForRealStronghold < lowestPossibleCertainty)
				lowestPossibleCertainty = newCertaintyForRealStronghold;
		}
		return lowestPossibleCertainty;
	}

	private double measurementErrorPdf(double errorInRadians, double sigma) {
		double error = errorInRadians * 180.0 / Math.PI;
		return Math.exp(-error * error / (2 * sigma * sigma));
	}

}
