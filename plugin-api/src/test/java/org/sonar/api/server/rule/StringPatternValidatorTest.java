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
package org.sonar.api.server.rule;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

public class StringPatternValidatorTest {

  private static final StringPatternValidator VALIDATOR_WITH_COMMON_PATTERN = StringPatternValidator.validatorWithCommonPatternForKeys("test");
  private static final StringPatternValidator VALIDATOR_WITH_CUSTOM_PATTERN = new StringPatternValidator("custom" , "^a$");

  @Test
  public void is_valid() {
    assertThatCode(() -> VALIDATOR_WITH_COMMON_PATTERN.validate("validkey")).doesNotThrowAnyException();
    assertThatCode(() -> VALIDATOR_WITH_COMMON_PATTERN.validate("valid_key")).doesNotThrowAnyException();
    assertThatCode(() -> VALIDATOR_WITH_COMMON_PATTERN.validate("valid_key")).doesNotThrowAnyException();
    assertThatCode(() -> VALIDATOR_WITH_CUSTOM_PATTERN.validate("a")).doesNotThrowAnyException();
  }

  @Test
  public void is_invalid() {
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate((String) null));
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate(""));
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate(" "));
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate("你好"));
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate("invalid key"));
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate("Invalid_Key"));
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate("invalid#key"));
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate("invalid+key"));
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate(" invalid_key "));
    assertThatIllegalArgumentException().isThrownBy(() -> VALIDATOR_WITH_CUSTOM_PATTERN.validate("b"));
  }

  @Test
  public void validate_and_fail_with_correct_error_message() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> VALIDATOR_WITH_COMMON_PATTERN.validate("    "))
      .withMessage("Entry '    ' is invalid. For test the entry has to match the regexp ^[a-z0-9_]+$");
  }
}
