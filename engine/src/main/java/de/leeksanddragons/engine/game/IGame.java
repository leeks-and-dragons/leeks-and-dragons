package de.leeksanddragons.engine.game;

import de.leeksanddragons.engine.camera.ResizeListener;
import de.leeksanddragons.engine.camera.manager.CameraManager;

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

}
