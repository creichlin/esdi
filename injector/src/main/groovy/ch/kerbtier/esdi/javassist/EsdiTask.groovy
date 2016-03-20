package ch.kerbtier.esdi.javassist

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import ch.kerbtier.esdi.javassist.ClassManipulator.ClassManipulatorLogger;

class EsdiTask extends DefaultTask {
  
  private List<File> classPath = new ArrayList<>()
  private List<File> classFolders = new ArrayList<>()

  @TaskAction
  public void action() {
    def injector = new InjectEsdi();
    
    injector.logger = [
      log: { name, field, type ->
        println("${name}.${field} = $type")
      },
      warn: { desc ->
        println("${desc}")
      }
    ] as ClassManipulatorLogger

    classPath.each {
      injector.addClasspath(it)
    }

    classFolders.each { classFolder -> 
      project.fileTree(classFolder).each { file ->
        injector.inject(file)
      }
    }
  }

  public List<File> getClassPath() {
    return classPath;
  }

    public List<File> getClassFolders() {
    return classFolders;
  }
}
