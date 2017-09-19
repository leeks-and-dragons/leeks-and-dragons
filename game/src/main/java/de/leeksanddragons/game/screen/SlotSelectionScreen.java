package de.leeksanddragons.game.screen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.font.BitmapFontFactory;
import de.leeksanddragons.engine.gui.HUD;
import de.leeksanddragons.engine.gui.widgets.MenuButton;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 18.09.2017.
 */
public class SlotSelectionScreen extends BaseScreen {

    //image paths
    protected static final String BG_PATH = "./data/wallpaper/Loading_Screen.png";
    protected static final String FG_PATH = "./data/wallpaper/Loading_Screen_layer_2.png";

    //background image
    protected Texture bgTexture = null;
    protected Texture fgTexture = null;

    //font
    protected BitmapFont font = null;

    //GUI
    protected HUD hud = null;

    //buttons
    protected MenuButton backToMenuButton = null;
    protected MenuButton deleteButton = null;
    protected MenuButton startButton = null;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {

    }

    @Override
    public void onResume(IScreenGame game) {
        if (this.hud == null) {
            //create new HUD
            this.hud = new HUD();

            //load background image first
            assetManager.load(BG_PATH, Texture.class);
            assetManager.load(FG_PATH, Texture.class);

            //force finish asset loading
            assetManager.finishLoadingAsset(BG_PATH);
            assetManager.finishLoadingAsset(FG_PATH);

            //get texture
            this.bgTexture = assetManager.get(BG_PATH);
            this.fgTexture = assetManager.get(FG_PATH);

            //create new font
            this.font = BitmapFontFactory.createFont("./data/font/arial/arial.ttf", 16, Color.WHITE);

            //add buttons
            this.addButtons();
        }
    }

    @Override
    public void onPause(IScreenGame game) {

    }

    /**
    * add buttons to HUD
    */
    protected void addButtons () {
        //first, get texture atlas
        TextureAtlas textureAtlas = game.getAssetManager().getAssetByName("menu_buttons", TextureAtlas.class);

        //get hover sound
        Sound hoverSound = game.getAssetManager().getAssetByName("menu_hover_sound", Sound.class);

        //create start button
        this.backToMenuButton = new MenuButton(textureAtlas, "button2", "button2_hovered", this.font, "Menu");
        this.backToMenuButton.setPosition(50, 50);
        this.backToMenuButton.setHoverSound(hoverSound, 0.5f);
        this.backToMenuButton.setClickListener(() -> {
            game.getScreenManager().leaveAllAndEnter("mainmenu");
        });
        this.hud.addWidget(backToMenuButton);
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //update HUD
        this.hud.update(game, time);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //get camera
        CameraHelper camera = game.getCameraManager().getUICamera();

        //draw background
        batch.draw(this.bgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());

        //draw GUI
        this.hud.drawLayer0(time, batch);

        //draw foreground graphics
        batch.draw(this.fgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());
    }

    @Override
    public void dispose() {
        if (this.bgTexture != null) {
            //unload texture
            this.game.getAssetManager().unload(BG_PATH);

            this.bgTexture = null;
        }

        if (this.fgTexture != null) {
            //unload texture
            this.game.getAssetManager().unload(FG_PATH);

            this.fgTexture = null;
        }

        if (this.font != null) {
            this.font.dispose();
            this.font = null;
        }
    }

}
