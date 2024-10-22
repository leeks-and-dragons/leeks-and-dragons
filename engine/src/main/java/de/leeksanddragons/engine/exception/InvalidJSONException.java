package de.leeksanddragons.engine.exception;

/**
 * Indicates an invalid JSON syntax.
 */
public class InvalidJSONException extends RuntimeException {

    public InvalidJSONException(String message) {
        super(message);
    }

}
