package ch.kerbtier.esdi;

import java.util.HashMap;
import java.util.Map;

public class Esdi {
  
  private static Esdi instance;
  private Map<Id, Creator> creators = new HashMap<>();
  
  public static Esdi getInstance() {
    if(instance == null) {
      synchronized(Esdi.class) {
        if(instance == null) {
          instance = new Esdi();
        }
      }
    }
    return instance;
  }
  
  private Esdi() {
    
  }

  public static Request onRequestFor(Class<?> class1) {
    return new RequestImpl(getInstance(), class1);
  }

  public Creator getCreator(Id id) {
    return creators.get(id);
  }
  
  public void setCreator(Id id, Creator creator) {
    synchronized(creators) {
      if(creators.containsKey(id)) {
        throw new RuntimeException("implementation for " + id + " already registered");
      }
      creators.put(id, creator);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(Class<T> toDeliver) {
    Id id = new Id(toDeliver, Inject.class);
    Creator creator = getInstance().getCreator(id);
    if(creator != null) {
      return (T) getInstance().getCreator(id).get();
    }
    return null;
  }

  public static void clear() {
    getInstance().clearEverything();
  }

  private void clearEverything() {
    creators.clear();
  }
  
}
