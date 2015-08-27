package ch.kerbtier.esdi;

public class RequestImpl implements Request {
  
  private Class<?> requested;
  private Esdi esdi;
  
  public RequestImpl(Esdi esdi, Class<?> requested) {
    this.requested = requested;
    this.esdi = esdi;
  }

  @Override
  public void deliver(Class<?> delivered) {
      Id id = new Id(requested, Inject.class);
      Creator creator = new CreateAlways(delivered);
      esdi.setCreator(id, creator);
  }

}
