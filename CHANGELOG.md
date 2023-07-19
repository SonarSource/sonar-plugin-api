# Changelog

## 10.1

* Remove @Beta Code Characteristics `org.sonar.api.code.CodeCharacteristic`

## 10.0

* Moved out tester classes to the dedicated test-fixtures artifact
* Remove `org.sonar.api.SonarQubeVersion`.
* Deprecate `org.sonar.api.Plugin.Context.getSonarQubeVersion()`. The name is confusing: the API version is different from the SonarQube version since the extraction. Use `org.sonar.api.SonarRuntime.getApiVersion()` instead.
* Remove `org.sonar.api.SonarPlugin`. Use `org.sonar.api.Plugin` instead
* Remove `org.sonar.api.SonarQubeVersion`. No replacement
* Remove `org.sonar.api.security.LoginPasswordAuthenticator` and `org.sonar.api.CoreProperties.CORE_AUTHENTICATOR_CLASS`.
  Use `org.sonar.api.security.Authenticator` instead. Make `org.sonar.api.security.SecurityRealm.doGetAuthenticator()` abstract
* Remove `org.sonar.api.web.UserRole.VIEWER`. Use `org.sonar.api.web.UserRole.USER` instead
* Remove `org.sonar.api.measures.Metric` constructors. Use `org.sonar.api.measures.Metric.Builder.create` instead
* Remove `org.sonar.api.rules.Rule` constructors. Use the `org.sonar.api.rules.Rule.create()` methods instead
* Remove `org.sonar.api.rules.RuleParam` constructors. Use the `org.sonar.api.rules.Rule.createParameter()` methods instead
* Remove `org.sonar.api.rules.AnnotationRuleParser`. Use `org.sonar.api.server.rule.RulesDefinitionAnnotationLoader` instead
* Remove `org.sonar.api.PropertyType.METRIC`, `org.sonar.api.PropertyType.METRIC_LEVEL`, `org.sonar.api.PropertyType.LICENSE`
  and `org.sonar.api.PropertyType.LONG`. There is no replacement as those settings are not used anymore
* Remove `org.sonar.api.profiles.XMLProfileSerializer` and `org.sonar.api.profiles.XMLProfileParser`. No replacement
* Remove `org.sonar.api.utils.ZipUtils.ZipEntryFilter`. Use `java.util.function.Predicate<ZipEntry>` instead
* Remove `org.sonar.api.Property.propertySetKey()`. No replacement
* Remove `org.sonar.api.platform.Server.isSecured()`. Use `org.sonar.api.server.http.HttpRequest.isSecure()` instead
* Remove `org.sonar.api.resources.ResourceType.Builder.availableForFilters()`.
  Use `org.sonar.api.resources.ResourceType.Builder.setProperty(java.lang.String, java.lang.String)` instead
* Remove `org.sonar.api.ce.posttask.QualityGate.Condition.getWarningThreshold()`
  and `org.sonar.api.ce.posttask.QualityGate.Condition.isOnLeakPeriod()`. No replacement
* Remove `org.sonar.api.ce.posttask.QualityGate.Operator.EQUALS` and `org.sonar.api.ce.posttask.QualityGate.Operator.NOT_EQUALS`. No
  replacement
* Remove `org.sonar.api.ce.posttask.QualityGate.EvaluationStatus.WARN`. No replacement
* Remove `org.sonar.api.ce.posttask.QualityGate.Status.WARN`. No replacement
* Remove `org.sonar.api.measures.Metric.Level.WARN`. No replacement
* Remove `org.sonar.api.measures.CoreMetrics.DOMAIN_DUPLICATION`. Use `org.sonar.api.measures.CoreMetrics.DOMAIN_DUPLICATIONS` instead
* Remove `org.sonar.api.measures.CoreMetrics.DIRECTORIES_KEY` and `org.sonar.api.measures.CoreMetrics.DIRECTORIES`. No replacement
* Remove `org.sonar.api.utils.HttpDownloader.TIMEOUT_MILLISECONDS`.
  Use `org.sonar.api.utils.HttpDownloader.DEFAULT_READ_TIMEOUT_IN_MILLISECONDS`
  or `org.sonar.api.utils.HttpDownloader.DEFAULT_CONNECT_TIMEOUT_IN_MILLISECONDS` instead
* Remove `org.sonar.api.resources.Qualifiers.LIBRARY`. No replacement
* Remove `org.sonar.api.resources.Scopes.PROGRAM_UNIT` and `org.sonar.api.resources.Scopes.BLOCK_UNIT`. No replacement
* Remove `org.sonar.api.CoreProperties.CORE_AUTHENTICATOR_CREATE_USERS`, `org.sonar.api.CoreProperties.CORE_AUTHENTICATOR_UPDATE_USER_ATTRIBUTES`, `org.sonar.api.CoreProperties.CORE_AUTHENTICATOR_REALM`,
`org.sonar.api.CoreProperties.LINKS_SOURCES_DEV` and `org.sonar.api.CoreProperties.CORE_AUTHENTICATOR_IGNORE_STARTUP_FAILURE`. No replacement
* Remove `org.sonar.api.issue.IssueComment` and `org.sonar.api.issue.Issue.comments()`. No replacement
* Remove `org.sonar.api.batch.rule.Rules`. No replacement
* Remove `org.sonar.api.notifications.NotificationChannel` and `org.sonar.api.notifications.Notification`. No replacement
* Remove `org.sonar.api.scan.filesystem.PathResolver.RelativePath` and `org.sonar.api.scan.filesystem.PathResolver.relativePath(java.util.Collection<java.io.File>, java.io.File)`. No replacement
* Remove `org.sonar.api.platform.Server.getPermanentServerId()`. Use `org.sonar.api.platform.Server.getId()` instead
* Remove `org.sonar.api.rules.RulePriority.valueOfString(String)`. Use `org.sonar.api.rules.RulePriority.valueOf(String)` instead
* Deprecate `org.sonar.api.Plugin.Context.getSonarQubeVersion()`. The name is confusing: the API version is different from the SonarQube
  version since the extraction. Use `org.sonar.api.SonarRuntime.getApiVersion()` instead.

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
