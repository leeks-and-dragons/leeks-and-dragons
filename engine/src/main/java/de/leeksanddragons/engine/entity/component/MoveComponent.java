package de.leeksanddragons.engine.entity.component;

import com.badlogic.gdx.math.Vector2;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 24.09.2017.
 */
public class MoveComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent(nullable = false)
    protected PositionComponent positionComponent = null;

    //movement direction
    protected Vector2 moveDirection = new Vector2(0, 0);

    //movement speed
    protected float speed = 1;

    //flag which indicates if the entity is currently moving.
    protected boolean isMoving = false;

    //temporary vector for calculations
    protected Vector2 tmpVector = new Vector2();

    //pixels per unit
    protected float PIXELS_PER_UNIT = 128;

    /**
    * default constructor
     *
     * @param x x direction
     * @param y y direction
     * @param speed movement speed
    */
    public MoveComponent (float x, float y, float speed) {
        //set direction
        this.setDirection(x, y);

        //set speed
        this.setBaseSpeed(speed);
    }

    @Override
    protected void onInit(IScreenGame game, Entity entity) {

    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        if (!isMoving) {
            // we dont have to move entity
            return;
        }

        //get delta time between 2 frames
        float dt = time.getDeltaTime();

        //set vector values to temporary vector
        tmpVector.set(moveDirection.x, moveDirection.y);

        //normalize and scale move vector
        tmpVector.scl(this.getSpeedInPixels() * dt * 100f);

        //move entity
        positionComponent.move(tmpVector.x, tmpVector.y);
    }

    /**
     * get movement direction
     *
     * @return movement direction
     */
    public Vector2 getDirection () {
        return this.moveDirection;
    }

    /**
    * set movement direction
     *
     * @param x x movement direction
     * @param y y movement direction
    */
    public void setDirection (float x, float y) {
        //set vector
        this.moveDirection.set(x, y);

        if (x == 0 && y == 0) {
            this.isMoving = false;
        } else {
            this.isMoving = true;

            //normalize vector
            this.moveDirection.nor();
        }
    }

    /**
    * stop moving entity
    */
    public void stopMoving () {
        this.setDirection(0, 0);
    }

    /**
    * check, if entity is moving
     *
     * @return true, if entity is moving
    */
    public boolean isMoving () {
        return this.isMoving;
    }

    /**
    * get movement speed
     *
     * @return movement speed
    */
    public float getSpeedInPixels () {
        //TODO: add boost effects

        //TODO: calculate speed in units
        float speedInUnits = this.speed;

        return speedInUnits / PIXELS_PER_UNIT;
    }

    /**
    * set movement speed in units
     *
     * @param speed movement speed
    */
    public void setBaseSpeed(float speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("speed has to be >= 0.");
        }

        this.speed = speed;
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.LOW;
    }

    @Override
    public void dispose() {

    }

}
