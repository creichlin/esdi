package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

/**
 * Is used to create/fetch and return instances which will be injected. For each
 * inject annotation there is one instance of a provider.
 * 
 * @author creichlin
 *
 */
public interface Provider {

  /**
   * creates/fetches and returns the implementation of the requested interface
   * 
   * @param requested
   *          client requests implementation of this type
   * 
   * @param implementation
   *          injection lib is configured to use this implementation
   * 
   * @param annotation
   *          can be used to identify which exact value to return
   * 
   * @return the instance of type requested
   */
  Object get(Configuration configuration, Annotation annotation);

}
