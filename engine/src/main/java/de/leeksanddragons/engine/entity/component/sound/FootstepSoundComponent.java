package de.leeksanddragons.engine.entity.component.sound;

import com.badlogic.gdx.audio.Sound;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.MoveComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.map.IRegion;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.engine.utils.RandomUtils;

/**
 * Created by Justin on 03.10.2017.
 */
public class FootstepSoundComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent (nullable = false)
    protected PositionComponent positionComponent;

    @InjectComponent (nullable = false)
    protected MoveComponent moveComponent;

    //elapsed time in ms
    protected float elapsed = 0;

    protected float timeBetweenSounds = 500;

    //map region
    protected IRegion region = null;

    public FootstepSoundComponent (IRegion region) {
        this.setRegion(region);
    }

    public void setRegion (IRegion region) {
        //check, if region is null
        if (region == null) {
            throw new NullPointerException("region cannot be null.");
        }

        this.region = region;
    }

    @Override
    protected void onInit(IScreenGame game, Entity entity) {

    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //we dont need to play footstep sounds, if player isnt moving
        if (!moveComponent.isMoving()) {
            this.elapsed = 0;

            return;
        }

        //calculate elapsed time
        this.elapsed += time.getDeltaTime() * 1000f;

        if (this.elapsed <= this.timeBetweenSounds) {
            //we dont have to play sound yet

            return;
        }

        //get player middle position
        float middleX = positionComponent.getMiddleX();
        float middleY = positionComponent.getMiddleY();

        //get sound
        Sound sound = this.region.getCurrentMap(game.getCameraManager().getMainCamera()).getFootstepSound(middleX, middleY);

        if (sound != null) {
            //calculate random volume
            float volume = RandomUtils.randomFloat(0.8f, 1f) * game.getSoundManager().getSoundVolume();

            //calculate random pitch
            float pitch = RandomUtils.randomFloat(0.8f, 1.1f);

            //play sound
            long soundID = sound.play(volume, pitch, 0);
            sound.setLooping(soundID, false);
        }

        //reset elapsed time
        this.elapsed = 0;
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.VERY_LOW;
    }

    @Override
    public void dispose() {

    }

}
