package de.leeksanddragons.engine.mods;

import de.leeksanddragons.engine.exception.InvalideModJSONException;
import de.leeksanddragons.engine.utils.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This class represents the general information about an mod and is loaded from mod.json in mod directory.
 *
 * Created by Justin on 09.09.2017.
 *
 * @author Leeks & Dragons Team
 * @since version 1.0.0
 *
 * Copyright (c) 2017 leeks-and-dragons.de, All Rights Reserved.
 * This file is licensed unter CC-BY-SA 3.0 license.
 */
public class ModInfo implements Comparable<ModInfo> {

    //path to mod directory
    protected String modPath = "";

    //mod name
    protected String name = "";

    //mod title
    protected String title = "";

    //mod version information
    protected String version = "";
    protected int versionNumber = 1;

    //mod author
    protected String author = "";

    //path to credits file
    protected String creditsFile = "";

    //engine requirements
    protected String minVersion = "";
    protected int minVersionNumber = 1;

    //array of all supported languages
    protected String[] supportedLanguages = new String[] {"de", "en"};

    //load priority
    protected int load_priority = 5;

    //some constants for json keys
    protected static final String KEY_MOD_NAME = "mod_name";
    protected static final String KEY_MOD_TITLE = "mod_title";
    protected static final String KEY_MOD_VERSION = "mod_version";
    protected static final String KEY_MOD_VERSION_NUMBER = "mod_version_number";
    protected static final String KEY_AUTHOR = "author";
    protected static final String KEY_CREDITS = "credits";
    protected static final String KEY_MIN_ENGINE_VERSION = "min_engine_version";
    protected static final String KEY_MIN_ENGINE_VERSION_NUMBER = "min_engine_version_number";
    protected static final String KEY_SUPPORTED_LANGUAGES = "supported_languages";
    protected static final String KEY_LOAD_PRIORITY = "load_priority";

    protected static final String[] REQUIRED_JSON_KEYS = new String[] {
            KEY_MOD_NAME,
            KEY_MOD_TITLE,
            KEY_MOD_VERSION,
            KEY_MOD_VERSION_NUMBER,
            KEY_AUTHOR,
            KEY_CREDITS,
            KEY_MIN_ENGINE_VERSION,
            KEY_MIN_ENGINE_VERSION_NUMBER,
            KEY_SUPPORTED_LANGUAGES,
            KEY_LOAD_PRIORITY
    };

    /**
    * default constructor
    */
    protected ModInfo () {
        //
    }

    public boolean isLoadable () {
        throw new UnsupportedOperationException("method isnt implemented yet.");
    }

    @Override
    public int compareTo(ModInfo o) {
        return o.load_priority > this.load_priority ? 1 : o.load_priority < this.load_priority ? -1 : 0;
    }

    /**
    * create new ModInfo instance and load from mod json
    */
    public static ModInfo create (String modPath) throws IOException, InvalideModJSONException {
        File jsonFile = new File(modPath + "/mod.json");

        //first, check if directory exists
        if (!new File(modPath).exists() || !new File(modPath).isDirectory()) {
            throw new FileNotFoundException("mod directory doesnt exists or is not an director: " + new File(modPath).getAbsolutePath());
        }

        //check, if it is an mod directory, so it must contains an mod.json file
        if (!jsonFile.exists()) {
            throw new FileNotFoundException("directory isnt an valide mod directory, no mod.json file exists: " + new File(modPath).getAbsolutePath());
        }

        //load json file
        String content = FileUtils.readFile(jsonFile.getAbsolutePath(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        //check, if json file is valide and contains all information needed for ModInfo
        for (String requiredKey : REQUIRED_JSON_KEYS) {
            if (!json.has(requiredKey)) {
                throw new InvalideModJSONException("invalide mod.json (" + jsonFile.getAbsolutePath() + "): key '" + requiredKey + "' is missing in mod.json file.");
            }
        }

        //create new instance
        ModInfo mod = new ModInfo();

        //set mod information
        mod.modPath = new File(modPath).getAbsolutePath();
        mod.name = json.getString(KEY_MOD_NAME);
        mod.title = json.getString(KEY_MOD_TITLE);
        mod.version = json.getString(KEY_MOD_VERSION);
        mod.versionNumber = json.getInt(KEY_MOD_VERSION_NUMBER);
        mod.author = json.getString(KEY_AUTHOR);
        mod.creditsFile = json.getString(KEY_CREDITS);
        mod.minVersion = json.getString(KEY_MIN_ENGINE_VERSION);
        mod.minVersionNumber = json.getInt(KEY_MIN_ENGINE_VERSION_NUMBER);

        JSONArray jsonArray = json.getJSONArray(KEY_SUPPORTED_LANGUAGES);
        mod.supportedLanguages = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            mod.supportedLanguages[i] = jsonArray.getString(i);
        }

        mod.load_priority = json.getInt(KEY_LOAD_PRIORITY);

        return mod;
    }

}
