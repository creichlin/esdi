package ch.kerbtier.esdi.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.esdi.model.InjectSingleton;
import ch.kerbtier.esdi.model.InjectThreadLocal;
import ch.kerbtier.esdi.model.Pojo;
import ch.kerbtier.esdi.model.SingletonExample;
import ch.kerbtier.esdi.model.ThreadLocalExample;
import ch.kerbtier.esdi.providers.SingletonProvider;
import ch.kerbtier.esdi.providers.ThreadLocalProvider;
import static org.testng.Assert.*;

public class Clear {

  private SingletonProvider singletonProvider = new SingletonProvider(); 
  private ThreadLocalProvider threadLocalProvider = new ThreadLocalProvider(); 
  
  @BeforeMethod
  public void setup() {
    Esdi.register(InjectSingleton.class, singletonProvider);
    Esdi.register(InjectThreadLocal.class, threadLocalProvider);
    Esdi.onRequestFor(SingletonExample.class).with(InjectSingleton.class).deliver(SingletonExample.class);
    Esdi.onRequestFor(ThreadLocalExample.class).with(InjectThreadLocal.class).deliver(ThreadLocalExample.class);
  }

  @AfterMethod
  public void shutDown() {
    Esdi.clear();
  }

  @Test
  public void checkSingletonClear() {
    SingletonExample s1 = new Pojo().getSingleton();
    SingletonExample s2 = new Pojo().getSingleton();
    singletonProvider.clear();
    SingletonExample s3 = new Pojo().getSingleton();
    
    System.out.println(s1 + " " + s2 + " " + s3);
    
    assertEquals(s1, s2);
    assertNotEquals(s1, s3);
  }

  @Test
  public void checkThreadLocalClear() {
    ThreadLocalExample s1 = new Pojo().getThreadLocal();
    ThreadLocalExample s2 = new Pojo().getThreadLocal();
    threadLocalProvider.clear();
    ThreadLocalExample s3 = new Pojo().getThreadLocal();

    System.out.println(s1 + " " + s2 + " " + s3);

    assertEquals(s1, s2);
    assertNotEquals(s1, s3);
  }
}
