package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public aspect ConstructorInjectorAspect {

  // execution(* new(..)) && within(hasfield(@B * *))
  // execution(* new(..)) && within(hasfield(@B * *))

  pointcut injectConstructor(Object o): execution((@Inject *).new(..)) && target(o);

  before(Object o): injectConstructor(o) {
    for (Field f : o.getClass().getDeclaredFields()) {
      for (Annotation annotation : f.getAnnotations()) {
        
        // if annotation is of type @Inject
        boolean doInject = annotation instanceof Inject;
        
        if(!doInject) {
          // or annotation is annotated with @Inject
          doInject = annotation.annotationType().getAnnotation(Inject.class) != null;
        }
        
        if (doInject) {
          // set the field with value fetched from esdi
          Object instance = Esdi.get(f.getType(), annotation);
          if (instance != null) {
            try {
              f.setAccessible(true);
              f.set(o, instance);
            } catch (IllegalArgumentException e) {
              e.printStackTrace();
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }
}
