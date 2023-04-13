package ninjabrainbot.model.input;

import ninjabrainbot.io.mcinstance.MinecraftInstance;
import ninjabrainbot.io.mcinstance.MinecraftWorldFile;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.environmentstate.IEnvironmentState;
import ninjabrainbot.util.FakeActiveInstanceProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActiveInstanceInputHandlerTest {

	@Mock
	IDomainModel domainModel;
	@Mock
	IActionExecutor actionExecutor;
	@Mock
	IDataState dataState;
	@Mock
	IEnvironmentState environmentState;

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void changingMinecraftWorldExecutesResetAction(boolean preferenceEnabled) {
		// Arrange
		NinjabrainBotPreferences preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		preferences.autoResetWhenChangingInstance.set(preferenceEnabled);

		Mockito.when(domainModel.isReset()).thenReturn(false);
		var locked = new DataComponent<>(domainModel, false);
		Mockito.when(dataState.locked()).thenReturn(locked);

		FakeActiveInstanceProvider activeInstanceProvider = new FakeActiveInstanceProvider();
		MinecraftInstance minecraftInstance = new MinecraftInstance("instance 1");
		activeInstanceProvider.currentWorldFile.set(new MinecraftWorldFile(minecraftInstance, "world 1"));

		ActiveInstanceInputHandler sut = new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, environmentState, actionExecutor, preferences);

		// Act
		activeInstanceProvider.currentWorldFile.set(new MinecraftWorldFile(minecraftInstance, "world 2"));

		// Assert
		if (preferenceEnabled) {
			Mockito.verify(actionExecutor, Mockito.only()).executeImmediately(ArgumentMatchers.any(ResetAction.class));
		} else {
			Mockito.verify(actionExecutor, Mockito.never()).executeImmediately(ArgumentMatchers.any());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void changingMinecraftWorld_FromNull_DoesNotExecuteResetAction(boolean preferenceEnabled) {
		// Arrange
		NinjabrainBotPreferences preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		preferences.autoResetWhenChangingInstance.set(preferenceEnabled);

		Mockito.when(domainModel.isReset()).thenReturn(false);
		var locked = new DataComponent<>(domainModel, false);
		Mockito.when(dataState.locked()).thenReturn(locked);

		FakeActiveInstanceProvider activeInstanceProvider = new FakeActiveInstanceProvider();
		MinecraftInstance minecraftInstance = new MinecraftInstance("instance 1");
		activeInstanceProvider.currentWorldFile.set(null);

		ActiveInstanceInputHandler sut = new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, environmentState, actionExecutor, preferences);

		// Act
		activeInstanceProvider.currentWorldFile.set(new MinecraftWorldFile(minecraftInstance, "world 1"));

		// Assert
		Mockito.verify(actionExecutor, Mockito.never()).executeImmediately(ArgumentMatchers.any());
	}

}