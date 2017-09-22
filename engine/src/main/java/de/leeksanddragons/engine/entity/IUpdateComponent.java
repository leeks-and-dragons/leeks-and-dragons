package de.leeksanddragons.engine.entity;

import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 10.02.2017.
 */
public interface IUpdateComponent {

    /**
     * update component
     *
     * @param game instance of game
     * @param time game time
     */
    public void update(IScreenGame game, GameTime time);

    /**
    * get update order
     *
     * @return update order
    */
    public ECSUpdatePriority getUpdateOrder();

}
