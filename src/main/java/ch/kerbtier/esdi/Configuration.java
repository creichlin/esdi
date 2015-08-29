package ch.kerbtier.esdi;

public interface Configuration {
  
  Class<? extends Object> getTarget();
  Class<? extends Object> getImplementation();
  Object getInstance();
}
