package de.leeksanddragons.engine.entity.component.draw;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.screen.IScreenGame;

/**
 * Created by Justin on 23.09.2017.
 */
public abstract class BaseDrawComponent extends BaseComponent {

    @InjectComponent(nullable = false)
    protected PositionComponent positionComponent = null;

    //origin position of texture / texture region
    protected float originX = 0;
    protected float originY = 0;

    //width and height of texture / texture region
    protected float width = 0;
    protected float height = 0;

    //scale of texture / texture region
    protected float scaleX = 1f;
    protected float scaleY = 1f;

    //rotation angle of texture / texture region
    protected float angle = 0;

    //padding of texture / texture region
    protected float paddingLeft = 0;
    protected float paddingBottom = 0;

    //flag, if entity is visible
    protected boolean visible = true;

    @Override
    protected final void onInit(IScreenGame game, Entity entity) {
        afterInit(game, entity);
    }

    /**
    * method to initialize draw component
     *
     * @param game instance of game
     * @param entity owner entity of component
    */
    public abstract void afterInit(IScreenGame game, Entity entity);

    /**
    * get width of texture / texture region
     *
     * @return width of texture / texture region
    */
    public float getWidth() {
        return this.width;
    }

    /**
    * get height of texture / texture region
     *
     * @return height of texture / texture region
    */
    public float getHeight() {
        return this.height;
    }

    /**
    * set dimension (size) of texture / texture region to draw
     *
     * @param width width of texture / texture region
     * @param height height of texture / texture region
    */
    public void setDimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

}
