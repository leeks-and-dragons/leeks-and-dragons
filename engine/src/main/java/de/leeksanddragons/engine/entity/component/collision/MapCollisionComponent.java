package de.leeksanddragons.engine.entity.component.collision;

import com.badlogic.gdx.math.Vector2;
import de.leeksanddragons.engine.collision.MapCollision;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.annotation.InjectComponent;
import de.leeksanddragons.engine.entity.component.MoveComponent;
import de.leeksanddragons.engine.entity.component.PositionComponent;
import de.leeksanddragons.engine.entity.impl.BaseComponent;
import de.leeksanddragons.engine.listener.BeforeMoveListener;
import de.leeksanddragons.engine.map.IRegion;
import de.leeksanddragons.engine.screen.IScreenGame;

/**
 * Created by Justin on 04.10.2017.
 */
public class MapCollisionComponent extends BaseComponent implements BeforeMoveListener {

    @InjectComponent (nullable = false)
    protected PositionComponent positionComponent;

    @InjectComponent (nullable = false)
    protected MoveComponent moveComponent;

    @InjectComponent (nullable = false)
    protected MoveBoundingBoxComponent moveBoundingBoxComponent;

    //instance of map collision
    protected MapCollision mapCollision = null;

    //instance of region
    protected IRegion region = null;

    //width and height of tiles
    protected int tileWidth = 32;
    protected int tileHeight = 32;

    /**
    * default constructor
     *
     * @param region instance of region
    */
    public MapCollisionComponent (IRegion region) {
        //set region
        this.setRegion(region);
    }

    @Override
    protected void onInit(IScreenGame game, Entity entity) {
        //register move listener
        this.moveComponent.addBeforeMoveListener(this);
    }

    public void setRegion (IRegion region) {
        if (region == null) {
            throw new NullPointerException("region cannot be null.");
        }

        this.region = region;
    }

