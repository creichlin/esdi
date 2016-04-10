package ch.kerbtier.esdi.javassist;

import javassist.ClassPool;
import javassist.Loader;


/**
 * CtClass.getAnnotations() loads the annotations with the classloader given in ClassPool.getClassLoader()
 * 
 * Per default this is the ContextClassLoader which would be tghe gradle context.
 * 
 * This ClassPool returns the loader of this ClassPool with all classpath elements set by addClasspath(...)
 * 
 * If not used it will result in ClassNotFoundExceptions for Annotations
 */
public class LocalClasspathClassPool extends ClassPool {
  
  private Loader loader;
  
  public LocalClasspathClassPool() {
    super(true);
    this.loader = new Loader(this);
  }

  @Override
  public ClassLoader getClassLoader() {
    return loader;
  }
  
  
}
