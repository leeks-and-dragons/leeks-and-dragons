package de.leeksanddragons.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.leeksanddragons.engine.utils.FileUtils;

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
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Leeks & Dragons";
        config.height = 600;
        config.width = 800;
        config.addIcon("./data/icon/app_icon.png", Files.FileType.Absolute);

        try {
            // start game
            new LwjglApplication(/*new Game()*/new ApplicationListener() {
                @Override
                public void create() {

                }

                @Override
                public void resize(int width, int height) {

                }

                @Override
                public void render() {

                }

                @Override
                public void pause() {

                }

                @Override
                public void resume() {

                }

                @Override
                public void dispose() {

                }
            }, config);
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
