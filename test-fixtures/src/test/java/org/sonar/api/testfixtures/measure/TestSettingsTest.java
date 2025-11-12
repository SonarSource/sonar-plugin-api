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
package org.sonar.api.testfixtures.measure;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestSettingsTest {

  private final TestSettings underTest = new TestSettings();

  @Test
  void get_string_value() {
    underTest.setValue("key", "value");

    assertThat(underTest.getString("key")).isEqualTo("value");
    assertThat(underTest.getString("unknown")).isNull();
  }

  @Test
  void get_string_array_value() {
    underTest.setValue("key", "value1,value2");

    assertThat(underTest.getStringArray("key")).containsOnly("value1", "value2");
    assertThat(underTest.getStringArray("unknown")).isEmpty();
  }
}
