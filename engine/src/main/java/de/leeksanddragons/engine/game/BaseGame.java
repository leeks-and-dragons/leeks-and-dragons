package de.leeksanddragons.engine.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.camera.ResizeListener;
import de.leeksanddragons.engine.camera.manager.CameraManager;
import de.leeksanddragons.engine.camera.manager.DefaultCameraManager;
import de.leeksanddragons.engine.controller.ControllerManager;
import de.leeksanddragons.engine.controller.impl.DefaultControllerManager;
import de.leeksanddragons.engine.cursor.CursorManager;
import de.leeksanddragons.engine.cursor.DefaultCursorManager;
import de.leeksanddragons.engine.data.DefaultSharedData;
import de.leeksanddragons.engine.data.SharedData;
import de.leeksanddragons.engine.input.InputManager;
import de.leeksanddragons.engine.input.impl.DefaultInputManager;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.preferences.GamePreferences;
import de.leeksanddragons.engine.preferences.WindowConfig;
import de.leeksanddragons.engine.sound.SoundManager;
import de.leeksanddragons.engine.sound.impl.DefaultSoundManager;
import de.leeksanddragons.engine.timer.*;
import de.leeksanddragons.engine.utils.FileUtils;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.engine.utils.ScreenshotUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    protected int lastFPS = 0;

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

    //shared data
    protected SharedData sharedData = null;

    //controller manager
    protected ControllerManager controllerManager = null;

    //asset manager
    protected GameAssetManager assetManager = null;

    //last asset manager progress
    protected float lastAssetManagerProgress = 0f;

    //list with timer tasks
    protected List<GameTimerTask> timerTasks = new ArrayList<>();

    //temporary list for performance / GC optimization
    protected List<GameTimerTask> removeTimerTaskList = new ArrayList<>();

    //window configuration
    protected WindowConfig windowConfig = null;

    //sound manager
    protected SoundManager soundManager = null;

    protected static final int EXECUTOR_SERVICE_THREADS = 2;

    //executor service
    protected ExecutorService executorService = null;

    //input manager
    protected InputManager inputManager = null;

    public BaseGame (WindowConfig windowConfig, String appName) {
        this.windowConfig = windowConfig;
        this.appName = appName.toLowerCase();
    }

    @Override
    public final void create () {
        //create new camera manager and set viewport of current window dimensions
        this.cameraManager = new DefaultCameraManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //create new cursor manager
        this.cursorManager = new DefaultCursorManager();

        //create new shared data
        this.sharedData = new DefaultSharedData();

        //create new controller manager
        this.controllerManager = new DefaultControllerManager();

        //create new input manager
        this.inputManager = new DefaultInputManager();

        //create new asset manager
        this.assetManager = new GameAssetManager();

        //set asset logger
        this.assetManager.getLogger().setLevel(Gdx.app.getLogLevel());

        //set error listener
        this.assetManager.setErrorListener(new AssetErrorListener() {
            @Override
            public void error(AssetDescriptor asset, Throwable throwable) {
                Gdx.app.error("GameAssetManager", "AssetError occurred! asset: " + asset, throwable);

                throw new GdxRuntimeException(throwable);
            }
        });

        //create new executor service
        this.executorService = Executors.newFixedThreadPool(EXECUTOR_SERVICE_THREADS);

        //log user.home and app home dir
        Gdx.app.log("Files", "user.home: " + FileUtils.getUserHomeDir());

        //check, if app home dir exists, else create directory
        if (!new File(getAppHomeDir()).exists()) {
            //create directory
            new File(getAppHomeDir()).mkdirs();
        }

        //check, if prefs dir exists, else create directory
        if (!new File(getAppHomeDir() + "prefs").exists()) {
            //create directory
            new File(getAppHomeDir() + "prefs").mkdirs();
        }

        //check, if mods dir exists, else create directory
        if (!new File(getAppHomeDir() + "mods").exists()) {
            //create directory
            new File(getAppHomeDir() + "mods").mkdirs();
        }

        //log app home dir
        Gdx.app.log("Files", "app.home: " + getAppHomeDir());

        //create new sound manager
        this.soundManager = new DefaultSoundManager(this.assetManager, this.getGeneralPreferences());

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
        if ((fps <= 59 && fps != 0) || this.lastFPS <= 59) {
            // check if warning was already logged this second
            long now = System.currentTimeMillis();
            long nowWarnSecond = now / 1000;
            long lastWarnSecond = lastFPSWarning / 1000;

            if (nowWarnSecond != lastWarnSecond) {
                Gdx.app.log("FPS", "Warning! FPS is <= 59, FPS: " + fps);

                lastFPSWarning = System.currentTimeMillis();

                lastFPS = fps;
            }
        }

        //switch window mode to fullscreen mode with keys CTRL + F
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.F)) {
            boolean fullScreen = !Gdx.graphics.isFullscreen();

            if (fullScreen) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                Gdx.graphics.setWindowedMode(windowConfig.getWidth(), windowConfig.getHeight());
            }
        }

        //take screenshots with keys CTRL + C
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            //generate screenshot path
            String screenshotPath = ScreenshotUtils.getScreenshotPath(this.getAppName());

            //take screenshot
            try {
                ScreenshotUtils.takeScreenshot(screenshotPath);

                Gdx.app.log("Screenshot", "take an new screenshot, saved to " + screenshotPath + ".");
            } catch (IOException e) {
                Gdx.app.error("Screnshot", "Cannot save screenshot (" + screenshotPath + "), because IOException was thrown.", e);
                e.printStackTrace();
            } catch (Exception e) {
                Gdx.app.error("Screnshot", "Cannot save screenshot, because Exception was thrown.", e);
                e.printStackTrace();
            }
        }

        try {
            //update sound manager
            this.soundManager.udpate(this.time);
        } catch (Exception e) {
            e.printStackTrace();

            Gdx.app.error("BaseGame", "exception thrown while updating sound manager: " + e.getLocalizedMessage(), e);
        }

        //update asset manager, so asset manager can load assets from queue
        if(this.assetManager.updateLoading()) {
            // we are done loading, let's move to another screen!
        }

        //show asset manager progress, if neccessary
        if (assetManager.getProgress() != this.lastAssetManagerProgress) {
            //log asset manager progress
            Gdx.app.debug("AssetManager", "loading progress: " + (assetManager.getProgress() * 100) + "%.");

            //save progress
            this.lastAssetManagerProgress = assetManager.getProgress();
        }

        //clear temporary list
        this.removeTimerTaskList.clear();

        //execute overdued timer tasks
        for (GameTimerTask timerTask : this.timerTasks) {
            //check, if delay was reached
            if (timerTask.isDelayReached()) {
                //execute task
                timerTask.execute();

                //remove task
                this.removeTimerTaskList.add(timerTask);
            }
        }

        //remove tasks
        this.timerTasks.removeAll(this.removeTimerTaskList);

        //execute tasks, which should be executed in OpenGL context thread
        Runnable runnable = uiQueue.poll();

        while (runnable != null) {
            runnable.run();

            runnable = uiQueue.poll();
        }

        try {
            //update game
            this.update(this.time);
        } catch (Exception e) {
            e.printStackTrace();

            Gdx.app.error("BaseGame", "exception thrown while updating game: " + e.getLocalizedMessage(), e);
        }

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

        try {
            //draw game
            this.draw(time, this.batch);
        } catch (Exception e) {
            e.printStackTrace();

            Gdx.app.error("BaseGame", "exception thrown while drawing game: " + e.getLocalizedMessage(), e);
        }

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
    public GameAssetManager getAssetManager () {
        return this.assetManager;
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
    public GamePreferences getGeneralPreferences () {
        return getPreferences("general");
    }

    /**
     * get preferences by category
     *
     * @return instance of preferences by category
     */
    @Override
    public GamePreferences getPreferences (String category) {
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
     * get instance of shared data (non-persistent data, only available on runtime)
     *
     * @return instance of shared data
     */
    public SharedData getSharedData () {
        return this.sharedData;
    }

    /**
     * get controller manager
     *
     * @return instance of controller manager
     */
    public ControllerManager getControllerManager () {
        return this.controllerManager;
    }

    /**
     * get input manager
     *
     * @return instance of input manager
     */
    public InputManager getInputManager () {
        return this.inputManager;
    }

    /**
    * get instance of sprite batch
     *
     * @return instance of sprite batch
    */
    public SpriteBatch getSpriteBatch () {
        return this.batch;
    }

    /**
     * add an timer task which will be executed once after an given time in milliseconds
     *
     * @param delay time in millis to wait, before executing task
     * @param runnable timer task
     */
    public void addTimerTask (long delay, Runnable runnable) {
        //create new timer task
        GameTimerTask timerTask = new GameTimerTask(delay, runnable);

        //set start time
        timerTask.start();

        //add task to list
        this.timerTasks.add(timerTask);
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
     * get executor service
     *
     * @return instance of executor service
     */
    public ExecutorService getExecutorService () {
        return this.executorService;
    }

    /**
     * get instance of main camera
     *
     * @return instance of main camera
     */
    public CameraHelper getMainCamera () {
        return this.getCameraManager().getMainCamera();
    }

    /**
     * get instance of UI camera
     *
     * @return instance of UI camera
     */
    public CameraHelper getUICamera () {
        return this.getCameraManager().getUICamera();
    }

    /**
     * check, if sound is muted
     *
     * @return true, if sound is muted
     */
    public boolean isSoundMuted () {
        return getSoundManager().isSoundMuted();
    }

    /**
     * check, if music is muted
     *
     * @return true, if music is muted
     */
    public boolean isMusicMuted () {
        return getSoundManager().isMusicMuted();
    }

    /**
     * get instance of sound manager
     *
     * @return instance of sound manager
     */
    public SoundManager getSoundManager () {
        return this.soundManager;
    }

    /**
     * check, if game is in dev mode
     *
     * @return true, if game is in dev mode
     */
    public boolean isDevMode () {
        return getGeneralPreferences().getBoolean("dev_mode", false);
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
        //save all preferences
        for (Map.Entry<String,GamePreferences> entry : this.prefsMap.entrySet()) {
            //check, if preferences can be saved
            if (entry.getValue().canSave()) {
                //save preferences
                entry.getValue().flush();
            }
        }

        //shutdowwn exector service
        this.executorService.shutdown();

        this.destroyGame();
    }

}
