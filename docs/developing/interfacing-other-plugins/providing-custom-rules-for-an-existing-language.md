# Providing custom rules for an existing language

The most common use case is to write a custom plugin that will extend a base plugin by contributing additional rules (see for example how it is done for [Java](https://github.com/SonarSource/sonar-java) analysis).
The main plugin will expose some APIs that will be implemented/used by the "custom rule" plugins.

## Extending other plugins

Get inspired by https://github.com/SonarSource/sonar-java/blob/master/docs/CUSTOM_RULES_101.md

### Links

Contributing custom rules for Java: https://github.com/SonarSource/sonar-java/blob/master/docs/CUSTOM_RULES_101.md
Find links for other languages ?

## Exposing APIs to other plugins

Plugins are loaded in isolated classloaders. It means a plugin can't access another plugin's classes. There is an exception for package names following pattern `org.sonar.plugins.<pluginKey>.api`. For example all classes in a plugin with the key myplugin that are located in `org.sonar.plugins.myplugin.api` are visible to other plugins.