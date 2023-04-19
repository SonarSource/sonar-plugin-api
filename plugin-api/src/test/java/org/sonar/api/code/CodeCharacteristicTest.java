/*
 * Sonar Plugin API
 * Copyright (C) 2009-2023 SonarSource SA
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
package org.sonar.api.code;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sonar.api.code.CharacteristicsCategory.DEVELOPMENT;
import static org.sonar.api.code.CharacteristicsCategory.PRODUCTION;
import static org.sonar.api.code.CodeCharacteristic.CLEAR;
import static org.sonar.api.code.CodeCharacteristic.ROBUST;

public class CodeCharacteristicTest {

  @Test
  public void getCharacteristicsCategory_forDifferentCharacteristic_returnDifferentCategories() {
    assertThat(ROBUST.getCharacteristicsCategory()).isEqualTo(PRODUCTION);
    assertThat(CLEAR.getCharacteristicsCategory()).isEqualTo(DEVELOPMENT);
  }

  @Test
  public void values_shouldNotBeEmpty() {
    CodeCharacteristic[] values = CodeCharacteristic.values();

    assertThat(values).isNotEmpty();
  }
}
