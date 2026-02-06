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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PluginKeyUtilsTest {

  @Test
  void shouldSanitizeMavenArtifactId() {
    assertThat(PluginKeyUtils.sanitize("sonar-test-plugin")).isEqualTo("test");
    assertThat(PluginKeyUtils.sanitize("test-sonar-plugin")).isEqualTo("test");
    assertThat(PluginKeyUtils.sanitize("test")).isEqualTo("test");

    assertThat(PluginKeyUtils.sanitize("sonar-test-foo-plugin")).isEqualTo("testfoo");
    assertThat(PluginKeyUtils.sanitize("test-foo-sonar-plugin")).isEqualTo("testfoo");
    assertThat(PluginKeyUtils.sanitize("test-foo")).isEqualTo("testfoo");
    assertThat(PluginKeyUtils.sanitize("keep.only-digits%12345&and*letters")).isEqualTo("keeponlydigits12345andletters");
    assertThat(PluginKeyUtils.sanitize("   remove whitespaces   ")).isEqualTo("removewhitespaces");
  }

  @Test
  void shouldReturnNullWhenSanitizingNull() {
    assertThat(PluginKeyUtils.sanitize(null)).isNull();
  }
}
