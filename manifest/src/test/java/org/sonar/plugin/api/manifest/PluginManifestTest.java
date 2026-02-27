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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.junit.jupiter.api.Test;
import org.sonar.api.utils.Version;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PluginManifestTest {

  @Test
  void test() {
    File fakeJar = new File("fake.jar");
    assertThatThrownBy(() -> new PluginManifest(fakeJar))
      .isInstanceOf(RuntimeException.class);
  }

  @Test
  void should_create_manifest() throws URISyntaxException {
    URL jar = getClass().getResource("/checkstyle-plugin.jar");

    PluginManifest manifest = new PluginManifest(Path.of(jar.toURI()));

    assertThat(manifest.getKey()).isEqualTo("checkstyle");
    assertThat(manifest.getName()).isEqualTo("Checkstyle");
    assertThat(manifest.getRequiredPlugins()).isEmpty();
    assertThat(manifest.getMainClass()).isEqualTo("org.sonar.plugins.checkstyle.CheckstylePlugin");
    assertThat(manifest.getVersion().length()).isGreaterThan(1);
    assertThat(manifest.isUseChildFirstClassLoader()).isFalse();
    assertThat(manifest.isSonarLintSupported()).isFalse();
    assertThat(manifest.getDependencies()).hasSize(2);
    assertThat(manifest.getDependencies()).containsOnly("META-INF/lib/antlr-2.7.7.jar", "META-INF/lib/checkstyle-5.5.jar");
    assertThat(manifest.getImplementationBuild()).isEqualTo("b9283404030db9ce1529b1fadfb98331686b116d");
    assertThat(manifest.getRequiredForLanguages()).isEmpty();
  }

  @Test
  void do_not_fail_when_no_old_plugin_manifest() throws URISyntaxException {
    URL jar = getClass().getResource("/old-plugin.jar");

    PluginManifest manifest = new PluginManifest(new File(jar.toURI()));

    assertThat(manifest.getKey()).isNull();
    assertThat(manifest.getName()).isNull();
    assertThat(manifest.getRequiredPlugins()).isEmpty();
    assertThat(manifest.getMainClass()).isEqualTo("org.sonar.plugins.checkstyle.CheckstylePlugin");
    assertThat(manifest.isUseChildFirstClassLoader()).isFalse();
    assertThat(manifest.getDependencies()).isEmpty();
    assertThat(manifest.getImplementationBuild()).isNull();
    assertThat(manifest.getDevelopers()).isEmpty();
    assertThat(manifest.getSourcesUrl()).isNull();
  }

  @Test
  void should_add_develpers() throws URISyntaxException {
    URL jar = getClass().getResource("/plugin-with-devs.jar");

    PluginManifest manifest = new PluginManifest(new File(jar.toURI()));

    assertThat(manifest.getDevelopers()).contains("Firstname1 Name1", "Firstname2 Name2");
  }

  @Test
  void should_add_sources_url() throws URISyntaxException {
    URL jar = getClass().getResource("/plugin-with-sources.jar");

    PluginManifest manifest = new PluginManifest(new File(jar.toURI()));

    assertThat(manifest.getSourcesUrl()).isEqualTo("https://github.com/SonarSource/project");
  }

  @Test
  void should_add_requires_plugins() throws URISyntaxException {
    URL jar = getClass().getResource("/plugin-with-require-plugins.jar");

    PluginManifest manifest = new PluginManifest(new File(jar.toURI()));

    assertThat(manifest.getRequiredPlugins()).hasSize(2);
    assertThat(manifest.getRequiredPlugins().get(0).getKey()).isEqualTo("scm");
    assertThat(manifest.getRequiredPlugins().get(0).getMinimalVersion()).hasToString("1.0");
    assertThat(manifest.getRequiredPlugins().get(1).getKey()).isEqualTo("fake");
    assertThat(manifest.getRequiredPlugins().get(1).getMinimalVersion()).hasToString("1.1");
  }

  @Test
  void should_add_languages() throws URISyntaxException {
    URL jar = getClass().getResource("/plugin-with-require-for-languages.jar");

    PluginManifest manifest = new PluginManifest(new File(jar.toURI()));

    assertThat(manifest.getRequiredForLanguages()).hasSize(2);
    assertThat(manifest.getRequiredForLanguages().get(0)).isEqualTo("java");
    assertThat(manifest.getRequiredForLanguages().get(1)).isEqualTo("xml");
  }

  @Test
  void should_create_manifest_from_manifest_object() {
    Manifest manifest = new Manifest();
    Attributes attributes = manifest.getMainAttributes();
    attributes.putValue(PluginManifestProperty.KEY.getKey(), "test-key");
    attributes.putValue(PluginManifestProperty.NAME.getKey(), "Test Plugin");
    attributes.putValue(PluginManifestProperty.VERSION.getKey(), "1.0");
    attributes.putValue(PluginManifestProperty.MAIN_CLASS.getKey(), "com.example.TestPlugin");
    attributes.putValue(PluginManifestProperty.DESCRIPTION.getKey(), "Test Description");
    attributes.putValue(PluginManifestProperty.ORGANIZATION.getKey(), "Test Org");
    attributes.putValue(PluginManifestProperty.ORGANIZATION_URL.getKey(), "https://test.org");
    attributes.putValue(PluginManifestProperty.LICENSE.getKey(), "MIT");
    attributes.putValue(PluginManifestProperty.HOMEPAGE.getKey(), "https://test.com");
    attributes.putValue(PluginManifestProperty.TERMS_CONDITIONS_URL.getKey(), "https://test.com/terms");
    attributes.putValue(PluginManifestProperty.SONAR_VERSION.getKey(), "9.0");
    attributes.putValue(PluginManifestProperty.ISSUE_TRACKER_URL.getKey(), "https://test.com/issues");
    attributes.putValue(PluginManifestProperty.USE_CHILD_FIRST_CLASSLOADER.getKey(), "true");
    attributes.putValue(PluginManifestProperty.SONARLINT_SUPPORTED.getKey(), "true");
    attributes.putValue(PluginManifestProperty.BASE_PLUGIN.getKey(), "base-plugin");
    attributes.putValue(PluginManifestProperty.IMPLEMENTATION_BUILD.getKey(), "abc123");
    attributes.putValue(PluginManifestProperty.SOURCES_URL.getKey(), "https://github.com/test");
    attributes.putValue(PluginManifestProperty.DISPLAY_VERSION.getKey(), "1.0.0");
    attributes.putValue(PluginManifestProperty.BUILD_DATE.getKey(), "2024-01-15T10:30:00+0000");

    PluginManifest pluginManifest = new PluginManifest(manifest);

    assertThat(pluginManifest.getKey()).isEqualTo("testkey");
    assertThat(pluginManifest.getName()).isEqualTo("Test Plugin");
    assertThat(pluginManifest.getVersion()).isEqualTo("1.0");
    assertThat(pluginManifest.getMainClass()).isEqualTo("com.example.TestPlugin");
    assertThat(pluginManifest.getDescription()).isEqualTo("Test Description");
    assertThat(pluginManifest.getOrganization()).isEqualTo("Test Org");
    assertThat(pluginManifest.getOrganizationUrl()).isEqualTo("https://test.org");
    assertThat(pluginManifest.getLicense()).isEqualTo("MIT");
    assertThat(pluginManifest.getHomepage()).isEqualTo("https://test.com");
    assertThat(pluginManifest.getTermsConditionsUrl()).isEqualTo("https://test.com/terms");
    assertThat(pluginManifest.getSonarPluginApiMinVersion()).hasValue(Version.create("9.0"));
    assertThat(pluginManifest.getIssueTrackerUrl()).isEqualTo("https://test.com/issues");
    assertThat(pluginManifest.isUseChildFirstClassLoader()).isTrue();
    assertThat(pluginManifest.isSonarLintSupported()).isTrue();
    assertThat(pluginManifest.getBasePlugin()).isEqualTo("base-plugin");
    assertThat(pluginManifest.getImplementationBuild()).isEqualTo("abc123");
    assertThat(pluginManifest.getSourcesUrl()).isEqualTo("https://github.com/test");
    assertThat(pluginManifest.getDisplayVersion()).isEqualTo("1.0.0");
    assertThat(pluginManifest.getBuildDate()).isNotNull();
  }

  @Test
  void should_return_immutable_lists() throws URISyntaxException {
    URL jar = getClass().getResource("/checkstyle-plugin.jar");
    PluginManifest manifest = new PluginManifest(new File(jar.toURI()));

    List<String> dependencies = manifest.getDependencies();
    assertThat(dependencies).hasSize(2);
    assertThat(dependencies.get(0)).isEqualTo("META-INF/lib/antlr-2.7.7.jar");

    assertThatThrownBy(() -> dependencies.add("modified"))
      .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void parseInstant_should_parse_valid_date() {
    ZonedDateTime result = PluginManifest.parseInstant("2024-01-15T10:30:00+0000");

    assertThat(result).isNotNull();
    assertThat(result.getYear()).isEqualTo(2024);
    assertThat(result.getMonthValue()).isEqualTo(1);
    assertThat(result.getDayOfMonth()).isEqualTo(15);
  }

  @Test
  void parseInstant_should_return_null_for_blank_string() {
    assertThat(PluginManifest.parseInstant("")).isNull();
    assertThat(PluginManifest.parseInstant("   ")).isNull();
    assertThat(PluginManifest.parseInstant(null)).isNull();
  }

  @Test
  void parseInstant_should_throw_exception_for_invalid_format() {
    assertThatThrownBy(() -> PluginManifest.parseInstant("invalid-date"))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("does not respect the date pattern");
  }

  @Test
  void isValid_should_return_true_when_key_and_version_present() {
    Manifest manifest = new Manifest();
    Attributes attributes = manifest.getMainAttributes();
    attributes.putValue(PluginManifestProperty.KEY.getKey(), "mykey");
    attributes.putValue(PluginManifestProperty.VERSION.getKey(), "1.0");

    PluginManifest pluginManifest = new PluginManifest(manifest);

    assertThat(pluginManifest.isValid()).isTrue();
  }

  @Test
  void isValid_should_return_false_when_key_missing() {
    Manifest manifest = new Manifest();
    Attributes attributes = manifest.getMainAttributes();
    attributes.putValue(PluginManifestProperty.VERSION.getKey(), "1.0");

    PluginManifest pluginManifest = new PluginManifest(manifest);

    assertThat(pluginManifest.isValid()).isFalse();
  }

  @Test
  void isValid_should_return_false_when_version_missing() {
    Manifest manifest = new Manifest();
    Attributes attributes = manifest.getMainAttributes();
    attributes.putValue(PluginManifestProperty.KEY.getKey(), "mykey");

    PluginManifest pluginManifest = new PluginManifest(manifest);

    assertThat(pluginManifest.isValid()).isFalse();
  }

  @Test
  void isValid_should_return_false_when_key_blank() {
    Manifest manifest = new Manifest();
    Attributes attributes = manifest.getMainAttributes();
    attributes.putValue(PluginManifestProperty.KEY.getKey(), "   ");
    attributes.putValue(PluginManifestProperty.VERSION.getKey(), "1.0");

    PluginManifest pluginManifest = new PluginManifest(manifest);

    assertThat(pluginManifest.isValid()).isFalse();
  }

  @Test
  void isValid_should_return_false_when_version_blank() {
    Manifest manifest = new Manifest();
    Attributes attributes = manifest.getMainAttributes();
    attributes.putValue(PluginManifestProperty.KEY.getKey(), "mykey");
    attributes.putValue(PluginManifestProperty.VERSION.getKey(), "   ");

    PluginManifest pluginManifest = new PluginManifest(manifest);

    assertThat(pluginManifest.isValid()).isFalse();
  }

  @Test
  void toString_should_return_string_representation() {
    Manifest manifest = new Manifest();
    Attributes attributes = manifest.getMainAttributes();
    attributes.putValue(PluginManifestProperty.KEY.getKey(), "testkey");
    attributes.putValue(PluginManifestProperty.VERSION.getKey(), "1.0");

    PluginManifest pluginManifest = new PluginManifest(manifest);

    String result = pluginManifest.toString();

    assertThat(result)
      .contains("testkey")
      .contains("1.0");
  }
}
