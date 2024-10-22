package de.leeksanddragons.engine.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.camera.ResizeListener;
import de.leeksanddragons.engine.camera.manager.CameraManager;
import de.leeksanddragons.engine.controller.ControllerManager;
import de.leeksanddragons.engine.data.SharedData;
import de.leeksanddragons.engine.input.InputManager;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.preferences.GamePreferences;
import de.leeksanddragons.engine.preferences.IPreferences;
import de.leeksanddragons.engine.sound.SoundManager;

import java.util.concurrent.ExecutorService;

/**
 * Created by Justin on 11.09.2017.
 */
public interface IGame {

    /**
     * add resize listener
     *
     * @param listener resize listener which is called, if window was resized
     */
    public void addResizeListener(ResizeListener listener);

    /**
     * remove resize listener
     *
     * @param listener resize listener which is called, if window was resized
     */
    public void removeResizeListener(ResizeListener listener);

    /**
     * get frames per second count
     *
     * @return FPS count
     */
    public int getFPS();

    /**
     * get camera manager
     *
     * @return instance of camera manager
     */
    public CameraManager getCameraManager ();

    /**
     * execute runnable on next render cycle in UI thread
     *
     * @param runnable runnable to execute in UI thread
     */
    public void runOnUIThread(Runnable runnable);

    /**
    * get executor service
     *
     * @return instance of executor service
    */
    public ExecutorService getExecutorService ();

    /**
    * get instance of main camera
     *
     * @return instance of main camera
    */
    public CameraHelper getMainCamera ();

    /**
     * get instance of UI camera
     *
     * @return instance of UI camera
     */
    public CameraHelper getUICamera ();

    /**
    * get path to app home dir in user.home/.APPNAME/
     *
     * @return path to app home dir
    */
    public String getAppHomeDir ();

    /**
    * get name of application
     *
     * @return name of application
    */
    public String getAppName ();

    /**
    * get general preferences
     *
     * @return instance of general preferences
    */
    public GamePreferences getGeneralPreferences ();

    /**
    * get preferences by category
     *
     * @return instance of preferences by category
    */
    public GamePreferences getPreferences (String category);

    /**
    * get instance of shared data (non-persistent data, only available on runtime)
     *
     * @return instance of shared data
    */
    public SharedData getSharedData ();

    /**
    * get controller manager
     *
     * @return instance of controller manager
    */
    public ControllerManager getControllerManager ();

    /**
    * get input manager
     *
     * @return instance of input manager
    */
    public InputManager getInputManager ();

    /**
    * get sprite batch. Only use this method, if it is really neccessarry!
     *
     * @return instance of sprite batch
    */
    public SpriteBatch getSpriteBatch ();

    /**
    * add an timer task which will be executed once after an given time in milliseconds
     *
     * @param delay time in millis to wait, before executing task
     * @param runnable timer task
    */
    public void addTimerTask (long delay, Runnable runnable);

    /**
    * check, if sound is muted
     *
     * @return true, if sound is muted
    */
    public boolean isSoundMuted ();

    /**
     * check, if music is muted
     *
     * @return true, if music is muted
     */
    public boolean isMusicMuted ();

    /**
    * get instance of sound manager
     *
     * @return instance of sound manager
    */
    public SoundManager getSoundManager ();

    /**
    * check, if game is in dev mode
     *
     * @return true, if game is in dev mode
    */
    public boolean isDevMode ();

    /**
     * get asset manager
     *
     * @return instance of asset manager
     */
    public GameAssetManager getAssetManager ();

}
