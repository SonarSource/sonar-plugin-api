/*
 * Sonar Plugin API
 * Copyright (C) 2009-2025 SonarSource Sàrl
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

import java.io.File;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.sonar.api.utils.Version;

/**
 * This class loads Sonar plugin metadata from JAR manifest.
 */
public final class PluginManifest {

  public static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

  private final String key;
  private final String name;
  private final String mainClass;
  private final String description;
  private final String organization;
  private final String organizationUrl;
  private final String license;
  private final String version;
  private final String displayVersion;
  @Nullable
  private final Version sonarPluginApiMinVersion;
  private final List<String> dependencies;
  private final String homepage;
  private final String termsConditionsUrl;
  private final ZonedDateTime buildDate;
  private final String issueTrackerUrl;
  private final boolean useChildFirstClassLoader;
  private final String basePlugin;
  private final String implementationBuild;
  private final String sourcesUrl;
  private final List<String> developers;
  private final List<RequiredPlugin> requiredPlugins;
  private final boolean sonarlintSupported;
  private final List<String> requiredForLanguages;
  @Nullable
  private final Version jreMinVersion;
  @Nullable
  private final Version nodeJsMinVersion;

  /**
   * Load the manifest from a JAR file.
   */
  public PluginManifest(File jarFile) {
    this(loadManifestFromFile(jarFile.toPath()));
  }

  /**
   * Load the manifest from a JAR file.
   */
  public PluginManifest(Path jarFilePath) {
    this(loadManifestFromFile(jarFilePath));
  }

  private static Manifest loadManifestFromFile(Path path) {
    try (JarFile jar = new JarFile(path.toFile())) {
      Manifest manifest = jar.getManifest();
      return manifest != null ? manifest : new Manifest();
    } catch (Exception e) {
      throw new IllegalStateException("Unable to read plugin manifest from jar : " + path.toAbsolutePath(), e);
    }
  }

  /**
   * @param manifest can not be null
   */
  public PluginManifest(Manifest manifest) {
    Attributes attributes = manifest.getMainAttributes();
    this.key = PluginKeyUtils.sanitize(attributes.getValue(PluginManifestProperty.KEY.getKey()));
    this.mainClass = attributes.getValue(PluginManifestProperty.MAIN_CLASS.getKey());
    this.name = attributes.getValue(PluginManifestProperty.NAME.getKey());
    this.description = attributes.getValue(PluginManifestProperty.DESCRIPTION.getKey());
    this.license = attributes.getValue(PluginManifestProperty.LICENSE.getKey());
    this.organization = attributes.getValue(PluginManifestProperty.ORGANIZATION.getKey());
    this.organizationUrl = attributes.getValue(PluginManifestProperty.ORGANIZATION_URL.getKey());
    this.version = attributes.getValue(PluginManifestProperty.VERSION.getKey());
    this.displayVersion = attributes.getValue(PluginManifestProperty.DISPLAY_VERSION.getKey());
    this.homepage = attributes.getValue(PluginManifestProperty.HOMEPAGE.getKey());
    this.termsConditionsUrl = attributes.getValue(PluginManifestProperty.TERMS_CONDITIONS_URL.getKey());
    this.sonarPluginApiMinVersion = parseVersion(attributes, PluginManifestProperty.SONAR_VERSION);
    this.issueTrackerUrl = attributes.getValue(PluginManifestProperty.ISSUE_TRACKER_URL.getKey());
    this.buildDate = parseInstant(attributes.getValue(PluginManifestProperty.BUILD_DATE.getKey()));
    this.useChildFirstClassLoader = "true".equalsIgnoreCase(attributes.getValue(PluginManifestProperty.USE_CHILD_FIRST_CLASSLOADER.getKey()));
    this.sonarlintSupported = "true".equalsIgnoreCase(attributes.getValue(PluginManifestProperty.SONARLINT_SUPPORTED.getKey()));
    this.basePlugin = attributes.getValue(PluginManifestProperty.BASE_PLUGIN.getKey());
    this.implementationBuild = attributes.getValue(PluginManifestProperty.IMPLEMENTATION_BUILD.getKey());
    this.sourcesUrl = attributes.getValue(PluginManifestProperty.SOURCES_URL.getKey());

    String deps = attributes.getValue(PluginManifestProperty.DEPENDENCIES.getKey());
    this.dependencies = List.of(StringUtils.split(StringUtils.defaultString(deps), ' '));

    String devs = attributes.getValue(PluginManifestProperty.DEVELOPERS.getKey());
    this.developers = List.of(StringUtils.split(StringUtils.defaultString(devs), ','));

    String requires = attributes.getValue(PluginManifestProperty.REQUIRE_PLUGINS.getKey());
    this.requiredPlugins = Stream.of(StringUtils.split(StringUtils.defaultString(requires), ','))
      .map(RequiredPlugin::parse).collect(Collectors.toUnmodifiableList());

    String languages = attributes.getValue(PluginManifestProperty.LANGUAGES.getKey());
    this.requiredForLanguages = List.of(StringUtils.split(StringUtils.defaultString(languages), ','));

    this.jreMinVersion = parseVersion(attributes, PluginManifestProperty.JRE_MIN_VERSION);
    this.nodeJsMinVersion = parseVersion(attributes, PluginManifestProperty.NODEJS_MIN_VERSION);
  }

