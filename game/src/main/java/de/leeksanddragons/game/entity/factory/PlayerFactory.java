package de.leeksanddragons.game.entity.factory;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.EntityManager;
import de.leeksanddragons.engine.entity.camera.FollowCameraComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.ECS;

/**
 * Created by Justin on 22.09.2017.
 */
public class PlayerFactory {

    public static Entity createPlayer (EntityManager ecs, float x, float y) {
        //create new entity
        Entity player = new Entity(ecs);

        //add position component
        player.addComponent(new PositionComponent(x, y), PositionComponent.class);

        //add camera component, so camera follows player
        player.addComponent(new FollowCameraComponent(), FollowCameraComponent.class);

        return player;
    }

}
