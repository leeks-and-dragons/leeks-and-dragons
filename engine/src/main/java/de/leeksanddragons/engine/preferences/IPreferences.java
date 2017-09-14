package de.leeksanddragons.engine.preferences;

import com.badlogic.gdx.Preferences;

/**
 * Created by Justin on 11.09.2017.
 */
public interface IPreferences extends Preferences {

    /**
    * check, if preferences can be saved, for example WebGL backend doesnt supports this operation
    */
    public boolean canSave ();

    /**
    * get path to preferences file
     *
     * @return path to preferences file
    */
    public String getPrefsPath ();

    /**
    * put boolean, if key is absent
     *
     * @param key key
     * @param val value which will be set, if key is absent
    */
    public void putBooleanIfAbsent (String key, boolean val);

}