    @Override
    public Vector2 beforeMove(Vector2 oldPosition, Vector2 newPosition, PositionComponent positionComponent) {
        //calculate speed of entity
        float deltaX = newPosition.x - oldPosition.x;
        float deltaY = newPosition.y - oldPosition.y;

        //calculate width and height in tiles
        int widthInTiles = (int) (moveBoundingBoxComponent.getWidth() / this.tileWidth);
        int heightInTiles = (int) (moveBoundingBoxComponent.getHeight() / this.tileHeight);

        //correct width, every started tile is counted
        if (moveBoundingBoxComponent.getWidth() % this.tileWidth != 0) {
            widthInTiles++;
        }

        //increment, because every entity has 2 edges
        //widthInTiles++;

        //correct height, every started tile is counted
        if (moveBoundingBoxComponent.getHeight() % this.tileHeight != 0) {
            heightInTiles++;
        }/* else {
            heightInTiles++;
        }*/

        /*float maxSpeedX = deltaX;
        float maxSpeedY = deltaY;

        //calculate speed in tiles
        int tileSpeedX = (int) (deltaX / this.tileWidth);
        int tileSpeedY = (int) (deltaY / this.tileHeight);

        if (deltaX % this.tileWidth != 0) {
            if (deltaX < 0) {
                tileSpeedX--;
            } else if (deltaX > 0) {
                tileSpeedX++;
            }
        }

        if (deltaY % this.tileHeight != 0) {
            if (deltaY < 0) {
                tileSpeedY--;
            } else if (deltaY > 0) {
                tileSpeedY++;
            }
        }*/

        //check x axis
        if (deltaX < 0) {
            for (float x = newPosition.x; x < positionComponent.getX(); x = x + 32) {
                //check, if all vertical tiles doesnt collides
                for (int y = 0; y < heightInTiles + 1; y++) {
                    //check tile
                    if (!canPass(x, positionComponent.getY() + (y * this.tileHeight))) {
                        if (-(newPosition.x - positionComponent.getX()) < this.tileWidth) {
                            newPosition.x -= (newPosition.x - positionComponent.getX()) % this.tileWidth;
                        } else {
                            newPosition.x += this.tileWidth;
                        }

                        break;
                    }
                }
            }
        } else if (deltaX > 0) {
            for (float x = newPosition.x; x > positionComponent.getX(); x = x - this.tileWidth) {
                //check, if all vertical tiles doesnt collides
                for (int y = 0; y < heightInTiles + 1; y++) {
                    //check tile
                    if (!canPass(x + moveBoundingBoxComponent.getWidth(), positionComponent.getY() + (y * this.tileHeight))) {
                        if (newPosition.x - positionComponent.getX() < this.tileWidth) {
                            newPosition.x -= (newPosition.x - positionComponent.getX()) % this.tileWidth;
                        } else {
                            newPosition.x -= this.tileWidth;
                        }

                        break;
                    }
                }
            }
        }

        //increment, because every entity has 2 edges
        heightInTiles++;

        //check y axis
        if (deltaY < 0) {
            //initalize variables
            float y = newPosition.y;
            float height = moveBoundingBoxComponent.getHeight();

            while (y <= positionComponent.getY()) {
                //check, if all horizontal tiles doesnt collides
                for (int x = 0; x < widthInTiles + 1; x++) {
                    //check tile
                    if (!canPass(positionComponent.getX() + (x * this.tileWidth), y)) {
                        if (-(newPosition.y - positionComponent.getY()) < this.tileHeight) {
                            newPosition.y -= (newPosition.y - positionComponent.getY()) % this.tileHeight;
                        } else {
                            newPosition.y += this.tileHeight;
                        }

                        break;
                    }
                }

                //increment y
                if (height > tileHeight) {
                    y += tileHeight;
                    height -= tileHeight;
                } else {
                    y += height % tileHeight;
                    height = 0;
                }
            }

            /*for (float y = newPosition.y; y < positionComponent.getY(); y = y + this.tileHeight) {
                //check, if all horizontal tiles doesnt collides
                for (int x = 0; x < widthInTiles + 1; x++) {
                    //check tile
                    if (!canPass(positionComponent.getX() + (x * this.tileWidth), y)) {
                        if (-(newPosition.y - positionComponent.getY()) < this.tileHeight) {
                            newPosition.y -= (newPosition.y - positionComponent.getY()) % this.tileHeight;
                        } else {
                            newPosition.y += this.tileHeight;
                        }

                        break;
                    }
                }
            }*/
        } else if (deltaY > 0) {
            //initialize variables
            float y = newPosition.y;
            float height = moveBoundingBoxComponent.getHeight();

            while (y >= positionComponent.getY()) {
                //check, if all vertical tiles doesnt collides
                for (int x = 0; x < widthInTiles + 1; x++) {
                    //check tile
                    if (!canPass(positionComponent.getX() + (x * this.tileWidth), y + moveBoundingBoxComponent.getHeight())) {
                        if (newPosition.y - positionComponent.getY() < this.tileHeight) {
                            newPosition.y -= (newPosition.y - positionComponent.getY()) % moveBoundingBoxComponent.getHeight();
                        } else {
                            //System.out.println("test");
                            newPosition.y -= this.tileHeight;
                        }

                        break;
                    }
                }

                //decrement y
                if (height > tileHeight) {
                    y -= tileHeight;
                    height -= tileHeight;
                } else {
                    y -= height % tileHeight;
                    height = 0;
                }
            }

            /*for (float y = newPosition.y; y > positionComponent.getY(); y = y - this.tileHeight) {
                //check, if all vertical tiles doesnt collides
                for (int x = 0; x < widthInTiles + 1; x++) {
                    //check tile
                    if (!canPass(positionComponent.getX() + (x * this.tileWidth), y + moveBoundingBoxComponent.getHeight())) {
                        if (newPosition.y - positionComponent.getY() < this.tileHeight) {
                            newPosition.y -= (newPosition.y - positionComponent.getY()) % moveBoundingBoxComponent.getHeight();
                        } else {
                            //System.out.println("test");
                            newPosition.y -= this.tileHeight;
                        }

                        break;
                    }
                }
            }*/
        }

        return newPosition;
    }

    protected boolean canPass (float x, float y) {
        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        //quick & dirty fix for NullPointerException
        float mapX = x;
        float mapY = y;

        if (mapX < 0) {
            mapX = 0;
        }

        if (mapY < 0) {
            mapY = 0;
        }

        try {
            //get current map of position
            this.mapCollision = this.region.getMapByPosition(mapX, mapY).getMapCollisions();
        } catch (NullPointerException e) {
            return false;
        }

        return this.mapCollision.canPass(x, y);
    }

    @Override
    public void dispose() {
        //remove move listener
        this.moveComponent.removeBeforeMoveListener(this);
    }

}
