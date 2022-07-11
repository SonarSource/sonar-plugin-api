/*
 * Sonar Plugin API
 * Copyright (C) 2009-2022 SonarSource SA
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
package org.sonar.api.server.rule;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

public class EducationPrincipleKeyFormatTest {
  @Test
  public void is_valid() {
    assertThatCode(() -> EducationPrincipleKeyFormat.validate("validkey")).doesNotThrowAnyException();
    assertThatCode(() -> EducationPrincipleKeyFormat.validate("valid_key")).doesNotThrowAnyException();
    assertThatCode(() -> EducationPrincipleKeyFormat.validate("validkey1")).doesNotThrowAnyException();
  }

  @Test
  public void is_invalid() {
    assertThatThrownBy(() -> EducationPrincipleKeyFormat.validate(null)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> EducationPrincipleKeyFormat.validate("")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> EducationPrincipleKeyFormat.validate(" ")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> EducationPrincipleKeyFormat.validate("你好")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> EducationPrincipleKeyFormat.validate("invalid key")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> EducationPrincipleKeyFormat.validate("Invalid_Key")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> EducationPrincipleKeyFormat.validate("invalid#key")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> EducationPrincipleKeyFormat.validate("invalid+key")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> EducationPrincipleKeyFormat.validate(" invalid_key ")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void validate_and_fail_with_correct_error_message() {
    try {
      EducationPrincipleKeyFormat.validate("  ");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("Entry '  ' is invalid. " + EducationPrincipleKeyFormat.ERROR_MESSAGE_SUFFIX);
    }
  }
}
