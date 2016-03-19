package ch.kerbtier.esdi.javassist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javassist.ClassPool;
import javassist.NotFoundException;

public class InjectEsdi {
  private ClassPool classPool = new ClassPool(true);
  
  public void addClasspath(File what) {
    try {
      classPool.appendClassPath(what.toString());
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public void inject(File file) {
    System.out.println("inject into " + file);
    
    try {
      ClassManipulator mani = new ClassManipulator(classPool.makeClass(new FileInputStream(file)));
      mani.manipulate();
      FileOutputStream fop = new FileOutputStream(file);
      mani.write(fop);
      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
