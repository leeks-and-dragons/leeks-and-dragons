package de.leeksanddragons.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Justin on 13.09.2017.
 */
public class ScreenshotUtils {

    public static void takeScreenshot (String saveFileName) {
        //take all pixels from framebuffer and save into byte array
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        PixmapIO.writePNG(Gdx.files.external(saveFileName), pixmap);
        pixmap.dispose();
    }

    public static String getScreenshotsHomeDir (String appName) {
        //get app.home directory
        String homeDir = FileUtils.getAppHomeDir(appName);

        String screenshotsDir = homeDir + "screenshots/";

        //create directory if not exists
        if (!new File(screenshotsDir).exists()) {
            new File(screenshotsDir).mkdirs();
        }

        return screenshotsDir;
    }

    public static String getScreenshotFileName () {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String dateTimeStr = format.format(new Date(System.currentTimeMillis()));

        return "screenshot_" + dateTimeStr + ".png";
    }

    public static String getScreenshotPath (String appName) {
        return getScreenshotsHomeDir(appName) + getScreenshotFileName();
    }

}
