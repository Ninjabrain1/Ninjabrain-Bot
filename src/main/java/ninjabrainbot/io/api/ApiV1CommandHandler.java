package ninjabrainbot.io.api;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ninjabrainbot.io.api.interfaces.ICommand;
import ninjabrainbot.io.api.interfaces.IDomainModelCommand;
import ninjabrainbot.io.api.interfaces.IParameterlessCommand;
import ninjabrainbot.io.api.interfaces.IParametrizedCommand;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.util.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiV1CommandHandler {

	private final IDomainModel domainModel;
	private final IDataState dataState;
	private final IActionExecutor actionExecutor;
	private final Map<String, ICommand> commands;

	public ApiV1CommandHandler(IDomainModel domainModel, IDataState dataState, IActionExecutor actionExecutor) {
		this.domainModel = domainModel;
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
		this.commands = ApiV1Commands
				.createAllCommands()
				.stream()
				.collect(Collectors.toMap(ICommand::name, x -> x));
	}

	public Result handleCommandRequest(String endpoint, String requestBody) {
		List<JSONObject> commandJsonObjects;

		if (requestBody.isEmpty())
			return Result.error("Missing request body.");

		try {
			if (endpoint.equals("send-command")) {
				JSONObject json = new JSONObject(requestBody);
				commandJsonObjects = Collections.singletonList(json);
			} else if (endpoint.equals("send-commands")) {
				JSONObject json = new JSONObject(requestBody);
				JSONArray commandsArray = json.getJSONArray("commands");
				commandJsonObjects = new ArrayList<>();
				for (int i = 0; i < commandsArray.length(); i++) {
					commandJsonObjects.add(commandsArray.getJSONObject(i));
				}
			} else {
				return Result.error("Unknown POST endpoint: " + endpoint);
			}
		} catch (JSONException e) {
			Logger.log("Failed to parse command JSON: " + e.getMessage());
			return Result.error("Malformed JSON: " + e.getMessage());
		}

		List<IDomainModelCommand> domainModelCommands = new ArrayList<>();
		List<IAction> actions = new ArrayList<>();

		for (JSONObject commandJson : commandJsonObjects) {
			String commandName = commandJson.optString("command", null);
			if (commandName == null)
				return Result.error("Missing 'command' property.");

			ICommand command = commands.get(commandName);
			if (command == null)
				return Result.error("Unknown command: " + commandName);

			JSONObject parameters = commandJson.optJSONObject("parameters");

			if (command instanceof IDomainModelCommand) {
				domainModelCommands.add((IDomainModelCommand) command);
			} else if (command instanceof IParameterlessCommand) {
				for (IAction action : ((IParameterlessCommand) command).mapToActions(domainModel, dataState)) {
					actions.add(action);
				}
			} else if (command instanceof IParametrizedCommand) {
				if (parameters == null)
					return Result.error("Missing 'parameters' for command: " + commandName);
				try {
					Object args = deserializeArgs((IParametrizedCommand<?>) command, parameters);
					for (IAction action : invokeMapToActions((IParametrizedCommand<?>) command, args)) {
						actions.add(action);
					}
				} catch (Exception e) {
					Logger.log("Failed to deserialize command parameters: " + e.getMessage());
					return Result.error("Invalid parameters for command '" + commandName + "': " + e.getMessage());
				}
			}
		}

		if (!domainModelCommands.isEmpty() && !actions.isEmpty()) {
			return Result.error("Command '" + domainModelCommands.get(0).name() + "' cannot be executed together with other commands.");
		}

		if (domainModelCommands.size() > 1) {
			return Result.error("Command '" + domainModelCommands.get(1).name() + "' cannot be executed together with other commands.");
		}

		if (domainModelCommands.size() == 1) {
			domainModelCommands.get(0).execute(domainModel);
		} else if (!actions.isEmpty()) {
			actionExecutor.executeImmediately(actions.toArray(new IAction[0]));
		}

		return Result.success();
	}

	private Object deserializeArgs(IParametrizedCommand<?> command, JSONObject parameters) throws Exception {
		Class<?> argsType = getArgsType(command);
		Object args = argsType.newInstance();
		for (Field field : argsType.getDeclaredFields()) {
			String fieldName = field.getName();
			if (!parameters.has(fieldName))
				continue;
			field.setAccessible(true);
			Class<?> fieldType = field.getType();
			if (fieldType == String.class) {
				field.set(args, parameters.getString(fieldName));
			} else if (fieldType == int.class || fieldType == Integer.class) {
				field.set(args, parameters.getInt(fieldName));
			} else if (fieldType == long.class || fieldType == Long.class) {
				field.set(args, parameters.getLong(fieldName));
			} else if (fieldType == double.class || fieldType == Double.class) {
				field.set(args, parameters.getDouble(fieldName));
			} else if (fieldType == float.class || fieldType == Float.class) {
				field.set(args, (float) parameters.getDouble(fieldName));
			} else if (fieldType == boolean.class || fieldType == Boolean.class) {
				field.set(args, parameters.getBoolean(fieldName));
			} else {
				throw new IllegalArgumentException("Unsupported parameter type: " + fieldType.getSimpleName());
			}
		}
		return args;
	}

	private Class<?> getArgsType(IParametrizedCommand<?> command) {
		for (Type implementedInterface : command.getClass().getGenericInterfaces()) {
			if (!(implementedInterface instanceof ParameterizedType))
				continue;
			ParameterizedType parameterizedType = (ParameterizedType) implementedInterface;
			if (parameterizedType.getRawType() == IParametrizedCommand.class) {
				return (Class<?>) parameterizedType.getActualTypeArguments()[0];
			}
		}
		throw new IllegalStateException("Could not determine args type for command: " + command.name());
	}

	@SuppressWarnings("unchecked")
	private <TArgs> Iterable<IAction> invokeMapToActions(IParametrizedCommand<TArgs> command, Object args) {
		return command.mapToActions(domainModel, dataState, (TArgs) args);
	}

	public static class Result {
		public final boolean success;
		public final String errorMessage;

		private Result(boolean success, String errorMessage) {
			this.success = success;
			this.errorMessage = errorMessage;
		}

		static Result success() {
			return new Result(true, null);
		}

		static Result error(String errorMessage) {
			return new Result(false, errorMessage);
		}
	}

}

