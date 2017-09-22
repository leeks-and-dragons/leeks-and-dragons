package de.leeksanddragons.engine.exception;

import de.leeksanddragons.engine.entity.annotation.InjectComponent;

/**
 * Exception, which is thrown when a required component wasn't found.
 *
 * This annotation orinally belongs to spacechaos.de project
 *
 * @link spacechaos.de
 * 
 * @see InjectComponent The respective annotation.
 */
public class RequiredComponentNotFoundException extends RuntimeException {

    public RequiredComponentNotFoundException(String message) {
        super(message);
    }

}
