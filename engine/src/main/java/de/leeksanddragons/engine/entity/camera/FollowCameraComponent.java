package de.leeksanddragons.engine.entity.camera;

import de.leeksanddragons.engine.camera.CameraMode;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 10.02.2017.
 */
public class FollowCameraComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent(nullable = false)
    protected PositionComponent entityPosition = null;

    @Override
    public void onInit(IScreenGame game, Entity entity) {
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //set camera mode
        game.getMainCamera().setMode(CameraMode.DIRECT_CAMERA);

        //set camera middle position to entity
        game.getMainCamera().setTargetMiddlePos(entityPosition.getMiddleX(), entityPosition.getMiddleY());
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        // camera should be updated after all entities
        return ECSUpdatePriority.VERY_LOW;
    }

}
