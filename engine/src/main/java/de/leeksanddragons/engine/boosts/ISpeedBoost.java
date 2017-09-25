package de.leeksanddragons.engine.boosts;

import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 26.09.2017.
 */
public interface ISpeedBoost {

    /**
    * update boost
    */
    public void update (GameTime time);

    public float getFlatBonusSpeed ();

    public float getPercentageBonusSpeed ();

    public boolean canRemoved ();

}
