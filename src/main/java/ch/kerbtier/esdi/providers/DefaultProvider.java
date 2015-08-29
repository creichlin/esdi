package ch.kerbtier.esdi.providers;

import java.lang.annotation.Annotation;

import ch.kerbtier.esdi.Configuration;
import ch.kerbtier.esdi.Provider;

/**
 * default provider creates a new instance for each request
 * 
 * @author creichlin
 *
 */
public class DefaultProvider implements Provider {

  @Override
  public Object get(Configuration configuration, Annotation annotation) {
    try {
      if (configuration.getInstance() != null) {
        return configuration.getInstance();
      } else {
        return configuration.getImplementation().newInstance();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
