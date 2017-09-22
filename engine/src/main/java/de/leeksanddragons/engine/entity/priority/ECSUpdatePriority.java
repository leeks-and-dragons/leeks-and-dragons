package de.leeksanddragons.engine.entity.priority;

/**
 * Update order of entity
 *
 * Created by Justin on 10.02.2017.
 */
public enum ECSUpdatePriority {

    VERY_LOW(1),

    LOW(2),

    COLLISION_DETECTION(3),

    NORMAL(4),

    HIGH(5),

    VERY_HIGH(6);

    private final int id;

    ECSUpdatePriority(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

}
