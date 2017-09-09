package de.leeksanddragons.engine.mods.impl;

import com.badlogic.gdx.Gdx;
import de.leeksanddragons.engine.exception.InvalideModJSONException;
import de.leeksanddragons.engine.mods.ModInfo;
import de.leeksanddragons.engine.mods.ModManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of mod manager, responsible for loading and manage active mods
 *
 * Created by Justin on 09.09.2017.
 */
public class DefaultModManager implements ModManager {

    public static final String LOG_TAG = "Mods";

    //list with all loaded mods
    protected List<ModInfo> loadedModsList = new ArrayList<>();

    //list with all mod names
    protected List<String> modNameList = new ArrayList<>();

    /**
    * default constructor
    */
    public DefaultModManager () {
        //
    }

    @Override
    public List<String> listLoadedModNames() {
        return this.modNameList;
    }

    @Override
    public List<ModInfo> listLoadedMods() {
        return Collections.unmodifiableList(this.loadedModsList);
    }

    @Override
    public int countLoadedMods() {
        return this.listLoadedMods().size();
    }

    @Override
    public void loadMods(String dirPath) {
        File dir = new File(dirPath);

        //list all directories in path
        File[] fileList = dir.listFiles();

        //iterate through all directories / files
        for (File file : fileList) {
            //check, if it is an directory
            if (file.isDirectory()) {
                //try to load mod
                try {
                    this.loadMod(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean loadMod(String modDirPath) throws IOException {
        ModInfo mod = null;

        try {
            //first load mod.json file
            mod = ModInfo.create(modDirPath);
        } catch (InvalideModJSONException e) {
            e.printStackTrace();

            Gdx.app.error(LOG_TAG, "Cannot load plugin, caused by invalide mod.json exception: " + modDirPath);

            return false;
        }

        //check, if mod is activated
        if (mod.isActiaved()) {
            this.loadedModsList.add(mod);
            this.modNameList.add(mod.getName());

            Gdx.app.log(LOG_TAG, "mod loaded: " + mod.getName());

            return true;
        } else {
            Gdx.app.log(LOG_TAG, "mod is deactivated: " + mod.getName());

            return false;
        }
    }

}
