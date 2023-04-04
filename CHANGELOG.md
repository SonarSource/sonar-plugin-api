# Changelog

## 9.15

* `org.sonar.api.utils.log.Loggers` and `org.sonar.api.utils.log.Logger` are now deprecated in favor of the direct use of [SLF4J](https://www.slf4j.org/):
  * Replace ~~`org.sonar.api.utils.log.Loggers`~~ by `org.slf4j.LoggerFactory`
  * Replace ~~`org.sonar.api.utils.log.Logger`~~ by `org.slf4j.Logger`
* Utility classes used to test logs have been moved to a separate artifact `org.sonarsource.api.plugin:sonar-plugin-api-test-fixtures` and moved to a new package:
  *  `org.sonar.api.utils.log.LogTester` &rarr; `org.sonar.api.testfixtures.log.LogTester`
  *  `org.sonar.api.utils.log.LogTesterJUnit5` &rarr; `org.sonar.api.testfixtures.log.LogTesterJUnit5`
* **Breaking change for tests**: the default log level when using `LogTester` is now `INFO`. This is consistent with the default behavior of Sonar products. If you want to assert `DEBUG` or `TRACE` logs in your tests, you should first change the log level by using for example `logTester.setLevel(Level.DEBUG)`.