# Changelog

## 10.0

* Moved out tester classes to the dedicated test-fixtures artifact
* Remove `org.sonar.api.SonarQubeVersion`.
* Deprecate `org.sonar.api.Plugin.Context.getSonarQubeVersion()`. The name is confusing: the API version is different from the SonarQube version since the extraction. Use `org.sonar.api.SonarRuntime.getApiVersion()` instead.

## 9.17

* Introduce `org.sonar.api.batch.sensor.issue.NewIssue.setCodeVariants(Iterable<String>)` to contribute issues for given variants (e.g. different target platforms)
* Improvements on `org.sonar.api.testfixtures.log.LogTester`:
  * intercepts logged exceptions
  * thread-safe

## 9.16

* Extension point `org.sonar.api.resources.Language` now supports `filenamePatterns` to detect files' language based on more complex filename patterns than only filename extensions.
* Usage of `javax-servlet-api` is now deprecated in favor of custom, framework-agnostic API:
  * Replace ~~`org.sonar.api.web.ServletFilter`~~ by `org.sonar.api.server.web.HttpFilter`
  * Replace ~~`javax.servlet.http.HttpServletRequest`~~ by `org.sonar.api.server.http.HttpRequest`
  * Replace ~~`javax.servlet.http.HttpServletResponse`~~ by `org.sonar.api.server.http.HttpResponse`
  * Other added classes: `org.sonar.api.web.FilterChain`, `org.sonar.api.web.UrlPattern` and `org.sonar.api.server.http.Cookie`

## 9.15

* `org.sonar.api.utils.log.Loggers` and `org.sonar.api.utils.log.Logger` are now deprecated in favor of the direct use of [SLF4J](https://www.slf4j.org/):
  * Replace ~~`org.sonar.api.utils.log.Loggers`~~ by `org.slf4j.LoggerFactory`
  * Replace ~~`org.sonar.api.utils.log.Logger`~~ by `org.slf4j.Logger`
* Utility classes used to test logs have been moved to a separate artifact `org.sonarsource.api.plugin:sonar-plugin-api-test-fixtures` and moved to a new package:
  *  `org.sonar.api.utils.log.LogTester` &rarr; `org.sonar.api.testfixtures.log.LogTester`
  *  `org.sonar.api.utils.log.LogTesterJUnit5` &rarr; `org.sonar.api.testfixtures.log.LogTesterJUnit5`
* **Breaking change for tests**: the default log level when using `LogTester` is now `INFO`. This is consistent with the default behavior of Sonar products. If you want to assert `DEBUG` or `TRACE` logs in your tests, you should first change the log level by using for example `logTester.setLevel(Level.DEBUG)`.
