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
package org.sonar.api.user;

import com.google.common.base.Strings;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserGroupValidationTest {

  @Test
  public void fail_when_group_name_is_Anyone() {
    assertThatThrownBy(() -> UserGroupValidation.validateGroupName("AnyOne"))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Anyone group cannot be used");
  }

  @Test
  public void fail_when_group_name_is_empty() {
    assertThatThrownBy(() -> UserGroupValidation.validateGroupName(""))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Group name cannot be empty");
  }

  @Test
  public void fail_when_group_name_contains_only_blank() {
    assertThatThrownBy(() -> UserGroupValidation.validateGroupName("     "))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Group name cannot be empty");
  }

  @Test
  public void fail_when_group_name_is_too_big() {
    assertThatThrownBy(() -> UserGroupValidation.validateGroupName(Strings.repeat("name", 300)))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Group name cannot be longer than 255 characters");
  }

  @Test
  public void fail_when_group_name_is_null() {
    assertThatThrownBy(() -> UserGroupValidation.validateGroupName(null))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Group name cannot be empty");
  }
}
