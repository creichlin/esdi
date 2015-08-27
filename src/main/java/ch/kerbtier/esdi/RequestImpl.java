package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

public class RequestImpl implements Request {

  private Class<?> requested;
  private Esdi esdi;
  private Class<? extends Annotation> annotation = Inject.class;
  private Class<?> implementation;

  public RequestImpl(Esdi esdi, Class<?> requested) {
    this.requested = requested;
    this.esdi = esdi;
  }
  
  @Override
  public Request with(Class<? extends Annotation> annotation_) {
    this.annotation = annotation_;
    return this;
  }

  @Override
  public void deliver(Class<?> implementation_) {
    this.implementation = implementation_;
    
    Id id = new Id(requested, annotation);
    esdi.setCreator(id, this);
  }

  Class<? extends Annotation> getAnnotation() {
    return annotation;
  }

  public Class<? extends Object> getImplementation() {
    return implementation;
  }

}
