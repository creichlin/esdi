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
import ch.kerbtier.esdi.model.ThreadLocalExample;
import ch.kerbtier.esdi.providers.SingletonProvider;
import ch.kerbtier.esdi.providers.ThreadLocalProvider;
import static org.testng.Assert.*;

public class DeliverInstanceTests {
  
  private DefaultExample defaultExample = new DefaultExample();
  private SingletonExample singletonExample = new SingletonExample();
  private ThreadLocalExample threadLocalExample = new ThreadLocalExample();
  
  
  @BeforeMethod
  public void setUp() {
    Esdi.register(InjectSingleton.class, new SingletonProvider());
    Esdi.register(InjectThreadLocal.class, new ThreadLocalProvider());
    
    Esdi.onRequestFor(DefaultExample.class).deliverInstance(defaultExample);
    Esdi.onRequestFor(SingletonExample.class).with(InjectSingleton.class).deliverInstance(singletonExample);
    Esdi.onRequestFor(ThreadLocalExample.class).with(InjectThreadLocal.class).deliverInstance(threadLocalExample);
  }
  
  
  @AfterMethod
  public void shutdown() {
    Esdi.clear();
  }
  
  @Test
  public void checkSingletonEquals() {
    SingletonExample e1 = new Pojo().getSingleton();
    assertEquals(e1, singletonExample);
  }
  
  @Test
  public void checkSingletonFromDifferentThreadEquals() {
    final List<SingletonExample> ts = new ArrayList<>();
    
    new Thread(){
      @Override
      public void run() {
        ts.add(new Pojo().getSingleton());
      }
      
    }.start();
    
    waitAMoment();
    
    assertEquals(ts.get(0), singletonExample);
  }


  private void waitAMoment() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void checkDefaultEquals() {
    DefaultExample e1 = new Pojo().getDef();
    assertEquals(e1, defaultExample);
  }
  
  @Test
  public void checkThreadLocalEquals() {
    ThreadLocalExample e1 = new Pojo().getThreadLocal();
    assertEquals(e1, threadLocalExample);
  }
  
  @Test
  public void checkThreadLocalFromDifferetnThreadsEquals() {
    final List<ThreadLocalExample> tl = new ArrayList<>();
    
    new Thread(){
      @Override
      public void run() {
        tl.add(new Pojo().getThreadLocal());
      }
      
    }.start();

    waitAMoment();

    assertEquals(tl.get(0), threadLocalExample);
  }
  
}
