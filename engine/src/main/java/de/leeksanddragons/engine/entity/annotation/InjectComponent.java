package de.leeksanddragons.engine.entity.annotation;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.EntityManager;
import de.leeksanddragons.engine.entity.IComponent;
import de.leeksanddragons.engine.exception.RequiredComponentNotFoundException;

import java.lang.annotation.*;

/**
 * Identifies injectable {@linkplain IComponent component-fields} and classes
 * with those fields.
 *
 * This annotation orinally belongs to spacechaos.de project
 *
 * @see Entity#init(de.leeksanddragons.engine.screen.IScreenGame, EntityManager)
 *
 * @link spacechaos.de
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectComponent {

    /**
     * @return Indicates whether an annotated field/the field of the annotated
     *         type can be null. If set to true, an
     *         {@linkplain RequiredComponentNotFoundException} is thrown.
     *         Default value: true.
     */
    boolean nullable() default true;

    /**
     * @return Indicates whether the inherited fields of the annotated type
     *         should also get injected. Default value: true.
     */
    boolean injectInherited() default true;

}
