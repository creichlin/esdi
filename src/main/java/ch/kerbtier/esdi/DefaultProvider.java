package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

/**
 * default provider creates a new instance for each request
 * @author creichlin
 *
 */
public class DefaultProvider implements Provider {

  @Override
  public Object get(Class<? extends Object> target, Class<? extends Object> implementation, Annotation annotation) {
    try {
      return implementation.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
