package de.leeksanddragons.engine.listener;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Justin on 04.10.2017.
 */
@FunctionalInterface
public interface AfterMoveListener {

    /**
    * method which is executed after entity was moved
     *
     * @param oldPosition old position of entity
     * @param newPosition new position of entity
    */
    public void afterMove (Vector2 oldPosition, Vector2 newPosition);

}
