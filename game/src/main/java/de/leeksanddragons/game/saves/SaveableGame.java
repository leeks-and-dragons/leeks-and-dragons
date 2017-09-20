package de.leeksanddragons.game.saves;

/**
 * Created by Justin on 19.09.2017.
 */
public interface SaveableGame {

    public void load (String path);

    public boolean isNewGame ();

}
