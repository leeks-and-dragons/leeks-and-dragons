package de.leeksanddragons.engine.gui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.leeksanddragons.engine.gui.BaseHUDWidget;
import de.leeksanddragons.engine.gui.ClickListener;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.engine.utils.MouseUtils;
import de.leeksanddragons.engine.utils.SpriteBatcherUtils;

/**
 * Created by Justin on 15.09.2017.
 */
public class MenuButton extends BaseHUDWidget<MenuButton> {

    //flags
    protected boolean hovered = false;
    protected boolean isClicked = false;

    //click listener
    protected ClickListener clickListener = null;

    //images to draw
    protected TextureRegion image = null;
    protected TextureRegion hoveredImage = null;

    //font
    protected BitmapFont font = null;

    //button text
    protected String text = "";

    //text padding
    protected float paddingTop = 30;
    protected float paddingLeft = 6;

    //sound, which is playing if player hovers over button
    protected Sound hoverSound = null;

    //sound volume
    protected float volume = 1f;

    public MenuButton(Texture texture, Texture hoveredTexture, BitmapFont font, String buttonText) {
        //get texture regions
        this.image = new TextureRegion(texture);
        this.hoveredImage = new TextureRegion(hoveredTexture);

        //save font
        this.font = font;

        //save text
        this.text = buttonText;

        //set dimension of texture region
        this.setDimension(image.getRegionWidth(), image.getRegionHeight());
    }

    public MenuButton(TextureAtlas textureAtlas, String name, String hoveredName, BitmapFont font, String buttonText) {
        //get texture regions
        this.image = textureAtlas.findRegion(name);
        this.hoveredImage = textureAtlas.findRegion(hoveredName);

        //save font
        this.font = font;

        //save text
        this.text = buttonText;

        //set dimension of texture region
        this.setDimension(image.getRegionWidth(), image.getRegionHeight());
    }

    /**
    * set text padding
     *
     * @param paddingTop padding top
     * @param paddingLeft padding left
    */
    public void setTextPadding (float paddingTop, float paddingLeft) {
        this.paddingTop = paddingTop;
        this.paddingLeft = paddingLeft;
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        // get mouse coordinates
        //float mouseX = Gdx.input.getX();
        //float mouseY = game.getCameraManager().getUICamera().getViewportHeight() - Gdx.input.getY();

        // check if mouse is inner button
        if (isMouseInner(game)) {
            if (!hovered) {
                // mouse enter widget

                // play sound
                if (this.hoverSound != null) {
                    this.hoverSound.play(game.getSoundManager().getSoundVolume() * this.volume);
                }
            }

            this.hovered = true;
        } else {
            hovered = false;
        }

        boolean oldClicked = this.isClicked;

        if (isMouseInner(game) && Gdx.input.isTouched()) {
            this.isClicked = true;
        } else {
            this.isClicked = false;

            // check if user has released button
            if (oldClicked == true) {
                // user has clicked button
                if (clickListener != null) {
                    clickListener.onClick();
                }
            }
        }

        //execute custom renderer, if available
        if (this.customRenderer != null) {
            this.customRenderer.update(game, this, time);
        }
    }

    @Override
    public void drawLayer0(IScreenGame game, GameTime time, SpriteBatch batch) {
        // draw background texture
        if (hovered) {
            batch.draw(this.hoveredImage, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(this.image, getX(), getY(), getWidth(), getHeight());
        }

        //draw text
        this.font.draw(batch, this.text, getX() + paddingLeft, getY() + getHeight() - paddingTop);

        //execute custom renderer, if available
        if (this.customRenderer != null) {
            this.customRenderer.drawLayer0(time, this, batch);
        }

        /*if (hovered) {
            SpriteBatcherUtils.drawRectangle(batch, getX(), getY(), getWidth(), getHeight(), Color.YELLOW);
        } else {
            SpriteBatcherUtils.drawRectangle(batch, getX(), getY(), getWidth(), getHeight(), Color.RED);
        }*/

        //Vector3 vec = MouseUtils.get

        /*Vector2 vec = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        vec = game.getCameraManager().getUICamera().getViewport().unproject(vec);*/

        float mouseX = game.getCameraManager().getUICamera().getMousePosition().x;//Gdx.input.getX();
        float mouseY = game.getCameraManager().getUICamera().getMousePosition().y;//Gdx.graphics.getHeight() - Gdx.input.getY();

        //SpriteBatcherUtils.fillRectangle(batch, mouseX - 2, mouseY - 2, 5, 5, Color.BLUE);
    }

    public void setHoverSound(Sound sound, float soundVolume) {
        this.hoverSound = sound;
        this.volume = soundVolume;
    }

    public void setClickListener(ClickListener listener) {
        this.clickListener = listener;
    }

}
