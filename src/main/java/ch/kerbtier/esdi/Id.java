package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

/**
 * This ID class is used to map concrete implementations to their interfaces
 * used by injection.
 * 
 * For identification a interface plus one annotation class is used.
 * 
 * If a value needs to be different for a parameterized annotation a custom
 * Provider needs to be used to return different values for the concrete
 * annotation instances.
 * 
 * @author creichlin
 *
 */
class Id {

  private final Class<?> target;
  private final Class<? extends Annotation> annotation;

  Id(Class<?> target, Class<? extends Annotation> annotation) {
    this.target = target;
    this.annotation = annotation;
  }

  Class<? extends Object> getTarget() {
    return target;
  }

  Class<? extends Annotation> getAnnotation() {
    return annotation;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((annotation == null) ? 0 : annotation.hashCode());
    result = prime * result + ((target == null) ? 0 : target.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Id other = (Id) obj;
    if (annotation == null) {
      if (other.annotation != null)
        return false;
    } else if (!annotation.equals(other.annotation))
      return false;
    if (target == null) {
      if (other.target != null)
        return false;
    } else if (!target.equals(other.target))
      return false;
    return true;
  }
}
