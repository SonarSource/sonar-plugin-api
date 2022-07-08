# Exposing APIs to other plugins

Plugins are loaded in isolated classloaders. It means a plugin can't access another plugin's classes. There is an exception for package names following pattern `org.sonar.plugins.<pluginKey>.api`. For example all classes in a plugin with the key myplugin that are located in `org.sonar.plugins.myplugin.api` are visible to other plugins.