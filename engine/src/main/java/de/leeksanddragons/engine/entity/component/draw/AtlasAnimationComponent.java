package de.leeksanddragons.engine.entity.component.draw;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IUpdateComponent;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.entity.priority.ECSUpdatePriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.AtlasUtils;
import de.leeksanddragons.engine.utils.GameTime;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Justin on 24.09.2017.
 */
public class AtlasAnimationComponent extends BaseComponent implements IUpdateComponent {

    @InjectComponent(nullable = false)
    protected DrawComponent drawComponent;

    //path to texture atlas
    protected String atlasPath = "";

    //name of animation
    protected String animationName = "";

    //duration of one frame in ms
    protected float frameDuration = 0.1f;

    //texture atlas
    protected TextureAtlas atlas = null;

    // map with all cached animations of entity
    protected Map<String, Animation<TextureRegion>> animationMap = new ConcurrentHashMap<>();

    //current animation
    protected Animation<TextureRegion> currentAnimation = null;

    //elapsed time
    protected float elapsed = 0;

    //flag, if texture atlas was loaded
    protected boolean isLoaded = false;

    /**
    * default constructor
     *
     * @param atlasPath path to texture atlas file
     * @param animationName name of animation to draw
    */
    public AtlasAnimationComponent (String atlasPath, String animationName, float frameDuration) {
        this.atlasPath = atlasPath;
        this.animationName = animationName;
        this.frameDuration = frameDuration;

        //check, if path is null
        if (atlasPath == null) {
            throw new NullPointerException("atlas path cannot be null.");
        }

        if (atlasPath.isEmpty()) {
            throw new IllegalArgumentException("atlas path cannot be empty.");
        }

        if (!new File(atlasPath).exists()) {
            throw new IllegalArgumentException("atlas file doesnt exists: " + atlasPath);
        }
    }

    @Override
    protected void onInit(IScreenGame game, Entity entity) {
        //load texture atlas
        game.getAssetManager().load(this.atlasPath, TextureAtlas.class);
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        if (this.atlas == null) {
            //check, if atlas was loaded
            if (game.getAssetManager().isLoaded(this.atlasPath)) {
                //get texture atlas
                this.atlas = game.getAssetManager().get(this.atlasPath);

                //get all available animation names
                Map<String, Integer> map = null;

                try {
                    // parse all available animations
                    map = AtlasUtils.getAvailableAnimations(this.atlasPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Couldnt parse texture altas: " + this.atlasPath + ", exception: "
                            + e.getLocalizedMessage());
                }

                //create all available animations in cache
                this.createCachedAnimations(map);

                this.isLoaded = true;

                //get current animation
                if (!this.animationName.isEmpty()) {
                    //this.currentAnimation = getAnimationByName(this.animationName);

                    /*if (this.currentAnimation == null) {
                        throw new NullPointerException("current animation cannot be null.");
                    }*/

                    String oldAnimationName = this.animationName;
                    this.animationName = "";

                    this.setCurrentAnimationName(oldAnimationName);
                }
            } else {
                //we dont need to do anything, while atlas file wasnt loaded
                return;
            }
        }

        //System.out.println("elapsed time: " + this.elapsed);

        //calculate elapsed time
        this.elapsed += time.getDeltaTime();

        if (this.currentAnimation == null) {
            //we dont need to update animation
            return;
        }

        // update texture region component
        updateTextureRegion();
    }

    /**
    * set current animation frame to texture region
    */
    protected void updateTextureRegion () {
        TextureRegion currentTextureRegion = this.currentAnimation.getKeyFrame(this.elapsed);

        if (currentTextureRegion == null) {
            throw new NullPointerException("current texture region is null.");
        }

        if (!currentTextureRegion.getTexture().isManaged()) {
            throw new IllegalStateException("current texture region isnt managed.");
        }

        // set current frame
        this.drawComponent.setTextureRegion(currentTextureRegion, true);
    }

    /**
    * get texture atlas path
     *
     * @return texture atlas path
    */
    public String getTextureAtlasPath() {
        return this.atlasPath;
    }

    /**
    * get texture atlas
     *
     * @return texture atlas
    */
    public TextureAtlas getTextureAtlas() {
        return this.atlas;
    }

    /**
    * get current animation name
     *
     * @return current animation name
    */
    public String getCurrentAnimationName() {
        return this.animationName;
    }

    /**
    * set current animation name
     *
     * @param animationName current animation name
    */
    public void setCurrentAnimationName(String animationName) {
        // check if animation name was changed
        if (this.animationName.equals(animationName)) {
            return;
        }

        String oldAnimationName = this.animationName;
        this.animationName = animationName;

        // get animation
        this.currentAnimation = this.getAnimationByName(animationName);

        // reset elapsed time
        //this.elapsed = 0;

        this.onAnimationStateChanged(oldAnimationName, animationName);
    }

    /**
    * create animations
     *
     * @param map map with all available animation names and frames count
    */
    protected void createCachedAnimations(Map<String, Integer> map) {
        int i = 0;

        //iterate through all entries
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String animationName = entry.getKey();
            int frameCounter = entry.getValue();

            // calculate duration per frame
            float durationPerFrame = this.frameDuration;//this.sumDuration / frameCounter;

            // get regions
            Array<TextureAtlas.AtlasRegion> regions = this.atlas.findRegions(animationName);

            // create animation
            Animation<TextureRegion> anim = new Animation<>(durationPerFrame, regions, Animation.PlayMode.LOOP);

            // add animation to map
            this.animationMap.put(animationName, anim);

            i++;
        }
    }

    /**
    * check, if texture atlas was loaded
     *
     * @return true, if texture atlas was loaded
    */
    public boolean isLoaded () {
        return this.isLoaded;
    }

    /**
    * get instance of animation by name
     *
     * @param animationName name of animation
     *
     * @return animation
    */
    public Animation<TextureRegion> getAnimationByName(String animationName) {
        if (!isLoaded) {
            throw new IllegalStateException("texture atlas wasnt loaded yet, check if loaded with isLoaded() before.");
        }

        // get animation
        Animation<TextureRegion> animation = this.animationMap.get(animationName);

        if (animation == null) {
            return null;
            //throw new IllegalStateException("Could not found animation: " + animationName);
        }

        return animation;
    }

    protected void onAnimationStateChanged(String oldAnimationName, String newAnimationName) {
        // TODO: call listeners
    }

    @Override
    public ECSUpdatePriority getUpdateOrder() {
        return ECSUpdatePriority.VERY_LOW;
    }

    @Override
    public void dispose() {

    }

}
