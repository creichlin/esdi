package ch.kerbtier.esdi.providers;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import ch.kerbtier.esdi.Configuration;
import ch.kerbtier.esdi.Provider;

/**
 * 
 * Singleton provider will return always the same instance for the same type. It
 * will be created when needed the first time and always reused.
 * 
 * If multiple SingletonProviders are registered for different annotations
 * though, the same type can have multiple instances, one for each
 * SingletonProvider.
 * 
 * For creating the singletons the very first time a {@see DefaultProvider} is
 * used.
 * 
 * @author creichlin
 *
 */
public class SingletonProvider implements Provider {

  private Map<Class<? extends Object>, Object> instances = new HashMap<>();
  private DefaultProvider defaultProvider = new DefaultProvider();

  @Override
  public Object get(Configuration configuration, Annotation annotation) {
    synchronized (instances) {
      Object impl = instances.get(configuration.getTarget());
      if (impl == null) {
        impl = defaultProvider.get(configuration, annotation);
        instances.put(configuration.getTarget(), impl);
      }
      return impl;
    }
  }

  @Override
  public void clear() {
    synchronized (instances) {
      instances.clear();
    }
  }
}
