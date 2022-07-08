# Changelog

TODO latest releases

## Release 9.3
![](/images/check.svg) Added
* `sonar-plugin-api.src.main.java.org.sonar.api.resources.Language#publishAllFiles` to define whether the files identified with the language should be automatically published to SonarQube.
* `org.sonar.api.batch.sensor.SensorDescriptor#processesFilesIndependently`

## Release 9.0
![](/images/exclamation.svg) Deprecated
* `org.sonar.api.server.rule.RulesDefinitionXmlLoader` is deprecated. Use the `sonar-check-api` to annotate rule classes instead of loading the metadata from XML files

![](/images/cross.svg) Removed
* `org.sonar.api.ExtensionProvider` Use `org.sonar.api.Plugin.Context#addExtensions()` to add objects to the container.
* `org.sonar.api.batch.sensor.SensorDescriptor#requireProperty()`. Use `#onlyWhenConfiguration()` instead. 
* All API related to preview/issues analysis mode.
* Coverage types (unit, IT, overall) was removed.
* Resource perspectives. Use methods in `SensorContext`.
* `org.sonar.api.platform.Server#getRootDir()`. Use `ServerFileSystem#getHomeDir()`.
* `org.sonar.api.profiles.ProfileDefinition.java`. Define quality profiles with `BuiltInQualityProfilesDefinition`.
* `org.sonar.api.rules.XMLRuleParser`. Use the `sonar-check-api` to annotate rule classes.

## Release 8.4
![](/images/check.svg) Added
* `org.sonar.api.batch.scm.ScmProvider#forkDate`

![](/images/exclamation.svg) Deprecated
* `org.sonar.api.rules.Rule#getId()` is deprecated and will always throw UnsupportedOperationException.

## Release 8.3
![](/images/exclamation.svg) Deprecated
* `org.sonar.api.utils.text.JsonWriter`

## Release 8.2
No changes

## Release 8.1
No changes

## Release 8.0
No changes
 
## Release 7.9
No changes

## Release 7.8

![](/images/check.svg) Added
* `org.sonar.api.web.WebAnalytics`

![](/images/exclamation.svg) Deprecated
* `org.sonar.api.i18n.I18`
* `org.sonar.api.SonarQubeVersion` use `org.sonar.api.SonarRuntime` instead
* `org.sonar.api.profiles.XMLProfileParser`
* `org.sonar.api.notifications.NotificationChannel`

![](/images/cross.svg) Removed
* Pico components relying on reflection to have their `start` or `stop` method called. Make your component implements `org.sonar.api.Startable` instead.

## Release 7.7

![](/images/check.svg) Added
* ` org.sonar.api.batch.scm.ScmProvider#ignoreCommand`

![](/images/exclamation.svg) Deprecated
* `org.sonar.api.batch.fs.InputFile::status`
* `org.sonar.api.resources.Qualifiers#BRC`

![](/images/cross.svg) Removed
* The preview/issues mode of scanner has been removed

## Release 7.6

![](/images/info.svg) Changed

* `PostJob` moved to project level IoC container
* `InputFileFilter` moved to project level IoC container

![](/images/check.svg) Added

* New annotation `org.sonar.api.scanner.ScannerSide` to mark (project level) scanner components
* `org.sonar.api.batch.fs.InputProject` to create issues on project
* `org.sonar.api.scanner.ProjectSensor` to declare Sensors that only run at project level

![](/images/exclamation.svg) Deprecated

* `org.sonar.scanner.issue.IssueFilter` deprecated
* `org.sonar.api.batch.InstantiationStrategy` deprecated
* `org.sonar.api.batch.ScannerSide` deprecated
* `org.sonar.api.batch.fs.InputModule` deprecated
* Concept of global Sensor is deprecated (use `ProjectSensor` instead)

![](/images/cross.svg) Removed

* Support of scanner tasks was removed
* `RulesProfile` is no longer available for scanner side components (use `ActiveRules` instead)

## Release 7.5
No changes

## Release 7.4
![](/images/info.svg) Changed

* Allow identity provider to not provide login

![](/images/check.svg) Added

* Allow sensors to report adhoc rules metadata

![](/images/cross.svg) Removed

* `org.sonar.api.rules.RuleFinder` removed from scanner side
* `sonar-channel` removed from plugin classloader
* stop support of plugins compiled with API < 5.2

## Release 7.3

![](/images/check.svg) Added

* `RulesDefinitions` supports HotSpots and security standards

![](/images/exclamation.svg) Deprecated
* `org.sonar.api.batch.AnalysisMode` and `org.sonar.api.issue.ProjectIssues` since preview mode is already deprecated for a while

## Release 7.2
![](/images/check.svg) Added
* `org.sonar.api.batch.sensor.SensorContext#newExternalIssue` to report external issues
* `org.sonar.api.batch.sensor.SensorContext#newSignificantCode` to report part of the source file that should be used for issue tracking
* `org.sonar.api.scan.issue.filter.FilterableIssue#textRange`

![](/images/exclamation.svg) Deprecated
* org.sonar.api.scan.issue.filter.FilterableIssue#line

## Release 7.1
![](/images/check.svg) Added
* `org.sonar.api.Plugin.Context#getBootConfiguration`
* `org.sonar.api.server.rule.RulesDefinition.NewRule#addDeprecatedRuleKey` to support deprecated rule keys

## Release 7.0
![](/images/check.svg) Added
* `org.sonar.api.batch.scm.ScmProvider#relativePathFromScmRoot`, `org.sonar.api.batch.scm.ScmProvider#branchChangedFiles` and `org.sonar.api.batch.scm.ScmProvider#revisionId` to improve branch and PR support

## Release 6.7
No changes
