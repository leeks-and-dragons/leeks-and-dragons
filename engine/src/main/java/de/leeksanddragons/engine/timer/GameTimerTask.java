package de.leeksanddragons.engine.timer;

import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 12.09.2017.
 */
public class GameTimerTask {

    protected long startTime = 0;

    //delay time before executing task
    protected long delay = 0;

    //runnable to execute after an given time
    protected Runnable runnable = null;

    public GameTimerTask(long delay, Runnable runnable) {
        this.delay = delay;
        this.runnable = runnable;
    }

    public void start () {
        //save current time as start time
        this.startTime = System.currentTimeMillis();
    }

    public long getElapsedTime () {
        long now = System.currentTimeMillis();

        return now - this.startTime;
    }

    public boolean isDelayReached () {
        return this.delay <= this.getElapsedTime();
    }

    public void execute () {
        this.runnable.run();
    }

}
