# Implementing the Plugin entrypoint

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
