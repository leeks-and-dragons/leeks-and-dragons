package de.leeksanddragons.engine.listener;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Justin on 04.10.2017.
 */
@FunctionalInterface
public interface BeforeMoveListener {

    /**
    * method is executed, before entity is moved
     *
     * @param oldPosition old position of entity
     * @param newPosition new position of entity
     *
     * @return new position of entity
    */
    public Vector2 beforeMove (Vector2 oldPosition, Vector2 newPosition);

}
