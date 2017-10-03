package de.leeksanddragons.engine.entity.component.sound;

import com.badlogic.gdx.audio.Sound;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.map.IRegion;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 03.10.2017.
 */
public class FootstepSoundComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent (nullable = false)
    protected PositionComponent positionComponent;

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
        //get player middle position
        float middleX = positionComponent.getMiddleX();
        float middleY = positionComponent.getMiddleY();

        //get sound
        Sound sound = this.region.getCurrentMap(game.getCameraManager().getMainCamera()).getFootstepSound(middleX, middleY);

        if (sound != null) {
            //System.out.println("sound found.");
        }
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.VERY_LOW;
    }

    @Override
    public void dispose() {

    }

}
