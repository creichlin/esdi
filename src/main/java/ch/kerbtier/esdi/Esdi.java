package ch.kerbtier.esdi;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ch.kerbtier.esdi.providers.DefaultProvider;

/**
 * Static class for configuration of injection.
 * 
 * By default the default {@see Inject} annotation is configured and will use a {@see providers.DefaultProvider}
 * 
 * To add custom injection annotations one can use:
 * 
 *     Esdi.register(InjectSingleton.class, new SingletonProvider());
 * 
 * With this configuration line, fields that are annotated with InjectSingleton
 * will be injected with an instance that got obtained by the provided
 * SingletonProvider instance.
 * 
 * To configure values that can be injected use:
 * 
 *     Esdi.onRequestFor(DefaultExample.class).deliver(DefaultExample.class);
 *     Esdi.onRequestFor(SingletonExample.class).with(InjectSingleton.class).deliver(SingletonExample.class);
 * 
 * Those two line define what should be done if a field with a type should be
 * injected with a certain injection annotation. {@see #onRequestFor(Class)}
 * 
 */

public class Esdi {

  private static Esdi instance;
  private Map<Id, RequestImpl> creators = new HashMap<>();
  private Map<Class<? extends Annotation>, Provider> providers = new HashMap<>();

  static Logger logger = Logger.getLogger(Esdi.class.getName());

  // static methods

  static synchronized Esdi getInstance() {
    if (instance == null) {
      instance = new Esdi();
    }
    return instance;
  }

  /**
   * 
   * start configuring an injectable resource.
   * 
   * Will return a {@see Request} instance which needs to be used to configure how
   * to obtain such a resource.
   * 
   * Common usecases are:
   * 
   *     Esdi.onRequestFor(TestInterface.class).deliver(TestImplementation.class);
   * 
   * To allow TestInterface to be injectable by default {@see Inject} annotation using
   * the implicitely configured {@see providers.DefaultProvider} which creates a new instance every single time when injected.
   *     
   *     Esdi.onRequestFor(TestInterface.class).with(MyAnnotation.class).deliver(TestImplementation.class);
   *     
   * This allows injection of fields by type TestInterface by annotating them with MyAnnotation. Returned are instances
   * of TestImplementation. How they are created/fetched is up to the configured Provider for MyAnnotation though.
   * 
   * @param class1
   *          the type which the resource must implement.
   * @return Request implementation for further configuration.
   * 
   */
  public static Request onRequestFor(Class<?> class1) {
    return new RequestImpl(getInstance(), class1);
  }

  /**
   * Can be used to get an instance of a configured injectable class. In normal circumstances it's not necessary to
   * call this method. The normal way would be to annotate a field for injection and let the magic (aspectJ) do it.
   * 
   * It will be used for testing tough.
   * 
   * @return the instance that should be injected
   */
  public static <T> T get(Class<T> toDeliver, Annotation annotation) {
    // use annotationType() to get class of annotation interface, otherwise some
    // weird proxy implementation is returned.
    Class<? extends Annotation> annotationClass = annotation.annotationType();
    return get(toDeliver, annotation, annotationClass);
  }
  
  public static <T> T get(Class<T> toDeliver, Class<? extends Annotation> annotationClass) {
    return get(toDeliver, null, annotationClass);
  }

  @SuppressWarnings("unchecked")
  private static <T> T get(Class<T> toDeliver, Annotation annotation, Class<? extends Annotation> annotationClass) {
    Id id = new Id(toDeliver, annotationClass);
    RequestImpl creator = getInstance().getRequest(id);
    if (creator != null) {
      Provider provider = getInstance().getProvider(annotationClass);
      return (T) provider.get(creator, annotation);
    } else { // no creator found
      logger.info("no provider for annotation " + annotationClass);
    }
    return null;
  }
  
  /**
   * registers an annotation to annotate fields for dependency injection and
   * sets the corresponding Provider who actually Provides the requested
   * instances.
   */
  public static void register(Class<? extends Annotation> annotation, Provider provider) {
    getInstance().reg(annotation, provider);
  }

  /**
   * discards all configuration. will not be needed in normal circumstances.
   */
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
