package ch.kerbtier.esdi.javassist;

import java.io.FileOutputStream;
import java.lang.annotation.Annotation;

import ch.kerbtier.esdi.Inject;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.NotFoundException;

public class ClassManipulator {
  
  private CtClass subject;
  
  public ClassManipulator(CtClass subject) {
    this.subject = subject;
  }
  
  public void manipulate() {
    for(CtField field: subject.getDeclaredFields()) {
      
      try {
        Annotation usedAnnotation = null;
        for(Object annotation :field.getAnnotations()) {
          boolean validAnnotation = annotation instanceof Inject;
          validAnnotation = validAnnotation || ((Annotation)annotation).annotationType().getAnnotation(Inject.class) != null;
          
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
              System.out.println(code);
              constructor.insertBeforeBody(code);
            } catch (CannotCompileException e) {
              throw new RuntimeException(e);
            } catch (NotFoundException e) {
              throw new RuntimeException(e);
            }
          }
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
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
}
