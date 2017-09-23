package de.leeksanddragons.engine.entity.component.utils;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Component, which auto removes entity after an given TTL time.
 *
 * Created by Justin on 09.03.2017.
 */
public abstract class TimedAutoRemoveComponent extends BaseComponent implements IUpdateComponent {

    protected long startTime = 0;
    protected long ttl = 0;

    public TimedAutoRemoveComponent(long TTL) {
        this.ttl = TTL;
    }

    @Override
    protected void onInit(IScreenGame game, Entity entity) {
        //
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        if (startTime == 0) {
            startTime = time.getTime();
        }

        // calculate elapsed time
        long elapsed = time.getTime() - startTime;

        if (elapsed > this.ttl) {
            // auto remove entity on next gameloop cycle
            game.runOnUIThread(() -> {
                // remove entity from ecs
                this.entity.getEntityComponentSystem().removeEntity(this.entity);
            });
        }
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.NORMAL;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTTL() {
        return this.ttl;
    }

    public void setTTL(long ttl) {
        this.ttl = ttl;
    }

}
