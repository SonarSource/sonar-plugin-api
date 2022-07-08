# Declaring library dependencies

Plugins are executed in their own isolated classloaders. That allows the packaging and use of 3rd-party libraries without runtime conflicts with core internal libraries or other plugins. Note that since version 5.2, the SonarQube API does not bring transitive dependencies, except SLF4J. The libraries just have to be declared in the pom.xml with default scope "compile":

pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project>
  ...
  <dependencies>
    ...
    <dependency>
      <groupId>commons-codec</groupId>
      <artifactId>commons-codec</artifactId>
      <version>1.10</version>
    </dependency>
 </dependencies>
</project>
```
Technically the libraries are packaged in the directory META-INF/lib of the generated JAR file. An alternative is to shade libraries, for example with maven-shade-plugin. That minimizes the size of the plugin .jar file by copying only the effective used classes.

[[info]]
| The command `mvn dependency:tree` gives the list of all dependencies, including transitive ones.