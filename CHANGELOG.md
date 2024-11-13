# Changelog

## 10.14

* Remove deprecation on  `org.sonar.api.rules.RuleType`, `org.sonar.api.batch.rule.Severity`, `org.sonar.api.rule.Severity` and related usages.
* Remove deprecation on `org.sonar.api.server.rule.internal.ImpactMapper` and `org.sonar.api.server.rule.RuleTagsToTypeConverter`.
* Remove deprecation on metrics `org.sonar.api.measures.CoreMetrics.BLOCKER_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.CRITICAL_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.MAJOR_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.MINOR_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.INFO_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.NEW_BLOCKER_VIOLATIONS`, , `org.sonar.api.measures.CoreMetrics.NEW_CRITICAL_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.NEW_MAJOR_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.NEW_MINOR_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.NEW_INFO_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.CODE_SMELLS`, `org.sonar.api.measures.CoreMetrics.NEW_CODE_SMELLS`, `org.sonar.api.measures.CoreMetrics.BUGS`, `org.sonar.api.measures.CoreMetrics.NEW_BUGS`, `org.sonar.api.measures.CoreMetrics.VULNERABILITIES`, `org.sonar.api.measures.CoreMetrics.NEW_VULNERABILITIES`.
* Deprecate `org.sonar.api.server.rule.internal.ImpactMapper.convertToDeprecatedSeverity`. Use `org.sonar.api.server.rule.internal.ImpactMapper.convertToRuleSeverity` instead.

## 10.13
* Deprecate `org.sonar.api.resources.Qualifiers` and `org.sonar.api.resources.Scopes`
* Add a specialized `ConfigScope` enum on `org.sonar.api.config.PropertyDefinition`, and provide replacements for all `onQualifiers` methods accordingly
* Change the name of `high_impact_accepted_issues` to `Blocker and High Severity Accepted Issues`
* Deprecate metrics `org.sonar.api.measures.CoreMetrics.RELIABILITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.MAINTAINABILITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.SECURITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.NEW_RELIABILITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.NEW_MAINTAINABILITY_ISSUES` and `org.sonar.api.measures.CoreMetrics.NEW_SECURITY_ISSUES`  

## 10.11
* Introduce new impact severities `org.sonar.api.issue.impact.Severity.INFO` and `org.sonar.api.issue.impact.Severity.BLOCKER`

## 10.10
* Introduce `org.sonar.api.server.rule.RulesDefinition.addStig` to support STIG security standards

## 10.8

* Fixed an issue where WebService which was not meant to return any response still showed the warning in the logs when response example was not set.
* Introduce 'org.sonar.api.server.ws.WebService.NewAction.setContentType' for optionally setting a response type of Action.
* Replace internal library `commons-lang:commons-lang` by `org.apache.commons:commons-lang3`.
* Do not throw an exception when a rule parameter is not known in `org.sonar.api.batch.rule.Checks`

## 10.7

* Change domain of metrics `org.sonar.api.measures.CoreMetrics.SECURITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.MAINTAINABILITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.RELIABILITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.NEW_SECURITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.NEW_MAINTAINABILITY_ISSUES` and `org.sonar.api.measures.CoreMetrics.NEW_RELIABILITY_ISSUES` to `SECURITY`, `MAINTAINABILITY` and `RELIABILITY` domains instead of `ISSUES` domain.
* Add new metrics `org.sonar.api.measures.CoreMetrics.NEW_SECURITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.NEW_MAINTAINABILITY_ISSUES` and `org.sonar.api.measures.CoreMetrics.NEW_RELIABILITY_ISSUES`.
* Deprecate metrics `org.sonar.api.measures.CoreMetrics.BLOCKER_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.CRITICAL_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.MAJOR_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.MINOR_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.INFO_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.NEW_BLOCKER_VIOLATIONS`, , `org.sonar.api.measures.CoreMetrics.NEW_CRITICAL_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.NEW_MAJOR_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.NEW_MINOR_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.NEW_INFO_VIOLATIONS`, `org.sonar.api.measures.CoreMetrics.CODE_SMELLS`, `org.sonar.api.measures.CoreMetrics.NEW_CODE_SMELLS`, `org.sonar.api.measures.CoreMetrics.BUGS`, `org.sonar.api.measures.CoreMetrics.NEW_BUGS`, `org.sonar.api.measures.CoreMetrics.VULNERABILITIES`, `org.sonar.api.measures.CoreMetrics.NEW_VULNERABILITIES`.  Use `org.sonar.api.measures.CoreMetrics.SECURITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.MAINTAINABILITY_ISSUES` and `org.sonar.api.measures.CoreMetrics.RELIABILITY_ISSUES` instead.

## 10.6

* Deprecate metric `org.sonar.api.measures.CoreMetrics.HIGH_IMPACT_ACCEPTED_ISSUES`.
* Add new metrics `org.sonar.api.measures.CoreMetrics.SECURITY_ISSUES`, `org.sonar.api.measures.CoreMetrics.MAINTAINABILITY_ISSUES` and `org.sonar.api.measures.CoreMetrics.RELIABILITY_ISSUES`.

## 10.5

