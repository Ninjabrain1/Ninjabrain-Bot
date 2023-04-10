package ninjabrainbot.data.domainmodel;

import java.util.List;

import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.Test;

public class ListComponentTests {

	@Test
	public void getAsImmutable() {
		ListComponent<Integer> listComponent = new ListComponent<>(null, 10);
		var snapshot0 = listComponent.getAsImmutable();

		listComponent.add(2);
		var snapshot1 = listComponent.getAsImmutable();

		listComponent.add(10);
		var snapshot2 = listComponent.getAsImmutable();

		TestUtils.assertIterableEquals(List.of(), snapshot0);
		TestUtils.assertIterableEquals(List.of(2), snapshot1);
		TestUtils.assertIterableEquals(List.of(2, 10), snapshot2);
	}

}
