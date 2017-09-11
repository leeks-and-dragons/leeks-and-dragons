package de.leeksanddragons.engine.preferences;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import de.leeksanddragons.engine.Engine;
import de.leeksanddragons.engine.utils.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Preferences implementation, only works on desktop backend
 *
 * Created by Justin on 11.09.2017.
 */
public class GamePreferences implements IPreferences {

    public static final String PREFERENCES_DIR_NAME = "prefs";

    //path to preferences file
    protected String prefPath = "";

    //json object
    protected JSONObject json = null;

    public GamePreferences (String appName, String fileName) {
        //get preferences path
        this.prefPath = getPath(appName, fileName);

        //try load preferences file
        this.load();

        //check, if json object is null (for example on junit tests) and create json object if neccessary
        if (this.json == null) {
            //create new json object
            this.json = new JSONObject();

            //set engine version
            this.json.put("engine_version", Engine.ENGINE_VERSION);
        }
    }

    /**
    * get file path of preferences file
    */
    protected String getPath (String appName, String fileName) {
        switch (Gdx.app.getType()) {
            case Desktop:
                //check, if preferences directory exists, if not create directory
                if (!new File(FileUtils.getAppHomeDir(appName) + PREFERENCES_DIR_NAME + "/").exists()) {
                    new File(FileUtils.getAppHomeDir(appName) + PREFERENCES_DIR_NAME + "/").mkdirs();
                }

                return FileUtils.getAppHomeDir(appName) + PREFERENCES_DIR_NAME + "/" + fileName + ".pref";
            case Android:
                throw new IllegalStateException("GamePreferences doesnt supports Android backend yet.");
            case iOS:
                throw new IllegalStateException("GamePreferences doesnt supports iOS backend yet.");
            case Applet:
                throw new IllegalStateException("GamePreferences doesnt supports Applet backend yet.");
            case HeadlessDesktop:
                return "none";
            case WebGL:
                //webGL backend cannot store persistent options
                return "none";
            default:
                throw new IllegalStateException("Unknown libGDX backend: " + Gdx.app.getType());
        }
    }

    /**
    * load preferences, if possible
    */
    protected void load () {
        //check, if junit tests are running
        if (Gdx.app.getType() == Application.ApplicationType.HeadlessDesktop) {
            return;
        }

        //check, if preferences file is possible (for example HeadlessApplication doesnt has one)
        if (this.prefPath.contains("none")) {
            return;
        }

        //check, if file exists
        if (!new File(this.prefPath).exists()) {
            //cannot load preferences, because file doesnt exists
            Gdx.app.debug("Preferences", "Cannot load preferences, because file doesnt exists yet:" + this.prefPath);
            return;
        }

        //read file content
        try {
            String content = FileUtils.readFile(this.prefPath, StandardCharsets.UTF_8);
            this.json = new JSONObject(content);
        } catch (IOException e) {
            e.printStackTrace();
            Gdx.app.error("Preferences", "Cannot load preferences file '" + this.prefPath + "', caused by IOException: " + e.getLocalizedMessage() , e);
        }
    }

    /**
    * save changes
    */
    protected void save () {
        //check, if junit tests are running
        if (Gdx.app.getType() == Application.ApplicationType.HeadlessDesktop) {
            return;
        }

        //check, if preferences file can be stored (for example WebGL backend doensnt support persistent file saving)
        if (this.prefPath.contains("none")) {
            return;
        }

        //https://www.programcreek.com/java-api-examples/index.php?api=org.json.JSONWriter

        //set engine version
        json.put("engine_version", Engine.ENGINE_VERSION);

        //write file
        try {
            FileUtils.writeFile(this.prefPath, json.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            Gdx.app.error("Preferences", "Cannot write preferences file '" + this.prefPath + "', caused by IOException: " + e.getLocalizedMessage(), e);
        }
    }

    @Override
    public Preferences putBoolean(String key, boolean val) {
        this.json.put(key, val);
        return this;
    }

    @Override
    public Preferences putInteger(String key, int val) {
        this.json.put(key, val);
        return this;
    }

    @Override
    public Preferences putLong(String key, long val) {
        this.json.put(key, val);
        return this;
    }

    @Override
    public Preferences putFloat(String key, float val) {
        this.json.put(key, val);
        return this;
    }

    @Override
    public Preferences putString(String key, String val) {
        this.json.put(key, val);
        return this;
    }

    @Override
    public Preferences put(Map<String, ?> vals) {
        for (Map.Entry<String,?> entry : vals.entrySet()) {
            this.json.put(entry.getKey(), entry.getValue());
        }

        return this;
    }

    @Override
    public boolean getBoolean(String key) {
        return json.getBoolean(key);
    }

    @Override
    public int getInteger(String key) {
        return json.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return json.getLong(key);
    }

    @Override
    public float getFloat(String key) {
        return (float) json.getDouble(key);
    }

    @Override
    public String getString(String key) {
        return json.getString(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        if (!json.has(key)) {
            return defValue;
        } else {
            return json.getBoolean(key);
        }
    }

    @Override
    public int getInteger(String key, int defValue) {
        if (!json.has(key)) {
            return defValue;
        } else {
            return json.getInt(key);
        }
    }

    @Override
    public long getLong(String key, long defValue) {
        if (!json.has(key)) {
            return defValue;
        } else {
            return json.getLong(key);
        }
    }

    @Override
    public float getFloat(String key, float defValue) {
        if (!json.has(key)) {
            return defValue;
        } else {
            return (float) json.getDouble(key);
        }
    }

    @Override
    public String getString(String key, String defValue) {
        if (!json.has(key)) {
            return defValue;
        } else {
            return json.getString(key);
        }
    }

    @Override
    public Map<String, ?> get() {
        return json.toMap();
    }

    @Override
    public boolean contains(String key) {
        return json.has(key);
    }

    @Override
    public void clear() {
        //iterate through all available keys in json object
        for (String key : this.json.keySet()) {
            //remove key
            json.remove(key);
        }
    }

    @Override
    public void remove(String key) {
        json.remove(key);
    }

    @Override
    public void flush() {
        this.save();
    }

}
