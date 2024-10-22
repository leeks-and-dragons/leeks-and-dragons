package de.leeksanddragons.game.entity.factory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.leeksanddragons.engine.character.ICharacter;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.EntityManager;
import de.leeksanddragons.engine.entity.component.MoveComponent;
import de.leeksanddragons.engine.entity.component.camera.FollowCameraComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.component.collision.MapCollisionComponent;
import de.leeksanddragons.engine.entity.component.collision.MoveBoundingBoxComponent;
import de.leeksanddragons.engine.entity.component.draw.AtlasAnimationComponent;
import de.leeksanddragons.engine.entity.component.draw.DrawComponent;
import de.leeksanddragons.engine.entity.component.draw.SetAnimationByDirectionComponent;
import de.leeksanddragons.engine.entity.component.input.SetMousePositionComponent;
import de.leeksanddragons.engine.entity.component.sound.FollowSoundPanoramaComponent;
import de.leeksanddragons.engine.entity.component.sound.FootstepSoundComponent;
import de.leeksanddragons.engine.map.IRegion;
import de.leeksanddragons.game.entity.component.InputMoveComponent;

/**
 * Created by Justin on 22.09.2017.
 */
public class PlayerFactory {

    public static Entity createPlayer (EntityManager ecs, ICharacter character, IRegion region, float x, float y) {
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
        player.addComponent(new AtlasAnimationComponent(character.getAtlasPath(), "standingDown", 0.1f), AtlasAnimationComponent.class);

        //add movement component, so entity can be moved
        player.addComponent(new MoveComponent(0, 0, character.getBaseSpeed()), MoveComponent.class);

        //add component to set animation dependend by move direction
        player.addComponent(new SetAnimationByDirectionComponent(), SetAnimationByDirectionComponent.class);

        //add component for bounding box
        player.addComponent(new MoveBoundingBoxComponent(), MoveBoundingBoxComponent.class);

        //add component for map collisions
        player.addComponent(new MapCollisionComponent(region), MapCollisionComponent.class);

        //add component to play footstep sounds
        player.addComponent(new FootstepSoundComponent(region), FootstepSoundComponent.class);

        //add component to convert input --> movement
        player.addComponent(new InputMoveComponent(), InputMoveComponent.class);

        return player;
    }

}
