package de.leeksanddragons.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.leeksanddragons.engine.preferences.WindowConfig;
import de.leeksanddragons.engine.utils.FileUtils;
import de.leeksanddragons.game.Game;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Starts the application for the desktop-based builds.
 *
 * @author Leeks & Dragons
 */
public class DesktopLauncher {

    /**
     * start method for game application
     */
    public static void main(String[] args) {
        //load window configuration
        WindowConfig windowConfig = new WindowConfig(FileUtils.getAppHomeDir("leeks-and-dragons") + "prefs/");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Leeks & Dragons";
        config.height = windowConfig.getHeight();
        config.width = windowConfig.getWidth();
        config.addIcon("./data/icon/app_icon.png", Files.FileType.Absolute);
        config.resizable = windowConfig.isResizeable();
        config.fullscreen = windowConfig.isFullscreen();

        //https://gamedev.stackexchange.com/questions/109047/how-to-close-an-app-correctly-on-desktop
        config.forceExit = false;

        try {
            // start game
            new LwjglApplication(new Game(windowConfig), config);
        } catch (Exception e) {
            e.printStackTrace();

            try {
                // write crash dump
                FileUtils.writeFile("./crash.log", e.getLocalizedMessage(), StandardCharsets.UTF_8);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            System.exit(-1);
        }
    }

}
