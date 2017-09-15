package de.leeksanddragons.engine.memory;

/**
 * Data model for asset loading
 *
 * Created by Justin on 14.09.2017.
 */
public class AssetInfo {

    public enum TYPE {
        TEXTURE, TEXTURE_ATLAS, MUSIC, SOUND, UNKNOWN
    };

    protected String path = "";
    protected TYPE type = TYPE.UNKNOWN;

    public AssetInfo (String path, TYPE type) {
        this.path = path;
        this.type = type;
    }

    public String getPath () {
        return this.path;
    }

    public TYPE getType () {
        return type;
    }

}
