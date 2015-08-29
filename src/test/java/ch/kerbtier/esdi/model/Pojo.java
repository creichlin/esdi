package ch.kerbtier.esdi.model;

import ch.kerbtier.esdi.Inject;

@Inject
public class Pojo {

  @InjectSingleton
  private SingletonExample singleton;
  
  @InjectThreadLocal
  private ThreadLocalExample threadLocal;
  
  @Inject
  private DefaultExample def;
  
  @Inject
  private DefaultInterface defaultInterface;

  public SingletonExample getSingleton() {
    return singleton;
  }

  public ThreadLocalExample getThreadLocal() {
    return threadLocal;
  }

  public DefaultExample getDef() {
    return def;
  }

  public DefaultInterface getDefaultInterface() {
    return defaultInterface;
  }
}
