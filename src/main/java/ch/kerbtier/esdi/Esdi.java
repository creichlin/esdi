package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Esdi {

  private static Esdi instance;
  private Map<Id, RequestImpl> creators = new HashMap<>();
  private Map<Class<? extends Annotation>, Provider> providers = new HashMap<>();

  static Logger logger = Logger.getLogger(Esdi.class.getName());

  // static
  public static Esdi getInstance() {
    if (instance == null) {
      synchronized (Esdi.class) {
        if (instance == null) {
          instance = new Esdi();
        }
      }
    }
    return instance;
  }

  public static Request onRequestFor(Class<?> class1) {
    return new RequestImpl(getInstance(), class1);
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(Class<T> toDeliver, Class<? extends Annotation> annotationClass, Annotation annotation) {
    Id id = new Id(toDeliver, annotationClass);
    RequestImpl creator = getInstance().getRequest(id);
    if (creator != null) {
      Provider provider = getInstance().getProvider(annotationClass);
      return (T) provider.get(id.getTarget(), creator.getImplementation(), annotation);
    } else { // no creator found
      logger.info("no provider for annotation " + annotationClass);
    }
    return null;
  }

  public static void register(Class<? extends Annotation> annotation, Provider provider) {
    getInstance().reg(annotation, provider);
  }

  public static void clear() {
    getInstance().clearEverything();
    instance = null;
  }

  // instance
  private Esdi() {
    providers.put(Inject.class, new DefaultProvider());
  }

  RequestImpl getRequest(Id id) {
    return creators.get(id);
  }

  Provider getProvider(Class<? extends Annotation> annotation) {
    return providers.get(annotation);
  }

  void setCreator(Id id, RequestImpl requestImpl) {
    synchronized (creators) {
      if (creators.containsKey(id)) {
        throw new RuntimeException("implementation for " + id + " already registered");
      }
      creators.put(id, requestImpl);
    }
  }

  void reg(Class<? extends Annotation> annotation, Provider provider) {
    providers.put(annotation, provider);
  }

  private void clearEverything() {
    creators.clear();
    providers.clear();
  }

}
