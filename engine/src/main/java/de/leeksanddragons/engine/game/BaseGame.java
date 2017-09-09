package de.leeksanddragons.engine.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import de.leeksanddragons.engine.camera.ResizeListener;
import de.leeksanddragons.engine.camera.manager.CameraManager;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Justin on 09.09.2017.
 */
public class BaseGame extends ApplicationAdapter {

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

    @Override
    public void create () {
        //
    }

    @Override
    public void resize (int width, int height) {
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
    public void addResizeListener(ResizeListener listener) {
        this.resizeListeners.add(listener);
    }

    /**
     * remove resize listener
     *
     * @param listener resize listener which is called, if window was resized
     */
    public void removeResizeListener(ResizeListener listener) {
        this.resizeListeners.remove(listener);
    }

    @Override
    public void render () {
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
    }

    /**
    * get frames per second count
     *
     * @return FPS count
    */
    public int getFPS() {
        return Gdx.graphics.getFramesPerSecond();
    }

    /**
    * get camera manager
     *
     * @return instance of camera manager
    */
    public CameraManager getCameraManager () {
        return this.cameraManager;
    }

    /**
    * execute runnable on next render cycle in UI thread
     *
     * @param runnable runnable to execute in UI thread
    */
    public void runOnUIThread(Runnable runnable) {
        this.uiQueue.offer(runnable);
    }

    @Override
    public void pause () {
        //
    }

    @Override
    public void resume () {
        //
    }

    @Override
    public void dispose () {
        //
    }

}
