package ninjabrainbot.io.mcinstance;

public interface IMinecraftWorldFile {

	MinecraftInstance minecraftInstance();

	String name();

	boolean hasEnteredEnd();

}
