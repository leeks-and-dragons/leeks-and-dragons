package de.leeksanddragons.engine.boosts;

import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 26.09.2017.
 */
public class MovementSpeedSpeedBoost implements ISpeedBoost {

    //elapsed time of boost
    protected float elapsed = 0;

    //time to live of boost
    protected long ttl = 0;

    //flat bonus
    protected float flatBonus = 0;

    //percentage bonus
    protected float percentageBonus = 0;

    public MovementSpeedSpeedBoost(long ttl, float flatBonus, float percentageBonus) {
        this.ttl = ttl;
        this.flatBonus = flatBonus;
        this.percentageBonus = percentageBonus;
    }

    @Override
    public void update(GameTime time) {
        this.elapsed += time.getDeltaTime();
    }

    @Override
    public float getFlatBonusSpeed() {
        return this.flatBonus;
    }

    @Override
    public float getPercentageBonusSpeed() {
        return this.percentageBonus;
    }

    @Override
    public boolean canRemoved() {
        return this.elapsed > this.ttl;
    }

    @Override
    public String toString () {
        return "Movement Speed Boost (ttl: " + this.ttl + ", flat bonus: " + this.flatBonus + ", percentage bonus: " + (percentageBonus * 100f) + "%).";
    }

}
