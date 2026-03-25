package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.documentation.ApiParam;
import ninjabrainbot.io.api.interfaces.IParametrizedCommand;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.DetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.LimitedPlayerPosition;
import ninjabrainbot.model.datastate.endereye.F3CData;
import ninjabrainbot.model.datastate.endereye.F3IData;
import ninjabrainbot.model.datastate.endereye.InputData1_12;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.input.IInputtedF3IToActionMapper;
import ninjabrainbot.model.input.IInputtedPlayerPositionToActionMapper;

public class InputClipboardCommand implements IParametrizedCommand<InputClipboardCommand.Args> {

	private final IInputtedPlayerPositionToActionMapper inputtedPlayerPositionToActionMapper;
	private final IInputtedF3IToActionMapper inputtedF3IToActionMapper;

	public InputClipboardCommand(IInputtedPlayerPositionToActionMapper inputtedPlayerPositionToActionMapper, IInputtedF3IToActionMapper inputtedF3IToActionMapper) {
		this.inputtedPlayerPositionToActionMapper = inputtedPlayerPositionToActionMapper;
		this.inputtedF3IToActionMapper = inputtedF3IToActionMapper;
	}

	@Override
	public String name() {
		return "input_clipboard";
	}

	@Override
	public String description() {
		return "Parses the given text and inputs it into the application as if the clipboard was set to it.";
	}

	@Override
	public IAction[] mapToActions(IDomainModel domainModel, IDataState dataState, Args args) {
		if (args.clipboardText == null)
			return new IAction[0];

		F3CData f3cData = F3CData.tryParseF3CString(args.clipboardText);
		if (f3cData != null) {
			DetailedPlayerPosition detailedPlayerPosition = new DetailedPlayerPosition(f3cData.x, f3cData.y, f3cData.z, f3cData.horizontalAngle, f3cData.verticalAngle, f3cData.dimension);
			return inputtedPlayerPositionToActionMapper.getActionsForInputtedPlayerPosition(detailedPlayerPosition);
		}

		InputData1_12 data1_12 = InputData1_12.parseInputString(args.clipboardText);
		if (data1_12 != null) {
			LimitedPlayerPosition limitedPlayerPosition = new LimitedPlayerPosition(data1_12.x, data1_12.z, data1_12.horizontalAngle, data1_12.correctionIncrements);
			return inputtedPlayerPositionToActionMapper.getActionsForInputtedLimitedPlayerPosition(limitedPlayerPosition);
		}

		F3IData f3iData = F3IData.tryParseF3IString(args.clipboardText);
		if (f3iData != null) {
			return inputtedF3IToActionMapper.getActionsForInputtedF3I(f3iData);
		}

		return new IAction[0];
	}

	public static class Args {

		@ApiParam(description = "Text to parse and input into the application as if the clipboard was set to it.",
				required = true,
				example = "/execute in minecraft:overworld run tp @s 0.00 0.00 0.00 0.00 0.00")
		private String clipboardText;

	}

}