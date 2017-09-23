package de.leeksanddragons.engine.entity.component.sound;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Component which is responsible for following sound panorama
 *
 * Created by Justin on 23.09.2017.
 */
public class FollowSoundPanoramaComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent(nullable = false)
    protected PositionComponent positionComponent;

    @Override
    protected void onInit(IScreenGame game, Entity entity) {

    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //set sound target position
        game.getSoundManager().setTargetPos(positionComponent.getMiddleX(), positionComponent.getMiddleY());
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.VERY_LOW;
    }

    @Override
    public void dispose() {

    }
}
