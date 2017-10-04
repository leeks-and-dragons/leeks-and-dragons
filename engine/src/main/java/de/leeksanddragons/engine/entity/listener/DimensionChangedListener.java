package de.leeksanddragons.engine.entity.listener;

/**
 * Created by Justin on 04.10.2017.
 */
@FunctionalInterface
public interface DimensionChangedListener {

    /**
    * method which is executed if texture (region) dimension (size) was changed
     *
     * @param oldWidth old width of texture
     * @param oldHeight old height of texture
     * @param newWidht new width of texture
     * @param newHeight new height of texture
    */
    public void onDimensionChanged (float oldWidth, float oldHeight, float newWidht, float newHeight);

}
