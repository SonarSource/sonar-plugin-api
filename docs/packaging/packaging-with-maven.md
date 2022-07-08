# Packaging with Maven

## Prerequisites

To build a plugin, you need Java 8 and Maven 3.1 (or greater).

## Setting the project up

To start the project from scratch, use the following Maven `pom.xml` template:

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

## Packaging the project 

To build your plugin project, execute this command from the project root directory:

`mvn clean package`

The plugin jar file is generated in the project's `target/` directory.

## Supported Build Properties

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

| Maven Property | Manifest Key      | Notes                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|----------------|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `version`      | Plugin-Version    | (required) Plugin version as displayed in page "Marketplace". Default: ${project.version}                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| -              | Sonar-Version     | (required) Minimal version of supported SonarQube at runtime. For example if value is 5.2, then deploying the plugin on versions 5.1 and lower will fail. Default value is given by the version of sonar-plugin-api dependency. It can be overridden with the Maven property sonarQubeMinVersion (since sonar-packaging-maven-plugin 1.16). That allows in some cases to use new features of recent API and to still be compatible at runtime with older versions of SonarQube. Default: version of dependency sonar-plugin-api |
| `license`      | Plugin-License    | Plugin license as displayed in page "Marketplace". Default `${project.licenses}`                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| `developers`   | Plugin-Developers | List of developers displayed in page "Marketplace". Default: `${project.developers}`                                                                                                                                                                                                                                                                                                                                                                                                                                            |

Supported `<configuration>` properties:

| Maven Property             | Manifest Key                 | Notes                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
|----------------------------|------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `pluginKey`                | Plugin-Key                   | (required) Contains only letters/digits and is unique among all plugins. Examples: groovy, widgetlab. Constructed from `${project.artifactId}.` Given an artifactId of: `sonar-widget-lab-plugin`, your pluginKey will be: `widgetlab`                                                                                                                                                                                                                                                    |
| `pluginClass`              | Plugin-Class                 | (required) Name of the entry-point class that extends `org.sonar.api.SonarPlugin`. Example: `org.codehaus.sonar.plugins.widgetlab.WidgetLabPlugin`                                                                                                                                                                                                                                                                                                                                        |
| `pluginName`               | Plugin-Name                  | (required) Displayed in the page "Marketplace". Default: `${project.name}`                                                                                                                                                                                                                                                                                                                                                                                                                |
| `pluginDescription`        | Plugin-Description           | Displayed in the page "Marketplace". Default: `${project.description}`                                                                                                                                                                                                                                                                                                                                                                                                                    |
| `pluginUrl`                | Plugin-Homepage              | Homepage of website, for example https://github.com/SonarQubeCommunity/sonar-widget-lab `${project.url}`                                                                                                                                                                                                                                                                                                                                                                                  |
| `pluginIssueTrackerUrl`    | Plugin-IssueTrackerUrl       | Example: https://github.com/SonarQubeCommunity/sonar-widget-lab/issues. Default: `${project.issueManagement.url}`                                                                                                                                                                                                                                                                                                                                                                         |
| `pluginTermsConditionsUrl` | Plugin-TermsConditionsUrl    | Users must read this document when installing the plugin from Marketplace. Default: `${sonar.pluginTermsConditionsUrl}`                                                                                                                                                                                                                                                                                                                                                                   |
| `useChildFirstClassLoader` | Plugin-ChildFirstClassLoader | Each plugin is executed in an isolated classloader, which inherits a shared classloader that contains API and some other classes. By default the loading strategy of classes is parent-first (look up in shared classloader then in plugin classloader). If the property is true, then the strategy is child-first. This property is mainly used when building plugin against API < 5.2, as the shared classloader contained many 3rd party libraries (guava 10, commons-lang, ...) false |
| `basePlugin`               | Plugin-Base                  | If specified, then the plugin is executed in the same classloader as basePlugin.                                                                                                                                                                                                                                                                                                                                                                                                          |
| `pluginSourcesUrl`         | Plugin-SourcesUrl            | URL of SCM repository for open-source plugins. Displayed in page "Marketplace". Default: `${project.scm.url}`                                                                                                                                                                                                                                                                                                                                                                             |
| `pluginOrganizationName`   | Plugin-Organization          | Organization which develops the plugin, displayed in the page "Marketplace". Default: `${project.organization.name}`                                                                                                                                                                                                                                                                                                                                                                      |
| `pluginOrganizationUrl`    | Plugin-OrganizationUrl       | URL of the organization, displayed in the page "Marketplace". Default: `${project.organization.url}`                                                                                                                                                                                                                                                                                                                                                                                      |
| `sonarLintSupported`       | SonarLint-Supported          | Whether the language plugin supports SonarLint or not. Only SonarSource analyzers and custom rules plugins for SonarSource analyzers should set this to true.                                                                                                                                                                                                                                                                                                                             |
| `pluginDisplayVersion`     | Plugin-Display-Version       | The version as displayed in SonarQube administration console. By default it's the raw version, for example "1.2", but can be overridden to "1.2 (build 12345)" for instance. Supported in sonar-packaging-maven-plugin 1.18.0.372. Default: `${project.version}`                                                                                                                                                                                                                          |

The Maven sonar-packaging-maven-plugin supports also these properties:

| Maven Property              | Manifest Key                                                       | Notes                                                     |
|-----------------------------|--------------------------------------------------------------------|-----------------------------------------------------------|
| `addMavenDescriptor`        | Copy pom file inside the directory META-INF of generated JAR file? | Boolean. Default: `${sonar.addMavenDescriptor}` / `true`. |
| `skipDependenciesPackaging` | Do not copy Maven dependencies into JAR file.                      | Default: `${sonar.skipDependenciesPackaging} / `false`.   |

Other Manifest fields:

* `Implementation-Build` - Identifier of build or commit, for example the Git sha1 "94638028f0099de59f769cdca776e506684235d6". It is displayed for debugging purpose in logs when SonarQube server starts.

