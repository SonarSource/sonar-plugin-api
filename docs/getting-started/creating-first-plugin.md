# Creating a first plugin

## Introduction
The `sonar-plugin-api` is a Java API that is used to develop plugins for the Sonar ecosystem, composed of SonarQube, SonarCloud and SonarLint.
This API used to be part of SonarQube and released with it, but it is a separate component since v9.5, with its own releases. You can find it here:  [sonar-plugin-api](https://github.com/SonarSource/sonar-plugin-api).

## Prerequisites

To build a plugin, you need Java 8 and Maven 3.1 (or greater).

## Create the plugin project

### Maven
This is the recommended way to start a new plugin project. Clone the plugin example project: https://github.com/SonarSource/sonar-custom-plugin-example.

If you want to start the project from scratch, use the following Maven `pom.xml` template:

[[collapse]]
| ## pom.xml
| ```
| <?xml version="1.0" encoding="UTF-8"?>
| <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
|   <modelVersion>4.0.0</modelVersion>
|   <groupId>YOUR_GROUP_ID</groupId>
|   <!-- it's recommended to follow the pattern "sonar-{key}-plugin", for example "sonar-myphp-plugin" -->
|   <artifactId>YOUR_ARTIFACT_ID</artifactId>
|   <version>YOUR_VERSION</version>
|   
|   <!-- this is important for sonar-packaging-maven-plugin -->
|   <packaging>sonar-plugin</packaging>
|  
|   <dependencies>
|     <dependency>
|       <!-- groupId has changed to 'org.sonarsource.api.plugin' starting on version 9.5 -->
|       <groupId>org.sonarsource.sonarqube</groupId>
|       <artifactId>sonar-plugin-api</artifactId>
|       <!-- minimal version of SonarQube to support. -->
|       <version>8.9</version>
|       <!-- mandatory scope -->
|       <scope>provided</scope>
|     </dependency>
|   </dependencies>
|  
|   <build>
|     <plugins>
|       <plugin>
|         <groupId>org.sonarsource.sonar-packaging-maven-plugin</groupId>
|         <artifactId>sonar-packaging-maven-plugin</artifactId>
|         <version>1.18.0.372</version>
|         <extensions>true</extensions>
|         <configuration>
|           <!-- the entry-point class that extends org.sonar.api.SonarPlugin -->
|           <pluginClass>com.mycompany.sonar.reference.ExamplePlugin</pluginClass>
|            
|           <!-- advanced properties can be set here. See paragraph "Advanced Build Properties". -->
|         </configuration>
|       </plugin>
|     </plugins>
|   </build>
| </project>
| ```

#### Build
To build your plugin project, execute this command from the project root directory:  
`mvn clean package`  
The plugin jar file is generated in the project's `target/` directory.

### Gradle

Gradle can also be used thanks to https://github.com/iwarapter/gradle-sonar-packaging-plugin. Note that this Gradle plugin is not officially supported by SonarSource, and it won't be covered in this guide.

## Deploy in a SonarQube instance
**"Cold" Deploy**  
The standard way to install the plugin for regular users is to copy the JAR artifact, from the `target/` directory  to the `extensions/plugins/` directory of your SonarQube installation then start the server. The file `logs/web.log` will then contain a log line similar to:  
`Deploy plugin Example Plugin / 0.1-SNAPSHOT`  
Scanner extensions such as sensors are immediately retrieved and loaded when scanning source code. 

## Debug
**Debugging web server extensions**  

1. Edit conf/sonar.properties and set: `sonar.web.javaAdditionalOpts=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000`
1. Install your plugin by copying its JAR file to extensions/plugins
1. Start the server. The line `Listening for transport dt_socket at address: 5005` is logged in  `logs/sonar.log`.
1. Attach your IDE to the debug process (listening on port 8000 in the example)

**Debugging compute engine extensions**  
Same procedure as for web server extensions (see previous paragraph), but with the property: `sonar.ce.javaAdditionalOpts=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000`

**Debugging scanner extensions**  
```
export SONAR_SCANNER_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000"
cd /path/to/project
sonar-scanner 
```
When using the Scanner for Maven, then simply execute:
```
cd /path/to/project
mvnDebug sonar:sonar
# debug port is 8000
```

## Advanced Build Properties
Plugin properties are defined in the file `META-INF/MANIFEST.MF` of the plugin .jar file.

Most of them are defined through the `<configuration>` section of the [sonar-packaging-maven-plugin](https://jira.sonarsource.com/browse/PACKMP). Some are taken from standard pom nodes Effective values are listed at the end of the build log:
```
[INFO] --- sonar-packaging-maven-plugin:1.15:sonar-plugin (default-sonar-plugin) @ sonar-widget-lab-plugin ---
[INFO] -------------------------------------------------------
[INFO] Plugin definition in Marketplace
[INFO]     Key: widgetlab
[INFO]     Name: Widget Lab
[INFO]     Description: Additional widgets
[INFO]     Version: 1.9-SNAPSHOT
[INFO]     Entry-point Class: org.codehaus.sonar.plugins.widgetlab.WidgetLabPlugin
[INFO]     Required Plugins:
[INFO]     Use Child-first ClassLoader: false
[INFO]     Base Plugin:
[INFO]     Homepage URL: https://redirect.sonarsource.com/plugins/widgetlab.html
[INFO]     Minimal SonarQube Version: 4.5.1
[INFO]     Licensing: GNU LGPL 3
[INFO]     Organization: Shaw Industries
[INFO]     Organization URL: http://shawfloors.com
[INFO]     Terms and Conditions:
[INFO]     Issue Tracker URL: http://jira.codehaus.org/browse/SONARWIDLB
[INFO]     Build date: 2015-12-15T18:28:54+0100
[INFO]     Sources URL: https://github.com/SonarCommunity/sonar-widget-lab
[INFO]     Developers: G. Ann Campbell,Patroklos Papapetrou
[INFO] -------------------------------------------------------
[INFO] Building jar: /dev/sonar-widget-lab/target/sonar-widget-lab-plugin-1.9-SNAPSHOT.jar 
```

Supported standard pom node properties:

Maven Property|Manifest Key|Notes
---|---|---
`version` | Plugin-Version | (required) Plugin version as displayed in page "Marketplace". Default: ${project.version}
- | Sonar-Version | (required) Minimal version of supported SonarQube at runtime. For example if value is 5.2, then deploying the plugin on versions 5.1 and lower will fail. Default value is given by the version of sonar-plugin-api dependency. It can be overridden with the Maven property sonarQubeMinVersion (since sonar-packaging-maven-plugin 1.16). That allows in some cases to use new features of recent API and to still be compatible at runtime with older versions of SonarQube. Default: version of dependency sonar-plugin-api
`license` | Plugin-License | Plugin license as displayed in page "Marketplace". Default `${project.licenses}`
`developers` | Plugin-Developers | List of developers displayed in page "Marketplace". Default: `${project.developers}`

Supported `<configuration>` properties:

Maven Property|Manifest Key|Notes
---|---|---
`pluginKey` | Plugin-Key | (required) Contains only letters/digits and is unique among all plugins. Examples: groovy, widgetlab. Constructed from `${project.artifactId}.` Given an artifactId of: `sonar-widget-lab-plugin`, your pluginKey will be: `widgetlab`
`pluginClass` | Plugin-Class | (required) Name of the entry-point class that extends `org.sonar.api.SonarPlugin`. Example: `org.codehaus.sonar.plugins.widgetlab.WidgetLabPlugin` 
`pluginName` | Plugin-Name | (required) Displayed in the page "Marketplace". Default: `${project.name}`
`pluginDescription` | Plugin-Description | Displayed in the page "Marketplace". Default: `${project.description}`
`pluginUrl` |  Plugin-Homepage | Homepage of website, for example https://github.com/SonarQubeCommunity/sonar-widget-lab `${project.url}`
`pluginIssueTrackerUrl` |  Plugin-IssueTrackerUrl | Example: https://github.com/SonarQubeCommunity/sonar-widget-lab/issues. Default: `${project.issueManagement.url}`
`pluginTermsConditionsUrl`  |  Plugin-TermsConditionsUrl | Users must read this document when installing the plugin from Marketplace. Default: `${sonar.pluginTermsConditionsUrl}`
`useChildFirstClassLoader` | Plugin-ChildFirstClassLoader | Each plugin is executed in an isolated classloader, which inherits a shared classloader that contains API and some other classes. By default the loading strategy of classes is parent-first (look up in shared classloader then in plugin classloader). If the property is true, then the strategy is child-first. This property is mainly used when building plugin against API < 5.2, as the shared classloader contained many 3rd party libraries (guava 10, commons-lang, ...) false
`basePlugin` | Plugin-Base | If specified, then the plugin is executed in the same classloader as basePlugin.
`pluginSourcesUrl` | Plugin-SourcesUrl | URL of SCM repository for open-source plugins. Displayed in page "Marketplace". Default: `${project.scm.url}`
`pluginOrganizationName` | Plugin-Organization | Organization which develops the plugin, displayed in the page "Marketplace". Default: `${project.organization.name}`
`pluginOrganizationUrl` | Plugin-OrganizationUrl | URL of the organization, displayed in the page "Marketplace". Default: `${project.organization.url}`
`sonarLintSupported` | SonarLint-Supported | Whether the language plugin supports SonarLint or not. Only SonarSource analyzers and custom rules plugins for SonarSource analyzers should set this to true. 
`pluginDisplayVersion` | Plugin-Display-Version | The version as displayed in SonarQube administration console. By default it's the raw version, for example "1.2", but can be overridden to "1.2 (build 12345)" for instance. Supported in sonar-packaging-maven-plugin 1.18.0.372. Default: `${project.version}`


The Maven sonar-packaging-maven-plugin supports also these properties:

Maven Property|Manifest Key|Notes
---|---|---
`addMavenDescriptor` |Copy pom file inside the directory META-INF of generated JAR file? | Boolean. Default: `${sonar.addMavenDescriptor}` / `true`.
`skipDependenciesPackaging` | Do not copy Maven dependencies into JAR file. | Default: `${sonar.skipDependenciesPackaging} / `false`.

Other Manifest fields:  

* `Implementation-Build` - Identifier of build or commit, for example the Git sha1 "94638028f0099de59f769cdca776e506684235d6". It is displayed for debugging purpose in logs when SonarQube server starts.

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


## Versioning and API Deprecation
### Versioning Strategy
The goal of this versioning strategy is both to:

* Release often, release early in order to get quick feedback from the SonarQube community
* Release stable versions of the SonarQube platform for companies whose main priority is to set up a very stable environment. Even if the price for such stable environments is missing out on the latest, sexy SonarQube features
* Support the API deprecation strategy (see next section)

The rules are:

* Every ~two months a new version of SonarQube is released. This version should increment the minor digit of the previous version (ex: 8.2 -> 8.3)
* Every ~eighteen months, a bug-fix version is released, and becomes the new LTS. The major digit of the subsequent version is incremented to start a new cycle (ex: 7.9 -> 8.0)

And here is the strategy in action:
```
7.8 -> 7.9 -> 8.0 -> 8.1 -> 8.2 -> ... -> 8.9 -> 9.0 -> ...     <- New release every ~2 months
        |                                  |
      7.9.1 -> 7.9.2 -> ...              8.9.1 -> 8.9.2 -> ...  <- New LTS
```

### API Deprecation Strategy
The goal of this deprecation strategy is to make sure that deprecated APIs will be dropped without side-effects at a given planned date. The expected consequence of such strategy is to ease the evolution of the SonarQube API by making such refactoring painless.

The rules are:

* An API must be deprecated before being dropped. Furthermore, if the underlying feature is not being dropped, a replacement API must immediately be provided.
* A deprecated API must be fully supported until its drop (For instance the implementation of a deprecated method can't be replaced by `throw new UnsupportedOperationException()`)
* If an API is deprecated in version X.Y, this API is planned to be dropped in version (X+1).0. Example: an API deprecated in 9.1 is supported in 9.2, 9.3, and so forth for the entire 9.N cycle; it will be dropped in version 10.0
* According to this versioning strategy, an API can remain deprecated for up to 18 months, and for as short as 2 months
* Any release of a SonarQube plugin must at least depend on the latest LTS version of the SonarQube API
* For each SonarQube plugin there must at least one release on each LTS version of SonarQube, which means at least one release each ~18 months.
* An API is marked as deprecated with both:
   * the annotation @Deprecated
   * the javadoc tag @deprecated whose message must start with "in x.y", for example:
   
    ```
    /**
     * @deprecated in 4.2. Replaced by {@link #newMethod()}.
     */
    @Deprecated
    public void foo() {
    ```
