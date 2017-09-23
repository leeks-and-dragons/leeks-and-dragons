package de.leeksanddragons.engine.entity.component.draw;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IDrawComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.screen.IScreenGame;

/**
 * Created by Justin on 23.09.2017.
 */
public abstract class BaseDrawComponent extends BaseComponent implements IDrawComponent {

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

    /**
    * get origin x position of texture / texture region
     *
     * @return origin x position of texture / texture region
    */
    public float getOriginX() {
        return this.originX;
    }

    /**
     * get origin y position of texture / texture region
     *
     * @return origin y position of texture / texture region
     */
    public float getOriginY() {
        return this.originY;
    }

    /**
    * set origin position of texture / texture region
     *
     * @param originX origin x
     * @param originY origin y
    */
    public void setOrigin(float originX, float originY) {
        this.originX = originX;
        this.originY = originY;
    }

    /**
    * get padding left
     *
     * @return padding left
    */
    public float getPaddingLeft () {
        return this.paddingLeft;
    }

    /**
     * get padding bottom
     *
     * @return padding bottom
     */
    public float getPaddingBottom () {
        return this.paddingBottom;
    }

    /**
    * set padding
     *
     * @param paddingLeft padding left
     * @param paddingBottom padding bottom
    */
    public void setPadding (float paddingLeft, float paddingBottom) {
        this.paddingLeft = paddingLeft;
        this.paddingBottom = paddingBottom;
    }

    /**
    * check, if entity is visible
     *
     * @return true, if entity is visible
    */
    public boolean isVisible() {
        return this.visible;
    }

    /**
    * set flag, if entity is visible
     *
     * @param visible flag, if entity is visible
    */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
    * get horizontal scale factor
     *
     * @return horizontal scale factor
    */
    public float getScaleX() {
        return this.scaleX;
    }

    /**
     * get vertical scale factor
     *
     * @return vertical scale factor
     */
    public float getScaleY() {
        return this.scaleY;
    }

    /**
    * set scale factor
     *
     * @param scale scale factor
    */
    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
    }

    /**
    * get rotation angle in degree
     *
     * @return rotation angle in degree
    */
    public float getRotationAngle() {
        return this.angle % 360;
    }

    /**
    * set rotation angle in degree
     *
     * @param angle rotation angle in degree
    */
    public void setRotationAngle(float angle) {
        // normalize angle
        if (angle < 0) {
            float abs = Math.abs(angle);

            int i = (int) abs / 360;
            angle += i * 360;
            angle += 360;
        }

        // set angle
        this.angle = angle % 360;
    }

}
