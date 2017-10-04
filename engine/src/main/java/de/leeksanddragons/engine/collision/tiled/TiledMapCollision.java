package de.leeksanddragons.engine.collision.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.leeksanddragons.engine.collision.MapCollision;
import de.leeksanddragons.engine.utils.SpriteBatcherUtils;

/**
 * Created by Justin on 04.10.2017.
 */
public class TiledMapCollision implements MapCollision {

    //boolean for every tile in map, true means, player can pass tile, false player cannot pass tile
    protected boolean[][] canPassTiles = null;

    //width and height of an single tile
    protected int tileWidth = 0;
    protected int tileHeight = 0;

    //flag, if collision information is loaded
    protected boolean loaded = false;

    //map position
    protected float mapX = 0;
    protected float mapY = 0;

    protected int tileCountX = 0;
    protected int tileCountY = 0;

    /**
    * default constructor
     *
     * @param mapX x position of tiled map (in pixels)
     * @param mapY y position of tiled map (in pixels)
    */
    public TiledMapCollision (float mapX, float mapY) {
        this.mapX = mapX;
        this.mapY = mapY;
    }

    /**
    * load tiled map collisions
     *
     * @param map instance of tiled map
    */
    public void load (TiledMap map) {
        //get properties
        MapProperties props = map.getProperties();
        this.tileCountX = props.get("width", Integer.class);//get width in tiles
        this.tileCountY = props.get("height", Integer.class);//get height in tiles

        //get width and hight of an single tile
        this.tileWidth = props.get("tilewidth", Integer.class);
        this.tileHeight = props.get("tileheight", Integer.class);

        Gdx.app.debug("TiledMapCollisions", "load map collisions for " + tileCountX + "x" + tileCountY + " map.");

        //create new collision array
        this.canPassTiles = new boolean[this.tileCountX][this.tileCountY];

        //initialize array, so player can pass every tile
        for (int x = 0; x < this.tileCountX; x++) {
            for (int y = 0; y < this.tileCountY; y++) {
                this.canPassTiles[x][y] = true;
            }
        }

        //iterate through all layers
        for (MapLayer layer : map.getLayers()) {
            //Gdx.app.debug("TiledMapCollision", "check layer '" + layer.getName() + "' for map collisions.");

            //check, if it is an collision layer
            if (layer.getName().contains("collision")) {
                //Gdx.app.debug("TiledMapCollision", "collision layer found: " + layer.getName());

                //iterate through objects
                for (MapObject obj : layer.getObjects()) {
                    //get position of object
                    float x = obj.getProperties().get("x", Float.class);
                    float y = obj.getProperties().get("y", Float.class);

                    //get width & height of object
                    float width = obj.getProperties().get("width", Float.class);
                    float height = obj.getProperties().get("height", Float.class);

                    float x2 = x + width;
                    float y2 = y + height;

                    //every started tile counts
                    if (x2 % this.tileWidth != 0) {
                        x2 += this.tileWidth - (x2 % this.tileWidth);
                    }

                    //every started tile counts
                    if (y2 % this.tileHeight != 0) {
                        y2 += this.tileHeight - (y2 % this.tileHeight);
                    }

                    //calculate start tile
                    int startX = (int) (x / this.tileWidth);
                    int startY = (int) (y / this.tileHeight);

                    if (startX < 0) {
                        startX = 0;
                    }

                    if (startY < 0) {
                        startY = 0;
                    }

                    int endX = (int) (x2 / this.tileWidth);
                    int endY = (int) (y2 / this.tileHeight);

                    if (endX > this.canPassTiles.length) {
                        endX = this.canPassTiles.length - 1;
                    }

                    if (endY > this.canPassTiles[0].length) {
                        endY = this.canPassTiles[0].length - 1;
                    }

                    //System.out.println("collision object found, X: " + startX + ", Y: " + startY + ", endX: " + endX + ", endY: " + endY);

                    //set boolean for every tile
                    for (int i = startX; i < endX; i++) {
                        for (int k = startY; k < endY; k++) {
                            this.canPassTiles[i][k] = false;
                        }
                    }
                }
            }
        }

        //set loaded flag
        this.loaded = true;

        Gdx.app.debug("TiledMapCollision", "tiled collisions loaded successfully.");
    }

    @Override
    public boolean canPassTile(int tileX, int tileY) {
        if (!this.loaded) {
            throw new IllegalStateException("TiledMapCollision isnt loaded yet, call load() before.");
        }

        if (tileX < 0 || tileX > canPassTiles.length) {
            throw new IllegalArgumentException("tileX index has to be >= 0 and tileX <= map width (" + canPassTiles.length + "), current x: " + tileX);
        }

        if (tileY < 0 || tileY > canPassTiles[0].length) {
            throw new IllegalArgumentException("tileY index has to be >= 0 and tileY <= map width (" + canPassTiles[0].length + "), current x: " + tileX);
        }

        return this.canPassTiles[tileX][tileY];
    }

    protected float getMapWidth () {
        return this.tileCountX * this.tileWidth;
    }

    protected float getMapHeight () {
        return this.tileCountY * this.tileHeight;
    }

    @Override
    public boolean canPass(float xPos, float yPos) {
        //check, if coordinates belongs to this map
        if (xPos < this.mapX || xPos > this.mapX + this.getMapWidth()) {
            throw new IllegalArgumentException("x position is wrong, this coordinates doesnt belongs to this map.");
        }

        if (yPos < this.mapY || yPos > this.mapY + this.getMapHeight()) {
            throw new IllegalArgumentException("y position is wrong, this coordinates doesnt belongs to this map.");
        }

        //calculate relative position
        float relX = xPos - this.mapX;
        float relY = yPos - this.mapY;

        //get tile index
        int tileX = (int) (relX / this.tileWidth);
        int tileY = (int) (relY / this.tileHeight);

        return this.canPassTile(tileX, tileY);
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public void drawDebugOverlay(SpriteBatch batch, Color passColor, Color collideColor) {
        for (int x = 0; x < this.tileCountX; x++) {
            for (int y = 0; y < this.tileCountY; y++) {
                float xPos = x * this.tileWidth + this.mapX;
                float yPos = y * this.tileHeight + this.mapY;

                if (canPassTile(x, y)) {
                    SpriteBatcherUtils.drawRectangle(batch, xPos, yPos, this.tileWidth, this.tileHeight, passColor);
                } else {
                    SpriteBatcherUtils.drawRectangle(batch, xPos, yPos, this.tileWidth, this.tileHeight, collideColor);
                }
            }
        }
    }

    @Override
    public void dispose() {
        this.canPassTiles = null;
    }

}
