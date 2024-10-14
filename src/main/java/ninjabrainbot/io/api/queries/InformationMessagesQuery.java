package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.information.InformationMessage;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.model.information.InformationType;
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
			informationMessageJson.put("type", mapInformationType(informationMessage.type));
			informationMessageJson.put("message", informationMessage.message);
			informationMessages.put(informationMessageJson);
		}
		return informationMessages;
	}

	private String mapInformationType(InformationType informationType){
		switch (informationType){
			case Info:
				return "info";
			case Warning:
				return "warning";
			case Error:
				return "error";
		}
		throw new IllegalArgumentException("Unknown information type: " + informationType);
	}

}
