package de.leeksanddragons.engine.memory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.utils.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public void parseMod (String modPath) throws IOException {
        //check, if mod directory exists
        if (!new File(modPath).exists() || !new File(modPath).isDirectory()) {
            throw new IllegalStateException("mod directory doesnt exists or isnt an directory: " + modPath);
        }

        //check, if load_assets.json file exists
        File file = new File(modPath + "load_assets.json");

        if (!file.exists()) {
            throw new GdxRuntimeException("Couldnt found load_assets.json in directory: " + modPath);
        }

        //read file
        String content = FileUtils.readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
        JSONArray jsonArray = new JSONArray(content);

        Gdx.app.debug("Loading", "" + jsonArray.length() + " assets for pre-loading found in load_assets.json: " + modPath + "/load_assets.json.");

        //iterate through all assets
        for (int i = 0; i < jsonArray.length(); i++) {
            //get json object
            JSONObject json = jsonArray.getJSONObject(i);

            //get attributes
            String path = json.getString("path");
            String type = json.getString("type");

            String fullPath = modPath + path;

            //create new asset info
            AssetInfo asset = null;

            switch (type.toLowerCase()) {
                case "texture":
                    asset = new AssetInfo(fullPath, AssetInfo.TYPE.TEXTURE);
                    break;
                case "sound":
                    asset = new AssetInfo(fullPath, AssetInfo.TYPE.SOUND);
                    break;
                case "music":
                    asset = new AssetInfo(fullPath, AssetInfo.TYPE.MUSIC);
                    break;
                default:
                    Gdx.app.error("Loading Asset", "Unknown asset type '" + type + "' in load_assets.json in mod directory: " + modPath);
                    continue;
            }

            //add asset to list
            this.assetInfoList.add(asset);
        }
    }

    public List<AssetInfo> listAllAssets () {
        return this.assetInfoList;
    }

}
