package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.information.InformationMessage;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.model.information.InformationMessageSeverity;
import org.json.JSONArray;
import org.json.JSONObject;

public class InformationMessagesQuery implements IQuery {

	private final InformationMessageList informationMessageList;
	private final boolean isPretty;

	public InformationMessagesQuery(InformationMessageList informationMessageList) {
		this(informationMessageList, false);
	}

	public InformationMessagesQuery(InformationMessageList informationMessageList, boolean isPretty) {
		this.informationMessageList = informationMessageList;
		this.isPretty = isPretty;
	}

	public String get() {
		JSONObject rootObject = new JSONObject();
		rootObject.put("informationMessages", convertInformationMessages());
		return rootObject.toString(isPretty ? 4 : 0);
	}

	@Override
	public boolean supportsSubscriptions() {
		return true;
	}

	private JSONArray convertInformationMessages() {
		JSONArray informationMessages = new JSONArray();
		for (InformationMessage informationMessage : informationMessageList) {
			JSONObject informationMessageJson = new JSONObject();
			informationMessageJson.put("severity", informationMessage.severity);
			informationMessageJson.put("type", informationMessage.type);
			informationMessageJson.put("message", informationMessage.message);
			informationMessages.put(informationMessageJson);
		}
		return informationMessages;
	}

}
