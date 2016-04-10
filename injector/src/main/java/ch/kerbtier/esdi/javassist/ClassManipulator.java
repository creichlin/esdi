package ch.kerbtier.esdi.javassist;

import java.io.FileOutputStream;
import java.lang.annotation.Annotation;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.NotFoundException;

public class ClassManipulator {
  
  private CtClass subject;
  private ClassManipulatorLogger logger;
  
  private Class injectClass;

  public ClassManipulator(CtClass subject, ClassManipulatorLogger logger) {
    this.subject = subject;
    this.logger = logger;
    try {
      // for Inject annotations check we need to load the Inject annotation trough the same classloader like the annotations we check for
      injectClass = subject.getClassPool().getClassLoader().loadClass("ch.kerbtier.esdi.Inject");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public ClassManipulator(CtClass subject) {
    this.subject = subject;
    this.logger = new ClassManipulatorLogger() {
      
      @Override
      public void log(String className, String field, String type) {
        // log nothing, logging is for whiney whimps
      }

      @Override
      public void warn(String description) {
        
      }
    };
  }
  
  public void manipulate() {
    
    if(subject.isAnnotation()) {
      return;
    }

    if(subject.isInterface()) {
      return;
    }
    
    if(subject.isEnum()) {
      return;
    }
    
    for(CtField field: subject.getDeclaredFields()) {
      
      try {
        Annotation usedAnnotation = null;
        for(Object annotation :field.getAnnotations()) {
          boolean validAnnotation = annotation.getClass().isAssignableFrom(injectClass);
          validAnnotation = validAnnotation || ((Annotation)annotation).annotationType().getAnnotation(injectClass) != null;
          
          if(validAnnotation) {
            if(usedAnnotation != null) {
              throw new RuntimeException("multiple injection annotations for field " + field);
            }
            usedAnnotation = (Annotation)annotation;
          }
          
        }
        
        if(usedAnnotation != null) {
          for(CtConstructor constructor: subject.getConstructors()) {
            try {
              
              String code = "$0." + field.getName() + " = (" + field.getType().getName() + ")ch.kerbtier.esdi.Esdi.get(" + field.getType().getName() + ".class, " + usedAnnotation.annotationType().getCanonicalName() + ".class);";
              logger.log(subject.getName(), field.getName(), field.getType().getName());
              constructor.insertBeforeBody(code);
            } catch (CannotCompileException e) {
              throw new RuntimeException(e);
            } catch (NotFoundException e) {
              throw new RuntimeException(e);
            }
          }
        }
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void write(FileOutputStream fop) {
    try {
      fop.write(subject.toBytecode());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public interface ClassManipulatorLogger {
    void log(String className, String field, String type);
    void warn(String description);
  }
}
