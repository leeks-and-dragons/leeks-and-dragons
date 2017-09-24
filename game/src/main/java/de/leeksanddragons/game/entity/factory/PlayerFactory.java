package de.leeksanddragons.game.entity.factory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.EntityManager;
import de.leeksanddragons.engine.entity.component.MoveComponent;
import de.leeksanddragons.engine.entity.component.camera.FollowCameraComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.component.draw.AtlasAnimationComponent;
import de.leeksanddragons.engine.entity.component.draw.DrawComponent;
import de.leeksanddragons.engine.entity.component.input.SetMousePositionComponent;
import de.leeksanddragons.engine.entity.component.sound.FollowSoundPanoramaComponent;

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

        //add sound panorama component, so sound panorama follows player
        player.addComponent(new FollowSoundPanoramaComponent(), FollowSoundPanoramaComponent.class);

        //add draw component to draw player
        player.addComponent(new DrawComponent((TextureRegion) null), DrawComponent.class);

        //add atlas animation component, so player can be animated
        player.addComponent(new AtlasAnimationComponent("./mods/maingame/character/cedric/cedric.atlas", "standingDown", 200f), AtlasAnimationComponent.class);

        //add movement component, so entity can be moved
        player.addComponent(new MoveComponent(0, 0, 1), MoveComponent.class);

        return player;
    }

}
