package de.leeksanddragons.engine.preferences;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 11.09.2017.
 */
public class GamePreferencesTest {

    @Test
    public void testGetterAndSetter () {
        GamePreferences prefs = new GamePreferences();

        //test boolean
        prefs.putBoolean("b1", true);
        assertEquals(true, prefs.getBoolean("b1"));
        assertEquals(true, prefs.getBoolean("b1", false));
        assertEquals(false, prefs.getBoolean("b2", false));
        assertEquals(true, prefs.getBoolean("b2", true));

        //test float
        prefs.putFloat("f1", 1.25f);
        assertEquals(1.25f, prefs.getFloat("f1"), 0);
        assertEquals(1.25f, prefs.getFloat("f1", 10f), 0);
        assertEquals(-3.75f, prefs.getFloat("f2", -3.75f), 0);
        assertEquals(2.5f, prefs.getFloat("f2", 2.5f), 0);

        //test integer
        prefs.putInteger("i1", 10);
        assertEquals(10, prefs.getInteger("i1"));
        assertEquals(10, prefs.getInteger("i1", 20));
        assertEquals(-20, prefs.getInteger("i2", -20));
        assertEquals(20, prefs.getInteger("i2", 20));

        //test long
        prefs.putLong("l1", 200l);
        assertEquals(200l, prefs.getLong("l1"));
        assertEquals(200l, prefs.getLong("l1", 400l));
        assertEquals(-400l, prefs.getLong("l2", -400));
        assertEquals(400l, prefs.getLong("l2", 400));

        //test string
        prefs.putString("s1", "string1");
        assertEquals("string1", prefs.getString("s1"));
        assertEquals("string1", prefs.getString("s1", "string2"));
        assertEquals("str3", prefs.getString("s2", "str3"));
        assertEquals("str4", prefs.getString("s2", "str4"));
    }

}
