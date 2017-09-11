package de.leeksanddragons.engine.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.camera.ResizeListener;
import de.leeksanddragons.engine.camera.manager.CameraManager;
import de.leeksanddragons.engine.camera.manager.DefaultCameraManager;
import de.leeksanddragons.engine.cursor.CursorManager;
import de.leeksanddragons.engine.cursor.DefaultCursorManager;
import de.leeksanddragons.engine.preferences.GamePreferences;
import de.leeksanddragons.engine.preferences.IPreferences;
import de.leeksanddragons.engine.utils.FileUtils;
import de.leeksanddragons.engine.utils.GameTime;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Justin on 09.09.2017.
 */
public abstract class BaseGame extends ApplicationAdapter implements IGame {

    //camera manager
    protected CameraManager cameraManager = null;

    //list with resize listeners
    protected List<ResizeListener> resizeListeners = new ArrayList<>();

    //instance of game time
    protected GameTime time = GameTime.getInstance();

    //tasks which should be executed in OpenGL context thread
    protected Queue<Runnable> uiQueue = new ConcurrentLinkedQueue<>();

    //last second of FPS warning
    protected long lastFPSWarning = 0;

    //cursor manager
    protected CursorManager cursorManager = null;

    //window background (clear) color
    protected Color bgColor = Color.BLACK;

    //sprite batch
    protected SpriteBatch batch = null;

    //preferences map
    protected Map<String,GamePreferences> prefsMap = new HashMap<>();

    //app name
    protected String appName = "";

    public BaseGame (String appName) {
        this.appName = appName.toLowerCase();
    }

    @Override
    public final void create () {
        //create new camera manager and set viewport of current window dimensions
        this.cameraManager = new DefaultCameraManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //create new cursor manager
        this.cursorManager = new DefaultCursorManager();

        //log user.home and app home dir
        Gdx.app.log("Files", "user.home: " + FileUtils.getUserHomeDir());

        //check, if app home dir exists, else create directory
        if (!new File(getAppHomeDir()).exists()) {
            //create directory
            new File(getAppHomeDir()).mkdirs();
        }

        //log app home dir
        Gdx.app.log("Files", "app.home: " + getAppHomeDir());

        // create sprite batcher
        this.batch = new SpriteBatch();

        //try to initialize game, if an exception is thrown log will be written to application directory
        try {
            this.initGame();
        } catch (Exception e) {
            e.printStackTrace();

            try {
                // write crash dump
                FileUtils.writeFile("./crash.log", e.getLocalizedMessage(), StandardCharsets.UTF_8);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            System.exit(0);
        }
    }

    @Override
    public final void resize (int width, int height) {
        Gdx.app.debug("Window", "new window size: " + width + "x" + height + "");

        //call resize listeners
        this.resizeListeners.stream().forEach(consumer -> {
            consumer.onResize(width, height);
        });
    }

    /**
    * add resize listener
     *
     * @param listener resize listener which is called, if window was resized
    */
    @Override
    public void addResizeListener(ResizeListener listener) {
        this.resizeListeners.add(listener);
    }

    /**
     * remove resize listener
     *
     * @param listener resize listener which is called, if window was resized
     */
    @Override
    public void removeResizeListener(ResizeListener listener) {
        this.resizeListeners.remove(listener);
    }

    @Override
    public final void render () {
        //update game time
        this.time.update();

        //check for FPS warning
        int fps = getFPS();
        if (fps <= 59 && fps != 0) {
            // check if warning was already logged this second
            long now = System.currentTimeMillis();
            long nowWarnSecond = now / 1000;
            long lastWarnSecond = lastFPSWarning / 1000;

            if (nowWarnSecond != lastWarnSecond) {
                Gdx.app.log("FPS", "Warning! FPS is <= 59, FPS: " + fps);

                lastFPSWarning = System.currentTimeMillis();
            }
        }

        //execute tasks, which should be executed in OpenGL context thread
        Runnable runnable = uiQueue.poll();

        while (runnable != null) {
            runnable.run();

            runnable = uiQueue.poll();
        }

        //update game
        this.update(this.time);

        // set cursor
        this.cursorManager.update(this, this.time);

        //update cameras
        this.cameraManager.update(this.time);

        //clear all color buffer bits and clear screen
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //beginn rendering process
        this.batch.begin();

        //set camera projection matrix
        this.batch.setProjectionMatrix(this.getCameraManager().getMainCamera().getCombined());

        //draw game
        this.draw(time, this.batch);

        //flush rendering
        this.batch.end();
    }

    /**
    * get frames per second count
     *
     * @return FPS count
    */
    @Override
    public int getFPS() {
        return Gdx.graphics.getFramesPerSecond();
    }

    /**
    * get camera manager
     *
     * @return instance of camera manager
    */
    @Override
    public CameraManager getCameraManager () {
        return this.cameraManager;
    }

    @Override
    public AssetManager getAssetManager () {
        Gdx.app.getPreferences("test");
        return null;
    }

    @Override
    public String getAppHomeDir () {
        return FileUtils.getAppHomeDir(this.appName);
    }

    /**
     * get general preferences
     *
     * @return instance of general preferences
     */
    @Override
    public IPreferences getGeneralPreferences () {
        return getPreferences("general");
    }

    /**
     * get preferences by category
     *
     * @return instance of preferences by category
     */
    @Override
    public IPreferences getPreferences (String category) {
        category = category.toLowerCase();

        //check, if preferences already exists
        if (!this.prefsMap.containsKey(category) || this.prefsMap.get(category) == null) {
            //create new instance of preferences
            this.prefsMap.put(category, new GamePreferences(this.getAppName(), category));
        }

        //return instance of preferences from map
        return this.prefsMap.get(category);
    }

    /**
     * get name of application
     *
     * @return name of application
     */
    public String getAppName () {
        return this.appName;
    }

    /**
    * execute runnable on next render cycle in UI thread
     *
     * @param runnable runnable to execute in UI thread
    */
    @Override
    public void runOnUIThread(Runnable runnable) {
        this.uiQueue.offer(runnable);
    }

    /**
    * initialize game
    */
    protected abstract void initGame();

    /**
    * update game
     *
     * @param time current game time
    */
    protected abstract void update(GameTime time);

    /**
    * draw game
     *
     * @param time current game time
     * @param batch instance of sprite batch
    */
    protected abstract void draw(GameTime time, SpriteBatch batch);

    /**
    * cleanup game
    */
    protected abstract void destroyGame();

    @Override
    public void pause () {
        Gdx.app.debug("Window-Lifecycle", "window rendering paused.");
    }

    @Override
    public void resume () {
        Gdx.app.debug("Window-Lifecycle", "window rendering resumed.");
    }

    @Override
    public final void dispose () {
        this.destroyGame();
    }

}
