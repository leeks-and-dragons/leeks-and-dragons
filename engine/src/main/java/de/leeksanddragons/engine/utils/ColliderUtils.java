package de.leeksanddragons.engine.utils;

/**
 * Created by Justin on 21.09.2017.
 */
public class ColliderUtils {

    public static boolean overlaping(float minA, float maxA, float minB, float maxB) {
        if (maxA < minA) {
            // swap values
            float a = maxA;
            maxA = minA;
            minA = a;
        }

        if (maxB < minB) {
            // swap values
            float b = minB;
            maxB = minB;
            minB = b;
        }

        // check if values are overlapping
        return minB <= maxA && minA <= maxB;
    }

}
