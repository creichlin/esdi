package ch.kerbtier.esdi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation to mark a field that needs injection.
 * 
 * Can also be used to mark other annotations so they can mark fields for
 * injection.
 * 
 * @author creichlin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface Inject {

}
