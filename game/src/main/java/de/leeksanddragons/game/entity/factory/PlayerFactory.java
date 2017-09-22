package de.leeksanddragons.game.entity.factory;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.EntityManager;
import de.leeksanddragons.engine.entity.impl.ECS;

/**
 * Created by Justin on 22.09.2017.
 */
public class PlayerFactory {

    public static Entity createPlayer (EntityManager ecs, float x, float y) {
        //create new entity
        Entity player = new Entity(ecs);

        return player;
    }

}
