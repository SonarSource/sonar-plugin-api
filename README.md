# sonar-plugin-api
Java API to develop plugins for SonarQube, SonarCloud and SonarLint.
This component was extracted out of SonarQube, and is released independently since v9.5.

## Developing plugins
See documentation [here](https://docs.sonarqube.org/latest/extend/developing-plugin/) about how to use the `sonar-plugin-api` to develop plugins.

## Dependency
The API was relocated when it was extracted out of SonarQube. Its new coordinates are:
```
org.sonarsource.api.plugin:sonar-plugin-api:<version>
```
You can find it in [maven central](https://mvnrepository.com/artifact/org.sonarsource.api.plugin/sonar-plugin-api).
Also note that the version no longer follows SonarQube's versions. The `sonar-plugin-api` is now released separately.

