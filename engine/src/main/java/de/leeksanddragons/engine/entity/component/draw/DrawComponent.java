package de.leeksanddragons.engine.entity.component.draw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.listener.TextureChangedListener;
import de.leeksanddragons.engine.entity.listener.TextureRegionChangedListener;
import de.leeksanddragons.engine.entity.priority.ECSDrawPriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 24.09.2017.
 */
public class DrawComponent extends BaseDrawComponent {

    //texture / texture region
    protected Texture texture = null;
    protected TextureRegion region = null;

    //flag, if texture / texture region is managed by asset manager
    protected boolean isManaged = true;

    public enum MODE {
        TEXTURE, TEXTURE_REGION
    }

    //draw mode
    protected MODE mode = MODE.TEXTURE_REGION;

    //list with listeners, which will be notified, if texture region was changed
    protected List<TextureChangedListener> textureChangedListenerList = new ArrayList<>();

    //list with listeners, which will be notified, if texture region was changed
    protected List<TextureRegionChangedListener> textureRegionChangedListenerList = new ArrayList<>();

    /**
    * default constructor
     *
     * @param texture instance of texture
    */
    public DrawComponent (Texture texture) {
        if (texture == null) {
            throw new NullPointerException("texture cannot be null.");
        }

        if (!texture.isManaged()) {
            throw new IllegalStateException("texture isn't loaded.");
        }

        //set texture and texture mode
        setTexture(texture, true);
    }

    /**
    * default constructor
     *
     * @param region instance of texture region
    */
    public DrawComponent (TextureRegion region) {
        //
    }

    @Override
    public void afterInit(IScreenGame game, Entity entity) {
        if (mode == MODE.TEXTURE) {
            // set new width and height of entity
            positionComponent.setDimension(texture.getWidth(), texture.getHeight());

            // set dimension of texture
            setDimension(texture.getWidth(), texture.getHeight());
        } else if (mode == MODE.TEXTURE_REGION) {
            if (this.region != null) {
                // set new width and height of entity
                positionComponent.setDimension(region.getRegionWidth(), region.getRegionHeight());

                setDimension(region.getRegionWidth(), region.getRegionHeight());
            }
        } else {
            throw new UnsupportedOperationException("mode " + mode.name() + " isnt supported yet.");
        }
    }

    /**
    * set texture
     *
     * @param texture texture to draw
    */
    public void setTexture (Texture texture, boolean setNewDimension) {
        //first, save old texture for listeners
        Texture oldTexture = this.texture;

        this.texture = texture;
        mode = MODE.TEXTURE;

        if (oldTexture == this.texture) {
            // we dont need to notify listeners
            return;
        }

        if (this.texture != null) {
            // update dimension
            this.setDimension(texture.getWidth(), texture.getHeight());

            if (setNewDimension) {
                // update width and height
                this.positionComponent.setDimension(texture.getWidth(), texture.getHeight());

                if (this.texture != null) {
                    // update dimension
                    this.width = this.texture.getWidth();
                    this.height = this.texture.getHeight();
                }
            }
        }

        this.textureChangedListenerList.stream().forEach(listener -> {
            listener.onTextureChanged(oldTexture, this.texture);
        });
    }

    /**
    * set texture region
     *
     * @param region texture region to draw
    */
    public void setTextureRegion (TextureRegion region, boolean setNewDimension) {
        //first, save old texture region for listeners
        TextureRegion oldTextureRegion = this.region;

        if (oldTextureRegion == region) {
            // we dont need to notify listeners
            return;
        }

        this.region = region;
        this.mode = MODE.TEXTURE_REGION;

        if (this.region != null) {
            // update width and height
            this.setDimension(region.getRegionWidth(), region.getRegionHeight());

            if (setNewDimension) {
                // set new width and height
                this.positionComponent.setDimension(region.getRegionWidth(), region.getRegionHeight());
            }


        }

        this.textureRegionChangedListenerList.stream().forEach(listener -> {
            listener.onTextureRegionChanged(oldTextureRegion, this.region);
        });
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        if (mode == MODE.TEXTURE) {
            // if no texture is set, we don't have to draw anything
            if (this.texture == null) {
                return;
            }

            // only draw texture, if entity is visible
            if (this.visible) {
                // draw texture
                batch.draw(this.texture,
                        this.positionComponent.getX() + this.getPaddingLeft()/* - originX */,
                        this.positionComponent.getY() + this.getPaddingBottom()/* - originY */, originX, originY, getWidth(), getHeight(), scaleX,
                        scaleY, angle, 0, // srcX
                        0, // srcY
                        this.texture.getWidth(), this.texture.getHeight(), false, false);
            }
        } else if (mode == MODE.TEXTURE_REGION) {
            if (this.region == null) {
                return;
            }

            if (this.visible) {
                // draw texture region
                batch.draw(this.region,
                        this.positionComponent.getX()/* - getOriginX() */,
                        this.positionComponent.getY()/* - getOriginY() */, getOriginX(), getOriginY(), getWidth(), getHeight(),
                        scaleX, scaleY, getRotationAngle());
            }
        } else {
            throw new UnsupportedOperationException("mode " + mode.name() + " isnt supported yet.");
        }
    }

    public void addTextureChangedListener(TextureChangedListener listener) {
        this.textureChangedListenerList.add(listener);
    }

    public void removeTextureChangedListener(TextureChangedListener listener) {
        this.textureChangedListenerList.remove(listener);
    }

    public void addTextureRegionChangedListener(TextureRegionChangedListener listener) {
        this.textureRegionChangedListenerList.add(listener);
    }

    public void removeTextureRegionChangedListener(TextureRegionChangedListener listener) {
        this.textureRegionChangedListenerList.remove(listener);
    }

    @Override
    public ECSDrawPriority getDrawOrder() {
        return ECSDrawPriority.NORMAL;
    }

    @Override
    public void dispose() {
        if (!isManaged) {
            if (texture != null) {
                texture.dispose();
            }

            if (region != null) {
                region.getTexture().dispose();
            }
        }
    }

}