* Introduce `org.sonar.api.issue.IssueStatus` to simplify `status` and `resolution` on issues.
* Deprecate `org.sonar.api.ce.measure.Issue.status()` and `org.sonar.api.ce.measure.Issue.resolution()`. Use `org.sonar.api.ce.measure.Issue.issueStatus()` method instead.
* Deprecate `org.sonar.api.issue.Issue.status()` and `org.sonar.api.issue.Issue.resolution()`. No replacement.
* Deprecate `STATUS_OPEN`, `STATUS_CONFIRMED`, `STATUS_REOPENED`, `STATUS_RESOLVED`, `STATUS_CLOSED`,
  `RESOLUTION_FIXED`, `RESOLUTION_FALSE_POSITIVE`, `RESOLUTION_REMOVED`, `RESOLUTION_WONT_FIX`, use `org.sonar.api.issue.IssueStatus` enum instead
* Deprecate `RESOLUTION_SAFE`, `RESOLUTION_ACKNOWLEDGED`, `STATUS_TO_REVIEW`, `STATUS_REVIEWED`. No replacement.
* Deprecate `org.sonar.api.measures.CoreMetrics.REOPENED_ISSUES`, `org.sonar.api.measures.CoreMetrics.OPEN_ISSUES`. Use `org.sonar.api.measures.CoreMetrics.VIOLATIONS` instead.
* Deprecate `org.sonar.api.measures.CoreMetrics.CONFIRMED_ISSUES`. No replacement.

## 10.4

* Add new metrics `org.sonar.api.measures.CoreMetrics.NEW_ACCEPTED_ISSUES` and `org.sonar.api.measures.CoreMetrics.HIGH_IMPACT_ACCEPTED_ISSUES`.
* Add new metric `org.sonar.api.measures.CoreMetrics.PULL_REQUEST_FIXED_ISSUES` to represent issues that would be fixed by the pull request.
* Fixed misleading javadoc of `org.sonar.api.config.Configuration` to make it clear that at Compute Engine level project configuration is not provided.
* Deprecate `org.sonar.api.issue.DefaultTransitions.UNCOMFIRM`. There is no replacement as `org.sonar.api.issue.DefaultTransitions.CONFIRM` is subject to removal in the future.
* Deprecate `org.sonar.api.issue.DefaultTransitions.CONFIRM`. Use `org.sonar.api.issue.DefaultTransitions.ACCEPT` instead.

## 10.3

* Deprecate `org.sonar.api.measures.CoreMetrics.WONT_FIX_ISSUES` metric and related key.
* Introduce `org.sonar.api.measures.CoreMetrics.ACCEPTED_ISSUES` which effectively replaces `org.sonar.api.measures.CoreMetrics.WONT_FIX_ISSUES`
* Introduce `org.sonar.api.issue.DefaultTransitions.ACCEPT` which effectively replaces `org.sonar.api.issue.DefaultTransitions.WONT_FIX`.
* Deprecate `org.sonar.api.issue.DefaultTransitions.WONT_FIX`. Use `org.sonar.api.issue.DefaultTransitions.ACCEPT` instead.

## 10.2

* Introduce email property type: `org.sonar.api.PropertyType.EMAIL`

## 10.1

* Remove @Beta Code Characteristics `org.sonar.api.code.CodeCharacteristic`
* Introduce `org.sonar.api.issue.impact.SoftwareQuality` and `org.sonar.api.issue.impact.Severity` to define impacts of rules and issues
* Introduce `org.sonar.api.rules.CleanCodeAttribute` to define clean code attribute on rules.
* Deprecate `org.sonar.api.rules.RuleType`, `org.sonar.api.batch.rule.Severity` and `org.sonar.api.rule.Severity`. Use impacts with `org.sonar.api.issue.impact.SoftwareQuality` and `org.sonar.api.issue.impact.Severity` instead
* Deprecate `org.sonar.api.server.rule.RuleTagsToTypeConverter`

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

* Introduce `org.sonar.api.batch.sensor.issue.NewIssue.setCodeVariants(Iterable<String>)` for analyzers to contribute issues for given variants (e.g. different target platforms)
* Introduce 'org.sonar.api.issue.Issue.codeVariants()' 
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

## 9.14

* Properties `sonar.tests.inclusions` and `sonar.tests.exclusions` added to `org.sonar.api.CoreProperties` as alias for `sonar.test.inclusions` and `sonar.test.exclusion`

# 9.13

* Support for plugins to add messages to issue locations with formatting:
  * Added interface `org.sonar.api.batch.sensor.issue.MessageFormatting`
  * Added interface `org.sonar.api.batch.sensor.issue.NewMessageFormatting`
  * Added method `org.sonar.api.batch.sensor.issue.IssueLocation.messageFormattings()` 
  * Added method `org.sonar.api.batch.sensor.issue.NewIssueLocation.message(String, List<NewMessageFormatting>)`
  * Added method `org.sonar.api.batch.sensor.issue.NewIssueLocation.newMessageFormatting`
* Support for plugins to add quick fixes to issues:
  * Added interface `org.sonar.api.batch.sensor.issue.fix.InputFileEdit`
  * Added interface `org.sonar.api.batch.sensor.issue.fix.NewInputFileEdit`
  * Added interface `org.sonar.api.batch.sensor.issue.fix.NewQuickFix`
  * Added interface `org.sonar.api.batch.sensor.issue.fix.NewTextEdit`
  * Added interface `org.sonar.api.batch.sensor.issue.fix.QuickFix`
  * Added interface `org.sonar.api.batch.sensor.issue.fix.TextEdit`
  * Added method `org.sonar.api.batch.sensor.issue.Issue.quickFixes()`
  * Added method `org.sonar.api.batch.sensor.issue.NewIssue.newQuickFix()`
  * Added method `org.sonar.api.batch.sensor.issue.NewIssue.addQuickFix(NewQuickFix)`

## 9.12

* Added a new property type: `org.sonar.api.PropertyType.FORMATTED_TEXT`
