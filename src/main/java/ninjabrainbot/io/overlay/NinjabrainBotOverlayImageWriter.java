package ninjabrainbot.io.overlay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ninjabrainbot.util.Logger;

public class NinjabrainBotOverlayImageWriter implements IImageWriter {

	public static final File OBS_OVERLAY = new File(System.getProperty("java.io.tmpdir"), "nb-overlay.png");

	public void write(BufferedImage img) {
		try {
			ImageIO.write(img, "png", OBS_OVERLAY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete() {
		if (!OBS_OVERLAY.delete())
			Logger.log("Warning: Failed to delete OBS overlay image.");
	}

}
