package ch.kerbtier.esdi.tests;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.esdi.model.DefaultExample;
import ch.kerbtier.esdi.model.InjectSingleton;
import ch.kerbtier.esdi.model.InjectThreadLocal;
import ch.kerbtier.esdi.model.Pojo;
import ch.kerbtier.esdi.model.SingletonExample;
import ch.kerbtier.esdi.providers.SingletonProvider;
import ch.kerbtier.esdi.providers.ThreadLocalProvider;
import static org.testng.Assert.*;

public class DeliverThreadLocalInstanceTests {
  
  private ThreadLocal<DefaultExample> defaultExample = new ThreadLocal<>();
  private ThreadLocal<SingletonExample> singletonExample = new ThreadLocal<>();
  
  
  @BeforeMethod
  public void setUp() {
    Esdi.register(InjectSingleton.class, new SingletonProvider());
    Esdi.register(InjectThreadLocal.class, new ThreadLocalProvider());
    
    Esdi.onRequestFor(DefaultExample.class).deliverThreadLocal(defaultExample);
    Esdi.onRequestFor(SingletonExample.class).with(InjectSingleton.class).deliverThreadLocal(singletonExample);
  }
  
  
  @AfterMethod
  public void shutdown() {
    Esdi.clear();
  }
  
  @Test
  public void checkSingletonEquals() {
    singletonExample.set(new SingletonExample());
    SingletonExample e1 = new Pojo().getSingleton();
    assertEquals(e1, singletonExample.get());
  }
  
  @Test
  public void checkSingletonFromDifferentThreadEquals() {
    final List<SingletonExample> ts = new ArrayList<>();
    
    singletonExample.set(new SingletonExample());
    ts.add(new Pojo().getSingleton());
    
    new Thread(){
      @Override
      public void run() {
        singletonExample.set(new SingletonExample());
        ts.add(new Pojo().getSingleton());
      }
      
    }.start();
    
    waitAMoment();
    
    assertEquals(ts.get(0), ts.get(1));
  }


  @Test
  public void checkDefaultFromDifferentThreadNotEquals() {
    final List<DefaultExample> ts = new ArrayList<>();
    
    defaultExample.set(new DefaultExample());
    ts.add(new Pojo().getDef());
    
    new Thread(){
      @Override
      public void run() {
        defaultExample.set(new DefaultExample());
        ts.add(new Pojo().getDef());
      }
      
    }.start();
    
    waitAMoment();
    
    assertNotEquals(ts.get(0), ts.get(1));
  }


  private void waitAMoment() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
