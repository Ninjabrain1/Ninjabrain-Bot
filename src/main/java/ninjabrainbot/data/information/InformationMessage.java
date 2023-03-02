package ninjabrainbot.data.information;

public class InformationMessage {

	public final InformationType type;
	public final String message;

	public InformationMessage(InformationType type, String message) {
		this.type = type;
		this.message = message;
	}

}
