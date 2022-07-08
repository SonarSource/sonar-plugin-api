# Adapting to the execution context

## Execution contexts

Plugins can be loaded and executed in a lot of different contexts in the Sonar ecosystem:

* Scanner, which runs the source code analysis (usually as part of a build pipeline)
* Compute Engine, ran on the SonarQube host but in a separate process. Its goal is to consolidate the output of scanners by: 
   * computing 2nd-level measures such as ratings
   * aggregating measures (for example number of lines of code of project = sum of lines of code of all files)
   * assigning new issues to developers who introduced them
   * persisting everything in data stores
* Web application
* SonarLint when connected to SonarQube (only to run custom rules of supported languages)

Extension points are not designed to add new features but to complete existing features. Technically they are contracts defined by a Java interface or an abstract class annotated with @ExtensionPoint. The exhaustive list of extension points is available in the javadoc.

### Lifecycle
A plugin extension exists only in its associated technical stacks. A scanner sensor is for example instantiated and executed only in a scanner runtime, but not in the web server nor in Compute Engine. The stack is defined by the annotations [@ScannerSide](http://javadocs.sonarsource.org/latest/apidocs/org/sonar/api/batch/ScannerSide.html), [@ServerSide](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/server/ServerSide.html) (for web server) and [@ComputeEngineSide](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/ce/ComputeEngineSide.html). 

An extension can call core components or another extension of the same stack. These dependencies are defined by constructor injection:

```
@ScannerSide
public class Foo {
  public void call() {}
}
 
// Sensor is a scanner extension point 
public class MySensor implements Sensor {
  private final Foo foo;
  private final Languages languages;
  
  // Languages is core component which lists all the supported programming languages.
  public MySensor(Foo foo, Languages languages) {   
    this.foo = foo;
    this.languages = languages;
  }
  
  @Override
  public void execute(SensorContext context) {
    System.out.println(this.languages.all());
    foo.call();
  }
}
 
  
public class ExamplePlugin implements Plugin {
  @Override
  public void define(Context context) {
    // Languages is a core component. It must not be declared by plugins.
    context.addExtensions(Foo.class, MySensor.class);
  }
}
```

It is recommended not to call other components in constructors. Indeed, they may not be initialized at that time. Constructors should only be used for dependency injection.

[[warning]]
| Compilation does not fail if incorrect dependencies are defined, such as a scanner extension trying to call a web server extension. Still it will fail at runtime when plugin is loaded.