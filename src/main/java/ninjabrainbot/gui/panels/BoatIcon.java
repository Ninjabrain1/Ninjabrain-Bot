package ninjabrainbot.gui.panels;

import javax.swing.ImageIcon;

import ninjabrainbot.Main;
import ninjabrainbot.data.highprecision.BoatState;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.SubscriptionHandler;
import ninjabrainbot.gui.components.ThemedIcon;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

@SuppressWarnings("serial")
public class BoatIcon extends ThemedIcon {

	public BoatIcon(StyleManager styleManager, IObservable<BoatState> boatState, NinjabrainBotPreferences preferences, SubscriptionHandler sh) {
		super(styleManager, getBoatIcon(boatState.get()));
		setVisible(preferences.useTallRes.get() && preferences.usePreciseAngle.get());

		sh.add(boatState.subscribeEDT(b -> setIcon(getBoatIcon(b))));
		sh.add(preferences.useTallRes.whenModified().subscribeEDT(b -> setVisible(b && preferences.usePreciseAngle.get())));
		sh.add(preferences.usePreciseAngle.whenModified().subscribeEDT(b -> setVisible(b)));
	}

	private static ImageIcon getBoatIcon(BoatState boatState) {
		switch (boatState) {
			case ERROR:
				return new ImageIcon(Main.class.getResource("/boat_red.png"));
			case MEASURING:
				return new ImageIcon(Main.class.getResource("/boat_blue.png"));
			case VALID:
				return new ImageIcon(Main.class.getResource("/boat_green.png"));
			default:
				return new ImageIcon(Main.class.getResource("/boat_gray.png"));
		}
	}

}
