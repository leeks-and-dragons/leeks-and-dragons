package de.leeksanddragons.engine.entity.component.draw;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IDrawComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSDrawPriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 09.03.2017.
 */
public class DrawParticlesComponent extends BaseComponent implements IDrawComponent {

    @InjectComponent(nullable = false)
    protected PositionComponent positionComponent = null;

    //particles padding
    protected float paddingX = 0;
    protected float paddingY = 0;

    //particle effect
    protected ParticleEffect particleEffect = null;

    //path to particle effect
    protected String particleEffectFile = "";

    //flag, if particle effect is looping
    protected boolean looping = false;

    //flag, if particle effect should start automatically
    protected boolean autoStart = false;

    /**
    * default constructor
     *
     * @param particleEffectFile path to particle effect
     * @param looping flag, if particles are looping
     * @param autoStart flag, if particle animation should start automatically
    */
    public DrawParticlesComponent(String particleEffectFile, boolean looping, boolean autoStart) {
        this.particleEffectFile = particleEffectFile;
        this.looping = looping;
        this.autoStart = autoStart;

        // https://github.com/libgdx/libgdx/wiki/2D-ParticleEffects
    }

    @Override
    public void onInit(IScreenGame game, Entity entity) {
        // create new particle effect
        this.particleEffect = new ParticleEffect();

        //load effect
        game.getAssetManager().load(this.particleEffectFile, ParticleEffect.class);
        game.getAssetManager().finishLoadingAsset(this.particleEffectFile);
        this.particleEffect = game.getAssetManager().get(this.particleEffectFile, ParticleEffect.class);

        if (this.autoStart) {
            // start particle effect
            this.particleEffect.start();
        }
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        // loop particle effect
        if (this.particleEffect.isComplete() && looping) {
            this.particleEffect.start();
        }

        //set position
        this.particleEffect.setPosition(positionComponent.getX() + paddingX, positionComponent.getY() + paddingY);

        //draw particle effect
        this.particleEffect.draw(batch, time.getDeltaTime());
    }

    @Override
    public ECSDrawPriority getDrawOrder() {
        return ECSDrawPriority.DRAW_PARTICLES;
    }

    public void startParticleEffect() {
        this.particleEffect.start();
    }

    public void stopParticleEffect() {
        this.particleEffect.reset();
    }

    public boolean isLooping() {
        return this.looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    @Override
    public void dispose() {
        this.game.getAssetManager().unload(this.particleEffectFile);
        this.particleEffect = null;
    }

}
