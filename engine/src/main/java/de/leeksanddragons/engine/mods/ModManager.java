package de.leeksanddragons.engine.mods;

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
    * load mods from directory
     *
     * @param dirPath path to mods directory
    */
    public void loadMods (String dirPath);

    /**
    * load an single mod
     *
     * @param modDirPath path to mod directory
    */
    public void loadMod (String modDirPath);

}
