package de.leeksanddragons.game.screen;

import com.badlogic.gdx.Gdx;
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
 * Created by Justin on 12.09.2017.
 */
public class MainMenuScreen extends BaseScreen {

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
    protected MenuButton startButton = null;
    protected MenuButton optionsButton = null;
    protected MenuButton editorButton = null;
    protected MenuButton creditsButton = null;
    protected MenuButton closeButton = null;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {
        //load background image first
        assetManager.load(BG_PATH, Texture.class);
        assetManager.load(FG_PATH, Texture.class);

        //force finish asset loading
        assetManager.finishLoadingAsset(BG_PATH);
        assetManager.finishLoadingAsset(FG_PATH);

        //get texture
        this.bgTexture = assetManager.get(BG_PATH);
        this.fgTexture = assetManager.get(FG_PATH);
    }

    @Override
    public void onResume() {
        if (this.hud == null) {
            //create new HUD
            this.hud = new HUD();

            //create new font
            this.font = BitmapFontFactory.createFont("./data/font/arial/arial.ttf", 16, Color.WHITE);

            //create buttons
            this.createVerticalButtons(this.hud);
        }
    }

    @Override
    public void onPause() {

    }

    protected void createHorizontalButtons (HUD hud) {
        //first, get texture atlas
        TextureAtlas textureAtlas = game.getAssetManager().getAssetByName("menu_buttons", TextureAtlas.class);

        //get hover sound
        Sound hoverSound = game.getAssetManager().getAssetByName("menu_hover_sound", Sound.class);

        float startX = 80;//40;
        float startY = 20;

        //create start button
        this.startButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Start Game");
        this.startButton.setPosition(startX, startY);
        this.startButton.setHoverSound(hoverSound, 0.5f);
        this.hud.addWidget(startButton);

        startX += 110;

        //create options button
        this.optionsButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Options");
        this.optionsButton.setPosition(startX, startY);
        this.optionsButton.setHoverSound(hoverSound, 0.5f);
        this.hud.addWidget(optionsButton);

        startX += 110;

        //create editor button
        this.editorButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Editor");
        this.editorButton.setPosition(startX, startY);
        this.editorButton.setHoverSound(hoverSound, 0.5f);
        this.hud.addWidget(editorButton);

        startX += 110;

        //create credits button
        this.creditsButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Credits");
        this.creditsButton.setPosition(startX, startY);
        this.creditsButton.setHoverSound(hoverSound, 0.5f);
        this.hud.addWidget(creditsButton);

        startX += 110;

        //create close button
        this.closeButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Close");
        this.closeButton.setPosition(startX, startY);
        this.closeButton.setHoverSound(hoverSound, 0.5f);
        this.closeButton.setClickListener(() -> {
            //close application
            Gdx.app.exit();
        });
        this.hud.addWidget(closeButton);
    }

    protected void createVerticalButtons (HUD hud) {
        //first, get texture atlas
        TextureAtlas textureAtlas = game.getAssetManager().getAssetByName("menu_buttons", TextureAtlas.class);

        //get hover sound
        Sound hoverSound = game.getAssetManager().getAssetByName("menu_hover_sound", Sound.class);

        float startX = 100;//40;
        float startY = 400;

        //create start button
        this.startButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Start Game");
        this.startButton.setPosition(startX, startY);
        this.startButton.setHoverSound(hoverSound, 0.5f);
        this.hud.addWidget(startButton);

        startY -= 60;

        //create options button
        this.optionsButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Options");
        this.optionsButton.setPosition(startX, startY);
        this.optionsButton.setHoverSound(hoverSound, 0.5f);
        this.hud.addWidget(optionsButton);

        startY -= 60;

        if (this.game.isDevMode()) {
            //create editor button
            this.editorButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Editor");
            this.editorButton.setPosition(startX, startY);
            this.editorButton.setHoverSound(hoverSound, 0.5f);
            this.hud.addWidget(editorButton);

            startY -= 60;
        }

        //create credits button
        this.creditsButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Credits");
        this.creditsButton.setPosition(startX, startY);
        this.creditsButton.setHoverSound(hoverSound, 0.5f);
        this.hud.addWidget(creditsButton);

        startY -= 60;

        //create close button
        this.closeButton = new MenuButton(textureAtlas, "button", "button_hovered", this.font, "Close");
        this.closeButton.setPosition(startX, startY);
        this.closeButton.setHoverSound(hoverSound, 0.5f);
        this.closeButton.setClickListener(() -> {
            //close application
            Gdx.app.exit();
        });
        this.hud.addWidget(closeButton);
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //update HUD
        this.hud.update(game, time);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //draw background
        batch.draw(this.bgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());

        //draw GUI
        this.hud.drawLayer0(time, batch);

        //draw foreground graphics
        batch.draw(this.fgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());
    }

    @Override
    public void dispose() {

    }

}
