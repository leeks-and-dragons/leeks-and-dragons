package de.leeksanddragons.engine.utils;

import com.badlogic.gdx.math.Vector2;
import de.leeksanddragons.engine.memory.Vector2Pool;

/**
 * Created by Justin on 11.04.2017.
 */
public class FastMath {

    /**
     * rotate vector with 90 degrees
     */
    public static Vector2 rotateVector90(Vector2 v, Vector2 newVector) {
        float x = -v.y;
        float y = v.x;
        newVector.set(x, y);

        return newVector;
    }

    /**
     * rotate vector with 90 degrees
     */
    public static Vector2 rotateVector90(Vector2 v) {
        return rotateVector90(v, Vector2Pool.create());
    }

    public static boolean checkVectorsParallel(Vector2 v1, Vector2 v2) {
        // rotate vector 90 degrees
        Vector2 rotatedVector1 = rotateVector90(v1, Vector2Pool.create());

        // 2 vectors are parallel, if dot product = 0
        float dotProduct = rotatedVector1.dot(v2);

        Vector2Pool.free(rotatedVector1);

        return dotProduct == 0;
    }

}
