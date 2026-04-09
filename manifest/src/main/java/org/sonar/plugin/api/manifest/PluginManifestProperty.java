/*
 * Sonar Plugin API
 * Copyright (C) SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugin.api.manifest;

public enum PluginManifestProperty {

  /**
   * (required) Contains only letters/digits and is unique among all plugins.
   * Constructed from the Maven artifactId. Given an artifactId of sonar-widget-lab-plugin, the pluginKey will be widgetlab.
   */
  KEY("Plugin-Key", "Key"),

  /**
   * (required) Plugin name displayed in various parts of the SonarQube products.
   */
  NAME("Plugin-Name", "Name"),

  /**
   * Plugin description displayed in various parts of the SonarQube products.
   */
  DESCRIPTION("Plugin-Description", "Description"),

  /**
   * (required) Name of the entry-point class that extends org.sonar.api.SonarPlugin.
   * Example: org.mycompany.sonar.plugins.widgetlab.WidgetLabPlugin
   */
  MAIN_CLASS("Plugin-Class", "Entry-point Class"),

  /**
   * The organization which develops the plugin, displayed in various parts of the SonarQube products.
   */
  ORGANIZATION("Plugin-Organization", "Organization"),

  /**
   * URL of the organization, displayed in various parts of the SonarQube products.
   */
  ORGANIZATION_URL("Plugin-OrganizationUrl", "Organization URL"),

  /**
   * Plugin license.
   */
  LICENSE("Plugin-License", "Licensing"),

  /**
   * (required) Plugin "raw" version. Should follow common version formats to be comparable.
   */
  VERSION("Plugin-Version", "Version"),

  /**
   * The version label, displayed in various parts of the SonarQube products.
   * By default, it's the raw version (e.g., "1.2"), but can be overridden to "1.2 (build 12345)" for instance.
   */
  DISPLAY_VERSION("Plugin-Display-Version", "Display Version"),

  /**
   * Minimal version of supported Sonar Plugin API at runtime.
   * For example, if the value is 9.8.0.203, then deploying the plugin on SonarQube versions
   * with sonar-plugin-api 9.6.1.114 (i.e., SonarQube 9.5) and lower will fail.
   */
  SONAR_VERSION("Sonar-Version", "Minimal Sonar Plugin API Version"),

  /**
   * List of Maven dependencies packaged with the plugin.
   */
  DEPENDENCIES("Plugin-Dependencies", "Dependencies"),

  /**
   * Homepage of website, for example {@code https://github.com/SonarQubeCommunity/sonar-widget-lab}
   */
  HOMEPAGE("Plugin-Homepage", "Homepage URL"),

  /**
   * Users must read this document when installing the plugin from Marketplace.
   */
  TERMS_CONDITIONS_URL("Plugin-TermsConditionsUrl", "Terms and Conditions"),

  /**
   * Build date of the plugin. Should follow the pattern yyyy-MM-dd'T'HH:mm:ssZ
   */
  BUILD_DATE("Plugin-BuildDate", "Build date"),

  /**
   * URL of the issue tracker, for example {@code https://github.com/SonarQubeCommunity/sonar-widget-lab/issues}
   */
  ISSUE_TRACKER_URL("Plugin-IssueTrackerUrl", "Issue Tracker URL"),

  /**
   * Comma-separated list of plugin keys that this plugin depends on.
   * Can also specify a minimal version for each required plugin, using the format "pluginKey:version", for example "widgetlab:1.2.0".
   */
  REQUIRE_PLUGINS("Plugin-RequirePlugins", "Required Plugins"),

  /**
   * Whether the language plugin supports SonarLint or not.
   * Only Sonar analyzers and custom rules plugins for Sonar analyzers should set this to true.
   */
  SONARLINT_SUPPORTED("SonarLint-Supported", "Does the plugin support SonarLint?"),

  /**
   * Each plugin is executed in an isolated classloader, which inherits a shared classloader that contains API and some other classes.
   * By default, the loading strategy of classes is parent-first (look up in shared classloader then in plugin classloader).
   * If the property is true, then the strategy is child-first.
   * This property is mainly used when building plugin against API < 5.2, as the shared classloader contained many 3rd party libraries (guava 10, commons-lang, …).
   */
  USE_CHILD_FIRST_CLASSLOADER("Plugin-ChildFirstClassLoader", "Use Child-first ClassLoader"),

  /**
   * If specified, then the plugin is executed in the same classloader as the base plugin.
   */
  BASE_PLUGIN("Plugin-Base", "Base Plugin"),

  /**
   * Identifier of build or commit, for example the Git SHA1 (94638028f0099de59f769cdca776e506684235d6).
   * It is displayed for debugging purposes in logs when the SonarQube server starts.
   */
  IMPLEMENTATION_BUILD("Implementation-Build", "Implementation Build"),

  /**
   * URL of SCM repository for open-source plugins, displayed in various parts of the SonarQube products.
   */
  SOURCES_URL("Plugin-SourcesUrl", "Sources URL"),

  /**
   * A list of developers, displayed in various parts of the SonarQube products.
   */
  DEVELOPERS("Plugin-Developers", "Developers"),

  /**
   * Minimal JRE specification version required to run the plugin.
   */
  JRE_MIN_VERSION("Jre-Min-Version", "Minimal JRE Specification Version"),

  /**
   * Minimal Node.js version required to run the plugin.
   */
  NODEJS_MIN_VERSION("NodeJs-Min-Version", "Minimal Node.js Version"),

  /**
   * Comma-separated list of languages for which this plugin should be downloaded.
   */
  LANGUAGES("Plugin-RequiredForLanguages", "Languages for which this plugin should be downloaded");

  private final String key;
  private final String label;

  PluginManifestProperty(String key, String label) {
    this.key = key;
    this.label = label;
  }

  public String getKey() {
    return key;
  }

  public String getLabel() {
    return label;
  }
}
