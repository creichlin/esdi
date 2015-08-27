package ch.kerbtier.esdi.iteration1;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ch.kerbtier.esdi.Esdi;
import ch.kerbtier.esdi.Inject;
import static org.testng.Assert.*;

public class IterationOne {

  @BeforeMethod
  public void setup() {
    Esdi.onRequestFor(AInterface.class).deliver(AImplementation.class);
    Esdi.onRequestFor(BImplementation.class).deliver(BImplementation.class);
  }

  @AfterMethod
  public void shutDown() {
    Esdi.clear();
  }

  @Test
  public void checkAImplementation() {
    AInterface a = Esdi.get(AInterface.class, Inject.class, null);
    assertEquals(a.getClass(), AImplementation.class);
  }

  @Test
  public void checkBImplementation() {
    BImplementation a = Esdi.get(BImplementation.class, Inject.class, null);
    assertEquals(a.getClass(), BImplementation.class);
  }

  @Test
  public void checkContainer() {
    Container c = new Container();
    assertEquals(c.getAField().getClass(), AImplementation.class);
    assertEquals(c.getBField().getClass(), BImplementation.class);
  }
}
