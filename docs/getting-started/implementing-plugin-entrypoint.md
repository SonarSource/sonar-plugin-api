# Implementing the Plugin entrypoint

## Creating the plugin class

All Sonar plugins need to define a single entrypoint for products to be able to load them.
This entrypoint is the "plugin class", a Java class that implements the `org.sonar.api.Plugin` interface,
and its main goal is to declare what it contributes to the Sonar ecosystem.

Here is what an empty plugin class would look like:

ExamplePlugin.java
```
package org.sonarqube.plugins.example;

import org.sonar.api.Plugin;
 
public class ExamplePlugin implements Plugin {
  @Override
  public void define(Plugin.Context context) {
    // declare extensions here
  }
}
```

Some requirements on this plugin class:
* it must be public
* it must have a constructor with no arguments
* it must avoid heavy processing. This class could be loaded at different times and in different contexts, so it must remain simple and light. Its only purpose should be to register extensions.

See [Understanding plugin lifecycle](../developing/extending-solution/understanding-plugin-lifecycle) for more details.

## Declaring the class in the manifest

The last important piece is to reference this plugin class in the plugin's JAR manifest.
The `sonar-packaging-maven-plugin` Maven plugin takes care of that with the mandatory `pluginClass` property (see [packaging](../packaging/index)): 

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

The packager purpose is to create a JAR file that can be loaded by products at runtime.
They can load and instantiate the main Plugin class with reflection using the `pluginClass` property's value. 
