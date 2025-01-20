/*
 * Sonar Plugin API
 * Copyright (C) 2009-2025 SonarSource SA
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
package org.sonar.api.config;

import java.util.Date;
import java.util.List;
import javax.annotation.CheckForNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SettingsTest {
  @Test
  void settings_can_be_subclassed() {
    var settings = new Settings() {
      @Override
      public boolean hasKey(String key) {
        return false;
      }

      @CheckForNull
      @Override
      public String getString(String key) {
        return "key";
      }

      @Override
      public boolean getBoolean(String key) {
        return false;
      }

      @Override
      public int getInt(String key) {
        return 0;
      }

      @Override
      public long getLong(String key) {
        return 0;
      }

      @CheckForNull
      @Override
      public Date getDate(String key) {
        return null;
      }

      @CheckForNull
      @Override
      public Date getDateTime(String key) {
        return null;
      }

      @CheckForNull
      @Override
      public Float getFloat(String key) {
        return null;
      }

      @CheckForNull
      @Override
      public Double getDouble(String key) {
        return null;
      }

      @Override
      public String[] getStringArray(String key) {
        return new String[]{key};
      }

      @Override
      public String[] getStringLines(String key) {
        return new String[0];
      }

      @Override
      public String[] getStringArrayBySeparator(String key, String separator) {
        return new String[0];
      }

      @Override
      public List<String> getKeysStartingWith(String prefix) {
        return null;
      }
    };

    assertThat(settings.getString("key")).isEqualTo("key");
  }
}
