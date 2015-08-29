package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

/**
 * is used to configure injection
 * 
 * Can only be obtained by:
 * 
 *     Esdi.onRequestFor(Intaface.class);
 * 
 * @author creichlin
 *
 */
public interface Request {

  void deliver(Class<?> class1);

  Request with(Class<? extends Annotation> annotation);

  void deliverInstance(Object instance);

}
