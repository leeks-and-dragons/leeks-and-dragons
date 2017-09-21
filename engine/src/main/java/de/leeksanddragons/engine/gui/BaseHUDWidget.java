package de.leeksanddragons.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.engine.utils.SpriteBatcherUtils;

/**
 * Created by Justin on 08.02.2017.
 */
public abstract class BaseHUDWidget<T extends HUDWidget> implements HUDWidget<T> {

    protected float x = 0;
    protected float y = 0;
    protected float width = 100;
    protected float height = 100;

    protected float groupX = 0;
    protected float groupY = 0;

    protected CustomRenderer<T> customRenderer = null;

    @Override
    public void drawLayer1(IScreenGame game, GameTime time, ShapeRenderer shapeRenderer) {

    }

    @Override
    public void drawLayer2(IScreenGame game, GameTime time, SpriteBatch batch) {

    }

    public float getX() {
        return this.x + groupX;
    }

    public float getY() {
        return this.y + groupY;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setDimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void onMoveGroup(float groupX, float groupY) {
        this.groupX = groupX;
        this.groupY = groupY;
    }

    protected boolean isInner(float mouseX, float mouseY) {
        if (mouseX >= getX() && mouseX <= (getX() + getWidth())) {
            if (mouseY >= getY() && mouseY <= (getY() + getHeight())) {
                return true;
            }
        }

        return false;
    }

    protected boolean isMouseInner(IScreenGame game) {
        // get relation between current window size and original viewport size
        float a = Gdx.graphics.getWidth() / /*1280f*/800;//game.getCameraManager().getUICamera().getViewportWidth();
        float b = Gdx.graphics.getHeight() / /*720f*/600;//game.getCameraManager().getUICamera().getViewportHeight();

        //get mouse position
        Vector3 mousePos = game.getCameraManager().getUICamera().getMousePosition();

        // get mouse coordinates
        float mouseX = mousePos.x;//Gdx.input.getX();// / a;
        float mouseY = mousePos.y;//game.getCameraManager().getUICamera().getViewportHeight() - (Gdx.input.getY() / b);

        return isInner(mouseX, mouseY);
    }

    public void setCustomRenderer (CustomRenderer<T> renderer) {
        this.customRenderer = renderer;
    }

    @Override
    public void dispose() {
        //
    }

}
