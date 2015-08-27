package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

public interface Request {

  void deliver(Class<?> class1);

  Request with(Class<? extends Annotation> annotation);

}
