package ch.kerbtier.esdi.providers;

import java.lang.annotation.Annotation;

import ch.kerbtier.esdi.Configuration;
import ch.kerbtier.esdi.Provider;

/**
 * This default provider is used as default and also used by other Providers
 * which get the instance from here and then cache/store it in different ways.
 * 
 * 
 * default provider creates a new instance for each request if class is set. If
 * instance is given returns this instance and if threadlocal is given gets the
 * value of it.
 * 
 */
public class DefaultProvider implements Provider {

  @Override
  public Object get(Configuration configuration, Annotation annotation) {
    try {
      if (configuration.getInstance() != null) {
        return configuration.getInstance();
      } else if (configuration.getThreadLocal() != null) {
        return configuration.getThreadLocal().get();
      } else {
        return configuration.getImplementation().newInstance();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void clear() {
    // no caching so no clearing necessary
  }
}
