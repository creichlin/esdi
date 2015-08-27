package ch.kerbtier.esdi.iteration1;

import ch.kerbtier.esdi.Inject;

@Inject
public class Container {
  
  public Container() {

 }
  
  @Inject
  private AInterface aField;
  
  @Inject
  private BImplementation bField;
  
  public AInterface getAField() {
    return aField;
  }
  public BInterface getBField() {
    return bField;
  }
  
  
}
