package ninjabrainbot.integrationtests.apiv1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.io.api.documentation.ApiParam;
import ninjabrainbot.io.api.ApiV1Commands;
import ninjabrainbot.io.api.interfaces.ICommand;
import ninjabrainbot.io.api.interfaces.IParametrizedCommand;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class GenerateApiDocsTests {

	@Test
	void generateApiDocs() {
		List<ICommand> commands = ApiV1Commands.createAllCommands(null, null);

		String mainTemplate = loadTemplate("api_docs_template.md");
		String commandTemplate = loadTemplate("api_docs_command_template.md");
		String parametersTemplate = loadTemplate("api_docs_parameters_template.md");

		StringBuilder commandsTable = new StringBuilder();
		StringBuilder commandDetails = new StringBuilder();

		for (ICommand command : commands) {
			commandsTable.append("| [`").append(command.name()).append("`](#").append(command.name()).append(") | ").append(command.summary()).append(" |\n");
			commandDetails.append(renderCommandSection(command, commandTemplate, parametersTemplate));
		}

		String result = mainTemplate
				.replace("{{commands_table}}", commandsTable.toString())
				.replace("{{command_details}}", commandDetails.toString());

		System.out.println(result);
	}

	private String renderCommandSection(ICommand command, String commandTemplate, String parametersTemplate) {
		List<ParameterInfo> params = getParameters(command);

		JSONObject exampleJson = new JSONObject();
		exampleJson.put("command", command.name());

		String parametersSection;
		if (params.isEmpty()) {
			parametersSection = "This command takes no parameters.";
		} else {
			StringBuilder rows = new StringBuilder();
			JSONObject exampleParams = new JSONObject();
			for (ParameterInfo p : params) {
				rows.append("| `").append(p.name).append("` | `").append(p.type).append("` | ")
						.append(p.required ? "Yes" : "No").append(" | ").append(p.description).append(" |\n");
				if (p.example != null) {
					exampleParams.put(p.name, p.example);
				}
			}
			exampleJson.put("parameters", exampleParams);
			parametersSection = parametersTemplate.replace("{{parameters_table_rows}}", rows.toString());
		}

		return commandTemplate
				.replace("{{command_name}}", command.name())
				.replace("{{command_description}}", command.description())
				.replace("{{parameters_section}}", parametersSection)
				.replace("{{example_json}}", exampleJson.toString(2));
	}

	private static String loadTemplate(String name) {
		try (InputStream inputStream = GenerateApiDocsTests.class.getResourceAsStream("/apiv1/" + name)) {
			if (inputStream == null)
				throw new IllegalStateException("Template not found: " + name);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] data = new byte[4096];
			int bytesRead;
			while ((bytesRead = inputStream.read(data)) != -1) {
				buffer.write(data, 0, bytesRead);
			}
			return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load template: " + name, e);
		}
	}

	private static List<ParameterInfo> getParameters(ICommand command) {
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
