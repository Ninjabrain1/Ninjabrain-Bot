package ninjabrainbot.gui.mainwindow;

import java.util.HashMap;
import java.util.Objects;

import javax.swing.ImageIcon;

import ninjabrainbot.Main;
import ninjabrainbot.data.calculator.highprecision.BoatState;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class BoatIcon extends ThemedLabel {

	public BoatIcon(StyleManager styleManager, IObservable<BoatState> boatState, NinjabrainBotPreferences preferences, DisposeHandler sh) {
		super(styleManager);
		setIcon(getBoatIcon(boatState.get()));
		setVisible(preferences.useTallRes.get() && preferences.usePreciseAngle.get());

		sh.add(boatState.subscribeEDT(b -> setIcon(getBoatIcon(b))));
		sh.add(preferences.useTallRes.whenModified().subscribeEDT(b -> setVisible(b && preferences.usePreciseAngle.get())));
		sh.add(preferences.usePreciseAngle.whenModified().subscribeEDT(this::setVisible));
	}

	private static final HashMap<String, ImageIcon> cachedIcons = new HashMap<>();

	public static ImageIcon getBoatIcon(BoatState boatState) {
		switch (boatState) {
			case ERROR:
				return getOrCreateCachedIcon("/boat_red.png");
			case MEASURING:
				return getOrCreateCachedIcon("/boat_blue.png");
			case VALID:
				return getOrCreateCachedIcon("/boat_green.png");
			default:
				return getOrCreateCachedIcon("/boat_gray.png");
		}
	}

	private static ImageIcon getOrCreateCachedIcon(String path) {
		if (!cachedIcons.containsKey(path))
			cachedIcons.put(path, new ImageIcon(Objects.requireNonNull(Main.class.getResource(path))));
		return cachedIcons.get(path);
	}

}
