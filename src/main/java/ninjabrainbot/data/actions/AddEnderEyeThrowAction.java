package ninjabrainbot.data.actions;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.endereye.F3CData;
import ninjabrainbot.data.calculator.endereye.IStdProfile;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.endereye.InputData1_12;
import ninjabrainbot.data.calculator.endereye.Throw;
import ninjabrainbot.data.calculator.endereye.ThrowType;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class AddEnderEyeThrowAction implements IAction {

	private final IDataState dataState;
	private final IPlayerPosition playerPosition;

	public AddEnderEyeThrowAction(IDataState dataState, IPlayerPosition playerPosition) {
		this.dataState = dataState;
		this.playerPosition = playerPosition;
	}

	@Override
	public void execute() {
		F3CData f3cData = F3CData.tryParseF3CString(f3c);
		if (f3cData != null) {
			boolean boatThrow = preferences.useTallRes.get() && preferences.usePreciseAngle.get() && boatAngle.get() != null;
			IThrow newThrow = boatThrow ? createBoatThrow(f3cData, boatAngle.get(), preferences, stdProfile) : createNormalThrow(f3cData, preferences.crosshairCorrection.get(), stdProfile);
			whenNewPlayerPositionInputted.set(newThrow);
			return;
		}

		InputData1_12 data1_12 = InputData1_12.parseInputString(f3c);
		if (data1_12 != null) {
			IThrow newThrow = createThrowForVersion1_12(data1_12, preferences.crosshairCorrection.get(), stdProfile);
			whenNewPlayerPositionInputted.set(newThrow);
			return;
		}

		dataState.getThrowSet().add(playerPosition);
	}

	public static IThrow parseNormalThrow(String string, double crosshairCorrection, IStdProfile stdProfile) {
		F3CData f3cData = F3CData.tryParseF3CString(string);
		if (f3cData == null)
			return null;
		return createNormalThrow(f3cData, crosshairCorrection, stdProfile);
	}

	public static IThrow createNormalThrow(F3CData data, double crosshairCorrection, IStdProfile stdProfile) {
		double correctedHorizontalAngle = getCorrectedHorizontalAngle(data.horizontalAngle, crosshairCorrection);
		return new Throw(data.x, data.y, data.z, correctedHorizontalAngle, data.verticalAngle, data.nether, ThrowType.Normal, stdProfile);
	}

	public static IThrow createBoatThrow(F3CData data, float currentBoatAngle, NinjabrainBotPreferences preferences, IStdProfile stdProfile) {
		double correctedHorizontalAngle = getPreciseBoatHorizontalAngle(data.horizontalAngle, preferences, currentBoatAngle);
		return new Throw(data.x, data.y, data.z, correctedHorizontalAngle, data.verticalAngle, data.nether, ThrowType.Boat, stdProfile);
	}

	public static IThrow createThrowForVersion1_12(InputData1_12 data, double crosshairCorrection, IStdProfile stdProfile) {
		double correctedHorizontalAngle = data.horizontalAngle + crosshairCorrection;
		return new Throw(data.x, 80, data.z, correctedHorizontalAngle, -31, false, ThrowType.McVersion1_12, stdProfile);
	}

	protected static double getCorrectedHorizontalAngle(double alpha, double crosshairCorrection) {
		alpha += crosshairCorrection;

		// Determined experimentally, exact cause unknown
		alpha -= 0.00079 * Math.sin((alpha + 45) * Math.PI / 180.0);

		return alpha;
	}

	private static double getPreciseBoatHorizontalAngle(double alpha, NinjabrainBotPreferences preferences, float boatAngle) {
		double sensitivity = preferences.sensitivity.get();
		double preMultiplier = sensitivity * 0.6f + 0.2f;
		preMultiplier = preMultiplier * preMultiplier * preMultiplier * 8.0f;
		double minInc = preMultiplier * 0.15D;
		alpha = boatAngle + Math.round((alpha - boatAngle) / minInc) * minInc;

		return getCorrectedHorizontalAngle(alpha, preferences.crosshairCorrection.get());
	}

}