  @Nullable
  private static Version parseVersion(Attributes attributes, PluginManifestProperty manifestProperty) {
    return Optional.ofNullable(attributes.getValue(manifestProperty.getKey()))
      .map(Version::create)
      .orElse(null);
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public List<RequiredPlugin> getRequiredPlugins() {
    return requiredPlugins;
  }

  public String getDescription() {
    return description;
  }

  public String getOrganization() {
    return organization;
  }

  public String getOrganizationUrl() {
    return organizationUrl;
  }

  public String getLicense() {
    return license;
  }

  public String getVersion() {
    return version;
  }

  public String getDisplayVersion() {
    return displayVersion;
  }

  public Optional<Version> getSonarPluginApiMinVersion() {
    return Optional.ofNullable(sonarPluginApiMinVersion);
  }

  public String getMainClass() {
    return mainClass;
  }

  public List<String> getDependencies() {
    return dependencies;
  }

  public ZonedDateTime getBuildDate() {
    return buildDate;
  }

  public String getHomepage() {
    return homepage;
  }

  public String getTermsConditionsUrl() {
    return termsConditionsUrl;
  }

  public String getIssueTrackerUrl() {
    return issueTrackerUrl;
  }

  public boolean isUseChildFirstClassLoader() {
    return useChildFirstClassLoader;
  }

  public boolean isSonarLintSupported() {
    return sonarlintSupported;
  }

  public String getBasePlugin() {
    return basePlugin;
  }

  public String getImplementationBuild() {
    return implementationBuild;
  }

  public String getSourcesUrl() {
    return sourcesUrl;
  }

  public List<String> getDevelopers() {
    return developers;
  }

  public List<String> getRequiredForLanguages() {
    return requiredForLanguages;
  }

  public Optional<Version> getJreMinVersion() {
    return Optional.ofNullable(jreMinVersion);
  }

  public Optional<Version> getNodeJsMinVersion() {
    return Optional.ofNullable(nodeJsMinVersion);
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }

  public boolean isValid() {
    return StringUtils.isNotBlank(key) && StringUtils.isNotBlank(version);
  }

  public static ZonedDateTime parseInstant(String s) {
    try {
      if (StringUtils.isNotBlank(s)) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN, Locale.US);
        return ZonedDateTime.parse(s, dateTimeFormatter);
      }
      return null;
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("The following value does not respect the date pattern " + DATETIME_PATTERN + ": " + s, e);
    }
  }

}
