# Creating a first plugin

## Introduction
The `sonar-plugin-api` is a Java API that is used to develop plugins for the Sonar ecosystem, composed of different products.
This API used to be part of SonarQube and released with it, but it is a separate component since v9.5, with its own releases. 
You can find it here:  [sonar-plugin-api](https://github.com/SonarSource/sonar-plugin-api).

## Prerequisites

To build a plugin, you need Java 8 and Maven 3.1 (or greater).

## Create the plugin project

The easiest way to bootstrap a new plugin is to clone the plugin example project: https://github.com/SonarSource/sonar-custom-plugin-example.

## API basics

### Extension points
SonarQube provides extension points for its three technical stacks:

* Scanner, which runs the source code analysis
* Compute Engine, which consolidates the output of scanners, for example by 
   * computing 2nd-level measures such as ratings
   * aggregating measures (for example number of lines of code of project = sum of lines of code of all files)
   * assigning new issues to developers
   * persisting everything in data stores
* Web application

Extension points are not designed to add new features but to complete existing features. Technically they are contracts defined by a Java interface or an abstract class annotated with @ExtensionPoint. The exhaustive list of extension points is available in the javadoc.

The implementations of extension points (named "extensions") provided by a plugin must be declared in its entry point class, which implements org.sonar.api.Plugin and which is referenced in pom.xml:

ExamplePlugin.java
```
package org.sonarqube.plugins.example;
import org.sonar.api.Plugin;
 
public class ExamplePlugin implements Plugin {
  @Override
  public void define(Context context) {
    // implementations of extension points
    context.addExtensions(FooLanguage.class, ExampleProperties.class);
  }
}
```
pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project>
  ...
  <build>
    <plugins>
      <plugin>
        <groupId>org.sonarsource.sonar-packaging-maven-plugin</groupId>
        <artifactId>sonar-packaging-maven-plugin</artifactId>
        <extensions>true</extensions>
        <configuration>
          <pluginClass>org.sonarqube.plugins.example.ExamplePlugin</pluginClass>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```
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
