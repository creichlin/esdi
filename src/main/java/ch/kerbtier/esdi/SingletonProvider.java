package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class SingletonProvider implements Provider {

  private Map<Class<? extends Object>, Object> instances = new HashMap<>();
  private DefaultProvider defaultProvider = new DefaultProvider();
  
  @Override
  public Object get(Class<? extends Object> target, Class<? extends Object> implementation, Annotation annotation) {
    Object impl = instances.get(target);
    if(impl == null) {
      synchronized(instances) {
        impl = instances.get(target);
        if(impl == null) {
          impl = defaultProvider.get(target, implementation, annotation);
          instances.put(target, impl);
        }
      }
    }
    return impl;
  }
}
