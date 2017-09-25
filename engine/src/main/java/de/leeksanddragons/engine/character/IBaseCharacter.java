package de.leeksanddragons.engine.character;

import java.io.IOException;

/**
 * Created by Justin on 25.09.2017.
 */
public interface IBaseCharacter {

    /**
    * load character json file
     *
     * @param path path to character json file
    */
    public void load (String path) throws IOException;

    /**
    * get name of character
     *
     * @return name of character
    */
    public String getName ();

    /**
    * get base speed of character in units
     *
     * @return base speed of character in units
    */
    public float getBaseSpeed ();

    /**
    * get path to atlas file
     *
     * @return path to atlas file
    */
    public String getAtlasPath ();

}
