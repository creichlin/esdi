package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

public class ThreadLocalProvider implements Provider {

  private ThreadLocal<SingletonProvider> threadLocal = new ThreadLocal<>();
  
  @Override
  public Object get(Class<? extends Object> target, Class<? extends Object> implementation, Annotation annotation) {
    SingletonProvider sp = threadLocal.get();
    if(sp == null) {
      sp = new SingletonProvider();
      threadLocal.set(sp);
    }
    return sp.get(target, implementation, annotation);
  }
}
