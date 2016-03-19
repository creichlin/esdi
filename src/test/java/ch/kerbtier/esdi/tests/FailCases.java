package ch.kerbtier.esdi.tests;

import static org.testng.Assert.assertEquals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.esdi.providers.DefaultProvider;

public class FailCases {
  @BeforeMethod
  public void setup() {
    Esdi.register(InjectFail.class, new DefaultProvider());
    Esdi.onRequestFor(String.class).with(InjectFail.class).deliverInstance("FAIL");
  }

  @AfterMethod
  public void shutDown() {
    Esdi.clear();
  }

  @Test
  public void checkGetFail() {
    assertEquals(new FailPojo().getData(), null);
  }
  
  class FailPojo {
    @InjectFail
    private String data;
    
    public String getData() {
      return data;
    }
  }
  
  // Missing @Inject annotation
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE, ElementType.FIELD })
  public @interface InjectFail {

  }
}
