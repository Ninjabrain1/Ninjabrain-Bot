package ninjabrainbot.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DisposeHandlerTest {

	@Mock
	IDisposable disposable1, disposable2, disposable3;

	@Test
	void disposesAll() {
		// Arrange
		DisposeHandler disposeHandler = new DisposeHandler();
		disposeHandler.add(disposable1);
		disposeHandler.add(disposable2);
		disposeHandler.add(disposable3);

		// Act
		disposeHandler.dispose();

		// Assert
		Mockito.verify(disposable1).dispose();
		Mockito.verify(disposable2).dispose();
		Mockito.verify(disposable3).dispose();
	}
}