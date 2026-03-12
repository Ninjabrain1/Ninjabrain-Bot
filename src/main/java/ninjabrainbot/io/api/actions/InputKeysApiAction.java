package ninjabrainbot.io.api.actions;


import ninjabrainbot.io.preferences.HotkeyPreference;
import org.json.JSONArray;

import javax.swing.SwingUtilities;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InputKeysApiAction implements IApiAction {
	@Override
	public String post(String body) throws RuntimeException {
		Map<String, HotkeyPreference> hotkeyMap = HotkeyPreference.hotkeys.stream()
				.collect(Collectors.toMap(HotkeyPreference::getKey, Function.identity(), (h1, h2) -> h1));

		JSONArray hotkeys = new JSONArray(body);
		for (Object input : hotkeys) {
			String hotkeyId;
			int count;
			if (input instanceof JSONArray) {
				JSONArray array = (JSONArray) input;
				hotkeyId = array.getString(0);
				count = array.getInt(1);
			} else if (input instanceof String) {
				hotkeyId = (String) input;
				count = 1;
			} else {
				throw new RuntimeException("Invalid Format: " + input);
			}
			// prevent accidental over pressing
			count = Math.min(count, 50);

			HotkeyPreference hotkey = hotkeyMap.get(hotkeyId);
			if (hotkey != null)
				for (int i = 0; i < count; i++) {
					SwingUtilities.invokeLater(hotkey::execute);
				}
			else
				throw new IllegalArgumentException("Invalid Hotkey: " + input);
		}
		return "success";
	}
}
