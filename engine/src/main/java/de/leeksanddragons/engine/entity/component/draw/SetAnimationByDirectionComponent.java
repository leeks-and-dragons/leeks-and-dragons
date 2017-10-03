package de.leeksanddragons.engine.entity.component.draw;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.MoveComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 25.09.2017.
 */
public class SetAnimationByDirectionComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent(nullable = false)
    protected MoveComponent moveComponent;

    @InjectComponent(nullable = false)
    protected AtlasAnimationComponent atlasAnimationComponent;

    protected String lastDirection = "";

    public SetAnimationByDirectionComponent () {
        this.lastDirection = "Down";
    }

    @Override
    protected void onInit(IScreenGame game, Entity entity) {

    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //get direction

        String direction = "";

        if (moveComponent.getDirection().x < 0) {
            direction += "Left";
        } else if (moveComponent.getDirection().x > 0) {
            direction += "Right";
        }

        if (moveComponent.getDirection().y < 0) {
            direction += "Down";
        } else if (moveComponent.getDirection().y > 0) {
            direction += "Up";
        }

        if (direction.isEmpty()) {
            direction = this.lastDirection;
        }

        String animationName = "";

        if (moveComponent.isMoving()) {
            animationName += "running";
        } else {
            animationName += "standing";
        }

        animationName += direction;

        //set last direction
        this.lastDirection = direction;

        if (atlasAnimationComponent.isLoaded()) {
            //set animation
            atlasAnimationComponent.setCurrentAnimationName(animationName);
        }
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.LOW;
    }

    @Override
    public void dispose() {

    }

}
