package ch.kerbtier.esdi;

public class CreateAlways implements Creator {

  private Class<?> toCreate;
  
  public CreateAlways(Class<?> toCreate) {
    this.toCreate = toCreate;
  }

  @Override
  public Object get() {
    try {
      return toCreate.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
