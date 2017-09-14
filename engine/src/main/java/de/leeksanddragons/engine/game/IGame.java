package de.leeksanddragons.engine.game;

import de.leeksanddragons.engine.camera.ResizeListener;
import de.leeksanddragons.engine.camera.manager.CameraManager;
import de.leeksanddragons.engine.data.SharedData;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.preferences.IPreferences;

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
    public IPreferences getGeneralPreferences ();

    /**
    * get preferences by category
     *
     * @return instance of preferences by category
    */
    public IPreferences getPreferences (String category);

    /**
    * get instance of shared data (non-persistent data, only available on runtime)
     *
     * @return instance of shared data
    */
    public SharedData getSharedData ();

    /**
    * add an timer task which will be executed once after an given time in milliseconds
     *
     * @param delay time in millis to wait, before executing task
     * @param runnable timer task
    */
    public void addTimerTask (long delay, Runnable runnable);

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
