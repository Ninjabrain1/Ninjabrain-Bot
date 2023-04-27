package ninjabrainbot.model.domainmodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.Test;

public class ListComponentTests {

	@Test
	public void getAsImmutable() {
		ListComponent<Integer> listComponent = new ListComponent<>(null, 10);
		IReadOnlyList<Integer> snapshot0 = listComponent.getAsImmutable();

		listComponent.add(2);
		IReadOnlyList<Integer> snapshot1 = listComponent.getAsImmutable();

		listComponent.add(10);
		IReadOnlyList<Integer> snapshot2 = listComponent.getAsImmutable();

		TestUtils.assertIterableEquals(Collections.emptyList(), snapshot0);
		TestUtils.assertIterableEquals(Collections.singletonList(2), snapshot1);
		TestUtils.assertIterableEquals(Arrays.asList(2, 10), snapshot2);
	}

}
