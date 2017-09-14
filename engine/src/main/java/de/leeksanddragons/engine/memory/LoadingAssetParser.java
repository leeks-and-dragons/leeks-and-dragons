package de.leeksanddragons.engine.memory;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 14.09.2017.
 */
public class LoadingAssetParser {

    //list with all assets which can be pre-loaded
    protected List<AssetInfo> assetInfoList = new ArrayList<>();

    public LoadingAssetParser () {
        //
    }

    public void parseMod (String modPath) {
        //check, if mod directory exists
        if (!new File(modPath).exists() || !new File(modPath).isDirectory()) {
            throw new IllegalStateException("mod directory doesnt exists or isnt an directory: " + modPath);
        }

        //check, if load_assets.json file exists
        File file = new File(modPath + "load_assets.json");

        if (!file.exists()) {
            throw new GdxRuntimeException("Couldnt found load_assets.json in directory: " + modPath);
        }
    }

    public List<AssetInfo> listAllAssets () {
        return this.assetInfoList;
    }

}
