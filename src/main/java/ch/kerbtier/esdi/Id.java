package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

public class Id {
  private final Class<?> target;
  private final Class<? extends Annotation> annotation;

  public Id(Class<?> target, Class<? extends Annotation> annotation) {
    this.target = target;
    this.annotation = annotation;
  }

  public Class<?> getTarget() {
    return target;
  }

  public Class<? extends Annotation> getAnnotation() {
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
