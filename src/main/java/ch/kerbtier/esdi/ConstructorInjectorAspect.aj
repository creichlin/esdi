package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public aspect ConstructorInjectorAspect {

  // execution(* new(..)) && within(hasfield(@B * *))
  // execution(* new(..)) && within(hasfield(@B * *))

  
  pointcut injectConstructor(Object o): execution((@Inject *).new(..)) && target(o);

  after(Object o): injectConstructor(o) {
    for(Field f: o.getClass().getDeclaredFields()) {
      if(f.isAnnotationPresent(Inject.class)) {
        for(Annotation a: f.getAnnotations()) {
          if(a instanceof Inject) {
            Object instance = Esdi.get(f.getType());
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
