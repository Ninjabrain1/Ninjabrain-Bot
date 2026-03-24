package ninjabrainbot.integrationtests.apiv1;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.io.api.documentation.ApiParam;
import ninjabrainbot.io.api.ApiV1Commands;
import ninjabrainbot.io.api.interfaces.IApiCommand;
import ninjabrainbot.io.api.interfaces.IParametrizedCommand;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class GenerateApiDocsTests {

	@Test
	void generateApiDocs() {
		List<IApiCommand> commands = ApiV1Commands.createAllCommands();

		StringBuilder md = new StringBuilder();

		md.append("# API v1 – Commands\n\n");
		md.append("Commands are sent as `POST` requests to `/api/v1/commands` with a JSON body.\n\n");
		md.append("## Request format\n\n");
		md.append("```json\n");
		md.append("{\n");
		md.append("  \"command\": \"<command_name>\",\n");
		md.append("  \"parameters\": { ... }\n");
		md.append("}\n");
		md.append("```\n\n");
		md.append("The `parameters` property should be omitted for commands that take no parameters.\n\n");

		// Table of contents
		md.append("## Commands\n\n");
		md.append("| Command | Description |\n");
		md.append("| --- | --- |\n");
		for (IApiCommand command : commands) {
			md.append("| [`").append(command.name()).append("`](#").append(command.name().replace("_", "_")).append(") | ").append(command.description()).append(" |\n");
		}
		md.append("\n---\n\n");

		// Detail sections
		for (IApiCommand command : commands) {
			md.append("### `").append(command.name()).append("`\n\n");
			md.append(command.description()).append("\n\n");

			JSONObject exampleJson = new JSONObject();
			exampleJson.put("command", command.name());

			List<ParameterInfo> params = getParameters(command);

			if (params.isEmpty()) {
				md.append("This command takes no parameters.\n\n");
			} else {
				md.append("#### Parameters\n\n");
				md.append("| Name | Type | Required | Description |\n");
				md.append("| --- | --- | --- | --- |\n");

				JSONObject exampleParams = new JSONObject();
				for (ParameterInfo p : params) {
					md.append("| `").append(p.name).append("` | `").append(p.type).append("` | ")
							.append(p.required ? "Yes" : "No").append(" | ").append(p.description).append(" |\n");
					if (p.example != null) {
						exampleParams.put(p.name, p.example);
					}
				}
				md.append("\n");
				exampleJson.put("parameters", exampleParams);
			}

			md.append("#### Example\n\n");
			md.append("```json\n");
			md.append(exampleJson.toString(2)).append("\n");
			md.append("```\n\n");
			md.append("---\n\n");
		}

		System.out.println(md);
	}

	private static List<ParameterInfo> getParameters(IApiCommand command) {
		List<ParameterInfo> params = new ArrayList<>();
		if (!(command instanceof IParametrizedCommand))
			return params;

		for (Type implementedInterface : command.getClass().getGenericInterfaces()) {
			if (!(implementedInterface instanceof ParameterizedType))
				continue;

			ParameterizedType parameterizedType = (ParameterizedType) implementedInterface;
			if (parameterizedType.getRawType() != IParametrizedCommand.class)
				continue;

			Type argType = parameterizedType.getActualTypeArguments()[0];
			for (Field field : ((Class<?>) argType).getDeclaredFields()) {
				ApiParam annotation = field.getAnnotation(ApiParam.class);
				ParameterInfo info = new ParameterInfo();
				info.name = field.getName();
				info.type = field.getType().getSimpleName();
				info.description = annotation != null ? annotation.description() : "No description";
				info.required = annotation != null && annotation.required();
				info.example = annotation != null && !annotation.example().isEmpty() ? annotation.example() : null;
				params.add(info);
			}
		}
		return params;
	}

	private static class ParameterInfo {
		String name;
		String type;
		String description;
		boolean required;
		String example;
	}

}
