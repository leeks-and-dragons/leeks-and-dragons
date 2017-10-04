package de.leeksanddragons.engine.entity.component.collision;

import com.badlogic.gdx.math.Vector2;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.MoveComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.listener.BeforeMoveListener;
import de.leeksanddragons.engine.map.IRegion;
import de.leeksanddragons.engine.screen.IScreenGame;

/**
 * Created by Justin on 04.10.2017.
 */
public class MapCollisionComponent extends BaseComponent implements BeforeMoveListener {

    @InjectComponent (nullable = false)
    protected PositionComponent positionComponent;

    @InjectComponent (nullable = false)
    protected MoveComponent moveComponent;

    @InjectComponent (nullable = false)
    protected MoveBoundingBoxComponent moveBoundingBoxComponent;

    //instance of region
    protected IRegion region = null;

    /**
    * default constructor
     *
     * @param region instance of region
    */
    public MapCollisionComponent (IRegion region) {
        //set region
        this.setRegion(region);
    }

    @Override
    protected void onInit(IScreenGame game, Entity entity) {
        //register move listener
        this.moveComponent.addBeforeMoveListener(this);
    }

    public void setRegion (IRegion region) {
        if (region == null) {
            throw new NullPointerException("region cannot be null.");
        }

        this.region = region;
    }

    @Override
    public Vector2 beforeMove(Vector2 oldPosition, Vector2 newPosition, PositionComponent positionComponent) {
        //calculate speed of entity
        float deltaX = newPosition.x - oldPosition.x;
        float deltaY = newPosition.y - oldPosition.y;

        if (deltaX < 0) {
            //TODO: check left top and left bottom corner
        } else if (deltaX > 0) {
            //TODO: check right top and right bottom corner
        }

        return newPosition;
    }

    @Override
    public void dispose() {
        //remove move listener
        this.moveComponent.removeBeforeMoveListener(this);
    }

}
