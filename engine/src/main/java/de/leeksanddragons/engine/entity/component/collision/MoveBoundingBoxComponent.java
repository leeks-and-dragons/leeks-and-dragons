package de.leeksanddragons.engine.entity.component.collision;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IDrawComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.listener.DimensionChangedListener;
import de.leeksanddragons.engine.entity.priority.ECSDrawPriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.engine.utils.SpriteBatcherUtils;

/**
 * Created by Justin on 04.10.2017.
 */
public class MoveBoundingBoxComponent extends BaseComponent implements IDrawComponent, DimensionChangedListener {

    @InjectComponent (nullable = false)
    protected PositionComponent positionComponent;

    //flag, if dimension should be injust, if texture size was changed
    protected boolean adjustDimension = true;

    //padding
    protected float paddingLeft = 0;
    protected float paddingBottom = 0;

    //width and height of bounding box
    protected float width = 0;
    protected float height = 0;

    //flag, if bounding box should be drawn
    protected boolean drawBoundingBox = true;
    protected Color boundingBoxColor = Color.YELLOW;

    //bounding box rectangle
    protected Rectangle rectangle = new Rectangle(0, 0, 0, 0);

    public MoveBoundingBoxComponent () {
        //
    }

    @Override
    protected void onInit(IScreenGame game, Entity entity) {
        if (adjustDimension) {
            //add dimension changed listener
            positionComponent.addDimensionChangedListener(this);

            //get width and height of entity
            this.width = positionComponent.getWidth();
            this.height = positionComponent.getHeight();
        }
    }

    @Override
    public void onDimensionChanged(float oldWidth, float oldHeight, float newWidht, float newHeight) {
        if (adjustDimension) {
            //update width and height of bounding box
            this.width = newWidht;
            this.height = newHeight;
        }
    }

    public float getX () {
        return positionComponent.getX() + this.paddingLeft;
    }

    public float getY () {
        return positionComponent.getY() + this.paddingBottom;
    }

    public float getWidth () {
        return this.width;
    }

    public float getHeight () {
        return this.height;
    }

    public Rectangle getRectangle () {
        this.rectangle.set(getX(), getY(), getWidth(), getHeight());

        return this.rectangle;
    }

    @Override
    public void dispose() {
        positionComponent.removeDimensionChangedListener(this);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        if (this.drawBoundingBox) {
            SpriteBatcherUtils.drawRectangle(batch, getX(), getY(), getWidth(), getHeight(), this.boundingBoxColor);
        }
    }

    @Override
    public ECSDrawPriority getDrawOrder() {
        return ECSDrawPriority.VERY_LOW;
    }

}
