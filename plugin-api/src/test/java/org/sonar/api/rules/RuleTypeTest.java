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
package org.sonar.api.rules;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RuleTypeTest {


  @Test
  public void test_valueOf_db_constant() {
    assertThat(RuleType.valueOf(1)).isEqualTo(RuleType.CODE_SMELL);
    assertThat(RuleType.valueOf(2)).isEqualTo(RuleType.BUG);
  }

  @Test
  public void valueOf_throws_ISE_if_unsupported_db_constant() {
    assertThatThrownBy(() -> RuleType.valueOf(5))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Unsupported type value : 5");
  }

  @Test
  public void test_ALL_NAMES() {
    assertThat(RuleType.names()).containsOnly("BUG", "VULNERABILITY", "CODE_SMELL", "SECURITY_HOTSPOT");
  }

  @Test
  public void ALL_NAMES_is_immutable() {
    assertThatThrownBy(() -> RuleType.names().add("foo"))
      .isInstanceOf(UnsupportedOperationException.class);
  }
}
