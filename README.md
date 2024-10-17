# sonar-plugin-api

Java API to develop plugins for SonarQube, SonarCloud and SonarLint.
This component was extracted out of SonarQube, and is released independently since v9.5.

The API is built with JDK 11.

## Developing plugins

See documentation [here](https://docs.sonarqube.org/latest/extend/developing-plugin/) about how to use the `sonar-plugin-api` to develop plugins.

## Dependency

The API was relocated when it was extracted out of SonarQube. Its new coordinates are:

```
org.sonarsource.api.plugin:sonar-plugin-api:<version>
```

You can find it in [maven central](https://mvnrepository.com/artifact/org.sonarsource.api.plugin/sonar-plugin-api).
Also note that the version no longer follows SonarQube's versions. The `sonar-plugin-api` is now released separately.

## Changelogs

See the [releases](https://github.com/SonarSource/sonar-plugin-api/releases) for changelogs.


## Deprecation Policy

See the [deprecation policy](docs/deprecation-policy.md).

## Compatibility

Breaking changes to the API happen when the major version changes. As a result, plugins should be compatible with future versions of the API within the same major version.
New APIs can be introduced in any release of the API, and plugins may want to do a runtime version check to see if they can use newly added APIs, depending on what platforms they intend to run.

The following tables indicate which API versions plugins can find at runtime: 

### SonarQube

| SonarQube              | Plugin API                |
|------------------------|---------------------------|
| 10.7.0.96327           | 10.11.0.2468              |
| 10.6.0.92116           | 10.7.0.2191               |
| 10.5.1.90531           | 10.7.0.2191               |
| 10.4.0.87286           | 10.6.0.2114               |
| 10.3.0.82913           | 10.2.0.1908               |
| 10.2.0.77647           | 10.1.0.809                |
| 10.1.0.73491           | 9.17.0.587                |
| 10.0.0.68432           | 9.14.0.375                |
| 9.9.0.65466            | 9.14.0.375                |
| 9.8.0.63668            | 9.13.0.360                |
| 9.7.1.62043            | 9.11.0.290                |
| 9.7.0.61563            | 9.11.0.290                |
| 9.6.1.59531            | 9.9.0.229                 |
| 9.6.0.59041            | 9.9.0.229                 |
| 9.5.0.56709            | 9.6.1.114                 |
| 9.4.0.54424 or earlier | Matches sonarqube version |

### SonarCloud
Current version: 10.6.0.2114

### SonarLint
| Flavor   | Plugin API   |
|----------|--------------|
| IntelliJ | 10.11.0.2468 |
| Eclipse  | 10.11.0.2468 |
| VSCode   | 10.11.0.2468 |

## Optimizing the execution of sensors

See [here](docs/optimize-sensors.md) how to leverage APIs introduced recently to help optimize sensors in order to achieve faster analysis.

## Have Question or Feedback?

For support questions ("How do I?", "I got this error, why?", ...), please head to the [SonarSource forum](https://community.sonarsource.com/c/help). There are chances that a question similar to yours has already been answered.

Be aware that this forum is a community, so the standard pleasantries ("Hi", "Thanks", ...) are expected. And if you don't get an answer to your thread, you should sit on your hands for at least three days before bumping it. Operators are not standing by. :-)

## License

Copyright 2008-2024 SonarSource.

Licensed under the [GNU Lesser General Public License, Version 3.0](https://www.gnu.org/licenses/lgpl.txt)
