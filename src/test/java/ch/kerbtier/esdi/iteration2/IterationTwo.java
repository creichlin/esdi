package ch.kerbtier.esdi.iteration2;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.esdi.SingletonProvider;
import ch.kerbtier.esdi.ThreadLocalProvider;
import static org.testng.Assert.*;

public class IterationTwo {
  
  @BeforeMethod
  public void setUp() {
    Esdi.register(InjectSingleton.class, new SingletonProvider());
    Esdi.register(InjectThreadLocal.class, new ThreadLocalProvider());
    
    Esdi.onRequestFor(DefaultExample.class).deliver(DefaultExample.class);
    Esdi.onRequestFor(SingletonExample.class).with(InjectSingleton.class).deliver(SingletonExample.class);
    Esdi.onRequestFor(ThreadLocalExample.class).with(InjectThreadLocal.class).deliver(ThreadLocalExample.class);
  }
  
  
  @AfterMethod
  public void shutdown() {
    Esdi.clear();
  }
  
  @Test
  public void checkSingletonEquals() {
    SingletonExample e1 = new Pojo().getSingleton();
    SingletonExample e2 = new Pojo().getSingleton();
    assertEquals(e1, e2);
  }
  
  @Test
  public void checkSingletonFromDifferentThredEquals() {
    SingletonExample e1 = new Pojo().getSingleton();
    final List<SingletonExample> e2 = new ArrayList<>();
    
    new Thread(){
      @Override
      public void run() {
        e2.add(new Pojo().getSingleton());
      }
      
    }.start();
    
    waitAMoment();
    
    assertEquals(e1, e2.get(0));
  }


  private void waitAMoment() {
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void checkDefaultNotEquals() {
    DefaultExample e1 = new Pojo().getDef();
    DefaultExample e2 = new Pojo().getDef();
    assertNotEquals(e1, e2);
  }
  
  @Test
  public void checkThreadLocalEquals() {
    ThreadLocalExample e1 = new Pojo().getThreadLocal();
    ThreadLocalExample e2 = new Pojo().getThreadLocal();
    
    assertEquals(e1, e2);
  }
  
  @Test
  public void checkThreadLocalFromDifferetnThreadsNotEquals() {
    ThreadLocalExample e1 = new Pojo().getThreadLocal();

    final List<ThreadLocalExample> e2 = new ArrayList<>();
    
    new Thread(){
      @Override
      public void run() {
        e2.add(new Pojo().getThreadLocal());
      }
      
    }.start();

    waitAMoment();

    assertNotEquals(e1, e2.get(0));
  }
  
}
