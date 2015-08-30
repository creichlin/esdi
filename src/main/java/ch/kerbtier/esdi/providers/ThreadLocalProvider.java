package ch.kerbtier.esdi.providers;

import java.lang.annotation.Annotation;

import ch.kerbtier.esdi.Configuration;
import ch.kerbtier.esdi.Provider;

/**
 * Gives always the same instance of a type when called from the same thread.
 * There is one instance for each thread.
 * 
 * So kind of like a SingletonProvider but one for each thread. If your
 * application has only one thread it behaves exactly the same.
 * 
 * It's actually implemented using a ThreadLocal and {@see SingletonProvider}.
 * 
 */
public class ThreadLocalProvider implements Provider {

  private ThreadLocal<SingletonProvider> threadLocal = new ThreadLocal<>();

  @Override
  public Object get(Configuration configuration, Annotation annotation) {
    SingletonProvider sp = threadLocal.get();
    if (sp == null) {
      sp = new SingletonProvider();
      threadLocal.set(sp);
    }
    return sp.get(configuration, annotation);
  }

  @Override
  public void clear() {
    if (threadLocal.get() != null) {
      // the singletonProvider in a thread local cannot be accessed
      // simultaneously so no synchronisation needed
      threadLocal.get().clear();
    }
  }
}
