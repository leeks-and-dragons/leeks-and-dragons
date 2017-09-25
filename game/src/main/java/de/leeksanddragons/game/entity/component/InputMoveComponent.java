package de.leeksanddragons.game.entity.component;

import com.badlogic.gdx.math.Vector2;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.MoveComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.engine.utils.MouseUtils;
import de.leeksanddragons.game.input.DefaultInputMapper;

/**
 * Component converts input --> movement
 *
 * Created by Justin on 25.09.2017.
 */
public class InputMoveComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent(nullable = false)
    protected PositionComponent positionComponent;

    @InjectComponent(nullable = false)
    protected MoveComponent moveComponent;

    //input mapper
    protected DefaultInputMapper inputMapper = null;

    @Override
    protected void onInit(IScreenGame game, Entity entity) {
        //get input mapper
        this.inputMapper = game.getInputManager().getInputMapper(DefaultInputMapper.class);
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        float x = 0;
        float y = 0;

        if (!this.inputMapper.isMouseMoving()) {
            if (inputMapper.isMovingDown()) {
                y = -1;
            } else if (inputMapper.isMovingUp()) {
                y = 1;
            }

            if (inputMapper.isMovingLeft()) {
                x = -1;
            } else if (inputMapper.isMovingRight()) {
                x = 1;
            }
        } else {
            //TODO: get mouse direction

            Vector2 vec = MouseUtils.getRelativePositionToEntity(game.getCameraManager().getMainCamera(), positionComponent.getMiddleX(), positionComponent.getMiddleY());
            x = vec.x;
            y = vec.y;
        }

        //set direction
        moveComponent.setDirection(x, y);
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.VERY_HIGH;
    }

    @Override
    public void dispose() {

    }

}
