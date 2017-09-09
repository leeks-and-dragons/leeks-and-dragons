package de.leeksanddragons.engine.exception;

/**
 * Exception, which is thrown, if an loaded mod.json file doesnt contains all required keys.
 *
 * Created by Justin on 09.09.2017.
 */
public class InvalideModJSONException extends Exception {

    public InvalideModJSONException (String message) {
        super(message);
    }

}
