package ninjabrainbot.gui.mainwindow.autolock;

import java.util.HashMap;
import java.util.Objects;

import javax.swing.ImageIcon;

import ninjabrainbot.Main;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class AutoLockIcon extends ThemedLabel {

    public AutoLockIcon(
            StyleManager styleManager,
            NinjabrainBotPreferences preferences,
            DisposeHandler disposeHandler) {

        super(styleManager);

        updateIcon(preferences);

        disposeHandler.add(
            preferences.autoLock.whenModified().subscribeEDT(__ ->
                updateIcon(preferences)
            )
        );

        disposeHandler.add(
            preferences.autoLockEnabled.whenModified().subscribeEDT(__ ->
                updateIcon(preferences)
            )
        );
    }

    private void updateIcon(NinjabrainBotPreferences preferences) {
        setVisible(preferences.autoLock.get());
        setIcon(getAutoLockIcon(preferences));
    }

    private static final HashMap<String, ImageIcon> cachedIcons =
            new HashMap<>();

    private static ImageIcon getAutoLockIcon(
            NinjabrainBotPreferences preferences) {

        if (!preferences.autoLock.get()) {
            return null;
        }

        String path = preferences.autoLockEnabled.get()
                ? "/green_lock.png"
                : "/gray_lock.png";

        return getOrCreateCachedIcon(path);
    }

    private static ImageIcon getOrCreateCachedIcon(String path) {
        if (!cachedIcons.containsKey(path)) {
            cachedIcons.put(
                path,
                new ImageIcon(
                    Objects.requireNonNull(Main.class.getResource(path))
                )
            );
        }

        return cachedIcons.get(path);
    }
}
