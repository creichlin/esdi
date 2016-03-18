package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

/**
 * Is used to create/fetch and return instances which will be injected. For each
 * inject annotation there is one instance of a provider.
 *
 */
public interface Provider {

  /**
   * creates/fetches and returns the implementation of the requested interface
   * 
   * @return the instance of type requested
   */
  Object get(Configuration configuration, Annotation annotation);
  
  /**
   * clears any hold/cached instances
   */
  void clear();

}
