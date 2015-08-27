package ch.kerbtier.esdi.iteration2;

import ch.kerbtier.esdi.Inject;

@Inject
public class Pojo {

  @InjectSingleton
  private SingletonExample singleton;
  @InjectThreadLocal
  private ThreadLocalExample threadLocal;
  @Inject
  private DefaultExample def;

  public SingletonExample getSingleton() {
    return singleton;
  }

  public ThreadLocalExample getThreadLocal() {
    return threadLocal;
  }

  public DefaultExample getDef() {
    return def;
  }
}
