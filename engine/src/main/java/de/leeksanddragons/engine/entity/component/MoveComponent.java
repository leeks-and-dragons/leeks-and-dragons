package de.leeksanddragons.engine.entity.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.leeksanddragons.engine.boosts.ISpeedBoost;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.listener.AfterMoveListener;
import de.leeksanddragons.engine.listener.BeforeMoveListener;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.List;

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
    protected Vector2 tmpOldPosVector = new Vector2();
    protected Vector2 tmpNewPosVector = new Vector2();

    //pixels per unit
    protected float PIXELS_PER_UNIT = 512;//256;

    //list with all boosts
    protected List<ISpeedBoost> boostList = new ArrayList<>();

    //bounds
    protected float minX = -Float.MAX_VALUE;
    protected float maxX = Float.MAX_VALUE;
    protected float minY = -Float.MAX_VALUE;
    protected float maxY = Float.MAX_VALUE;

    //list with all move listener hooks, for example for collision system
    protected List<BeforeMoveListener> beforeMoveListeners = new ArrayList<>();
    protected List<AfterMoveListener> afterMoveListeners = new ArrayList<>();

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

        //udpate boosts
        for (ISpeedBoost boost : this.boostList) {
            if (boost.canRemoved()) {
                this.boostList.remove(boost);

                Gdx.app.debug("MoveComponent", "removed speed bonus: " + boost.toString());
            }

            boost.update(time);
        }

        //normalize and scale move vector
        tmpVector.scl(this.getSpeedInPixels() * dt * 100f);

        //set old position to temporary vectors
        this.tmpOldPosVector.set(positionComponent.getX(), positionComponent.getY());
        this.tmpNewPosVector.set(positionComponent.getX() + tmpVector.x, positionComponent.getY() + tmpVector.y);

        //call listeners
        this.beforeMoveListeners.forEach((BeforeMoveListener listener) -> {
            this.tmpNewPosVector = listener.beforeMove(this.tmpOldPosVector, this.tmpNewPosVector, this.positionComponent);
        });

        //move entity
        positionComponent.move(tmpVector.x, tmpVector.y);

        //set new position to temporary vector
        this.tmpNewPosVector.set(positionComponent.getX(), positionComponent.getY());

        //call listeners
        this.afterMoveListeners.forEach((AfterMoveListener listener) -> {
            listener.afterMove(this.tmpOldPosVector, this.tmpNewPosVector, this.positionComponent);
        });

        //check, if player is in bounds, if not, correct position
        if (positionComponent.getX() < this.minX) {
            positionComponent.setX(this.minX);
        }

        if (positionComponent.getX() + positionComponent.getWidth() >= this.maxX) {
            positionComponent.setX(this.maxX - positionComponent.getWidth());
        }

        if (positionComponent.getY() < this.minY) {
            positionComponent.setY(this.minY);
        }

        if (positionComponent.getY() + positionComponent.getHeight() >= this.maxY) {
            positionComponent.setY(this.maxY - positionComponent.getHeight());
        }
    }

    /**
    * set bounds, entity should be in
    */
    public void setBounds (float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
    * reset bounds, so bounds arent used
    */
    public void resetBounds () {
        this.minX = -Float.MAX_VALUE;
        this.maxX = Float.MAX_VALUE;
        this.minY = -Float.MAX_VALUE;
        this.maxY = Float.MAX_VALUE;
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
        float flatBonus = 0;
        float percentageBonus = 0;

        for (ISpeedBoost boost : this.boostList) {
            flatBonus += boost.getFlatBonusSpeed();
            percentageBonus += boost.getPercentageBonusSpeed();
        }

        //percentage bonus can have maximum 100%
        percentageBonus = Math.max(1, percentageBonus);

        //calculate speed in units
        float speedInUnits = (this.speed + flatBonus) * (1 + percentageBonus);

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

    public void setPixelsPerUnit (float pixelsPerUnit) {
        this.PIXELS_PER_UNIT = pixelsPerUnit;
    }

    public void addBoost (ISpeedBoost boost) {
        this.boostList.add(boost);
    }

    public void removeBoost (ISpeedBoost boost) {
        this.boostList.remove(boost);
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.NORMAL;
    }

    @Override
    public void dispose() {

    }

}
