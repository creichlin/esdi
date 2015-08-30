package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;

class RequestImpl implements Request, Configuration {

  private Esdi esdi;
  private Class<?> requested;
  private Class<? extends Annotation> annotation = Inject.class;
  private Class<?> implementation;
  private ThreadLocal<? extends Object> source;
  private Object instance;

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
    
    add();
  }

  @Override
  public void deliverInstance(Object instance_) {
    this.instance = instance_;
    
    add();
  }

  @Override
  public void deliverThreadLocal(ThreadLocal<? extends Object> source_) {
    this.source = source_;
    
    add();
  }

  private void add() {
    Id id = new Id(requested, annotation);
    esdi.setCreator(id, this);
  }

  Class<? extends Annotation> getAnnotation() {
    return annotation;
  }

  @Override
  public
  Class<? extends Object> getImplementation() {
    return implementation;
  }
  
  @Override
  public
  Object getInstance() {
    return instance;
  }

  @Override
  public Class<? extends Object> getTarget() {
    return requested;
  }

  @Override
  public ThreadLocal<? extends Object> getThreadLocal() {
    return source;
  }
}
