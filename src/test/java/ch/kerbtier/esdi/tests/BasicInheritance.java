package ch.kerbtier.esdi.tests;

import java.lang.annotation.Annotation;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.esdi.Inject;
import ch.kerbtier.esdi.model.DefaultExample;
import ch.kerbtier.esdi.model.DefaultInterface;
import ch.kerbtier.esdi.model.Pojo;
import static org.testng.Assert.*;

public class BasicInheritance {

  @BeforeMethod
  public void setup() {
    Esdi.onRequestFor(DefaultInterface.class).deliver(DefaultExample.class);
  }

  @AfterMethod
  public void shutDown() {
    Esdi.clear();
  }

  @Test
  public void checkDefaultImplementation() {
    DefaultInterface a = Esdi.get(DefaultInterface.class, new InjectImpl());
    assertEquals(a.getClass(), DefaultExample.class);
  }

  @Test
  public void checkContainer() {
    Pojo pojo = new Pojo();
    
    assertEquals(pojo.getDefaultInterface().getClass(), DefaultExample.class);
  }
  
  class InjectImpl implements Inject {
    @Override
    public Class<? extends Annotation> annotationType() {
      return Inject.class;
    }
  }
}
