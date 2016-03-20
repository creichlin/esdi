package ch.kerbtier.esdi.javassist

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class EsdiPlugin implements Plugin<Project>{

  @Override
  public void apply(Project project) {


    project.afterEvaluate {

      project.task("esdiInstrumentTests", type: EsdiTask) {
        dependsOn project.compileTestJava

        project.configurations.testCompile.each { classPath << it }
        
        classPath << project.sourceSets.main.output.classesDir
        classPath << project.sourceSets.test.output.classesDir

        classFolders << project.sourceSets.test.output.classesDir
      }

      project.testClasses.dependsOn project.esdiInstrumentTests



      project.task("esdiInstrument", type: EsdiTask) {
        dependsOn project.compileJava

        project.configurations.compile.each { classPath << it }
        classPath << project.sourceSets.main.output.classesDir

        classFolders << project.sourceSets.main.output.classesDir
      }

      project.classes.dependsOn project.esdiInstrument
      
    }
  }
}
