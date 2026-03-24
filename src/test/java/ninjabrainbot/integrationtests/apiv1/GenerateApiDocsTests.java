package ninjabrainbot.integrationtests.apiv1;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import ninjabrainbot.io.api.documentation.ApiParam;
import ninjabrainbot.io.api.ApiV1Commands;
import ninjabrainbot.io.api.interfaces.IApiCommand;
import ninjabrainbot.io.api.interfaces.IParametrizedCommand;
import org.junit.jupiter.api.Test;

public class GenerateApiDocsTests {

	@Test
	void generateApiDocs() {
		List<IApiCommand> commands = ApiV1Commands.createAllCommands();

		for (IApiCommand command : commands) {
			System.out.println("Command: " + command.name());
			System.out.println("  " + command.description());

			if (command instanceof IParametrizedCommand) {
				for (Type implementedInterface : command.getClass().getGenericInterfaces()) {
					if (!(implementedInterface instanceof ParameterizedType))
						continue;

					ParameterizedType parameterizedType = (ParameterizedType) implementedInterface;
					if (parameterizedType.getRawType() != IParametrizedCommand.class)
						continue;

					Type argType = parameterizedType.getActualTypeArguments()[0];
					System.out.println("  Parameters:");
					for (Field field : ((Class<?>) argType).getDeclaredFields()) {
						ApiParam annotation = field.getAnnotation(ApiParam.class);

						String name = field.getName();
						String type = field.getType().getSimpleName();

						String description = annotation != null
								? annotation.description()
								: "No description";

						boolean required = annotation != null && annotation.required();

						System.out.println("    " + name + " (" + type + ")");
						System.out.println("      Required: " + required);
						System.out.println("      " + description);
					}
				}
			}
			System.out.println();
		}
	}

}

