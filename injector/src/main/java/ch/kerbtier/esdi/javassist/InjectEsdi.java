package ch.kerbtier.esdi.javassist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.kerbtier.esdi.javassist.ClassManipulator.ClassManipulatorLogger;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class InjectEsdi {
  private ClassPool classPool;
  
  private ClassManipulatorLogger logger = new ClassManipulatorLogger() {
    @Override
    public void log(String className, String field, String type) {
      // no logging by default
    }

    @Override
    public void warn(String description) {
      // no logging by default
    }
  };
  
  public InjectEsdi() {
    classPool = new LocalClasspathClassPool();
  }

  public void addClasspath(File what) {
    try {
      classPool.appendClassPath(what.toString());
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void inject(File file) {
    try {
      try {
        CtClass ctClass = classPool.makeClass(new FileInputStream(file));
        ClassManipulator mani = new ClassManipulator(ctClass, logger);
        mani.manipulate();
        FileOutputStream fop = new FileOutputStream(file);
        mani.write(fop);
      } catch (RuntimeException e) {
        String className = e.getMessage().split(":")[0];
        logger.warn("failed to instrument " + className);
        logger.warn(e.getMessage());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setLogger(ClassManipulatorLogger logger) {
    this.logger = logger;
  }
}
