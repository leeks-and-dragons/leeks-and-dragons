package de.leeksanddragons.engine.entity.component.draw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.AnimationTextureUtils;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * An basic animation with only one animation state
 *
 * Created by Justin on 11.02.2017.
 */
public class BasicAnimationComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent(nullable = false)
    protected DrawComponent textureRegionComponent = null;

    //animation
    protected Animation<TextureRegion> animation = null;

    //spritesheet rows and cols count
    protected int rows = 0;
    protected int cols = 0;

    // https://github.com/libgdx/libgdx/wiki/2D-Animation

    //elapsed time to calculate current animation framee
    protected float elapsed = 0;

    //current animation frame
    protected TextureRegion currentFrame = null;

    public BasicAnimationComponent(Texture texture, float duration, int rows, int cols) {
        if (texture == null) {
            throw new NullPointerException("texture cannot be null.");
        }

        if (!texture.isManaged()) {
            throw new IllegalStateException("texture isnt managed or wasnt loaded.");
        }

        this.rows = rows;
        this.cols = cols;

        // create animation
        this.animation = AnimationTextureUtils.createAnimationFromTexture(texture, duration / 1000, rows, cols);
    }

    @Override
    public void onInit(IScreenGame game, Entity entity) {
        super.init(game, entity);

        // set current frame
        this.currentFrame = this.animation.getKeyFrame(1);
        this.textureRegionComponent.setTextureRegion(this.currentFrame, true);
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        // calculate elapsed time in milliseconds
        this.elapsed += time.getDeltaTime();

        // get current frame of animation
        TextureRegion region = animation.getKeyFrame(elapsed, true);

        if (this.currentFrame != region) {
            //set texture region
            this.textureRegionComponent.setTextureRegion(this.currentFrame, true);
        }

        this.currentFrame = region;
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.NORMAL;
    }

    public TextureRegion getCurrentFrame() {
        return this.currentFrame;
    }

    @Override
    public void dispose() {
        this.animation = null;
    }
}
