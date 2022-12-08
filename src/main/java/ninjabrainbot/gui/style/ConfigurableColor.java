package ninjabrainbot.gui.style;

public class ConfigurableColor {
	
	public final WrappedColor color;
	
	public final String name;

	public final String uid;
	
	public ConfigurableColor(WrappedColor color, String name, String uid) {
		this.color = color;
		this.name = name;
		this.uid = uid;
	}
	
}
