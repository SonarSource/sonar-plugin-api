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

## Optimizing the execution of sensors

See [here](docs/optimize-sensors.md) how to leverage APIs introduced recently to help optimize sensors in order to achieve faster analysis.

## Have Question or Feedback?

For support questions ("How do I?", "I got this error, why?", ...), please head to the [SonarSource forum](https://community.sonarsource.com/c/help). There are chances that a question similar to yours has already been answered.

Be aware that this forum is a community, so the standard pleasantries ("Hi", "Thanks", ...) are expected. And if you don't get an answer to your thread, you should sit on your hands for at least three days before bumping it. Operators are not standing by. :-)

## License

Copyright 2008-2023 SonarSource.

Licensed under the [GNU Lesser General Public License, Version 3.0](https://www.gnu.org/licenses/lgpl.txt)
