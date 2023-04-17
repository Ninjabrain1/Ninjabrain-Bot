package ninjabrainbot.io.overlay;

import java.awt.image.BufferedImage;

public interface IImageWriter {

	void write(BufferedImage image);

	void delete();

}
