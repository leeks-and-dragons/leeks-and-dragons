package de.leeksanddragons.engine.mods;

import de.leeksanddragons.engine.exception.InvalideModJSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Justin on 09.09.2017.
 */
public interface ModManager {

    /**
    * list all loaded mod names
     *
     * @return list with all loaded mod names
    */
    public List<String> listLoadedModNames ();

    /**
    * list all loaded mods
     *
     * @return list of all loaded mods
    */
    public List<ModInfo> listLoadedMods ();

    /**
    * load mods from directory
     *
     * @param dirPath path to mods directory
    */
    public void loadMods (String dirPath);

    /**
    * load an single mod
     *
     * @param modDirPath path to mod directory
     *
     * @return true, if mod was loaded successfully
    */
    public boolean loadMod (String modDirPath) throws IOException, InvalideModJSONException;

}
