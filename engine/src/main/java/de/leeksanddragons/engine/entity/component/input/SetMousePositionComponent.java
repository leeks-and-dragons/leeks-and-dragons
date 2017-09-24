package de.leeksanddragons.engine.entity.component.input;

import com.badlogic.gdx.math.Vector3;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Component to set entity position to mouse position
 *
 * Created by Justin on 10.03.2017.
 */
public class SetMousePositionComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent(nullable = false)
    protected PositionComponent positionComponent;

    public SetMousePositionComponent() {
        //
    }

    @Override
    public void onInit (IScreenGame game, Entity entity) {
        //
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //get camera
        CameraHelper camera = game.getMainCamera();

        //get mouse position
        Vector3 mousePos = camera.getMousePosition();

        //set mouse position
        this.positionComponent.setMiddlePosition(mousePos.x, mousePos.y);
    }

    @Override public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.HIGH;
    }

    @Override
    public void dispose() {
        //
    }

}
