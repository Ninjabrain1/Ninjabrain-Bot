package ninjabrainbot.model.information;

public class InformationMessage {

	public final InformationMessageSeverity severity;
	public final String type;
	public final String message;

	public InformationMessage(InformationMessageSeverity severity, String type, String message) {
		this.severity = severity;
		this.type = type;
		this.message = message;
	}

}
