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

import org.sonar.api.Beta;
import org.sonar.api.rules.RuleType;

import static org.sonar.api.code.CharacteristicsCategory.*;

/**
 * Enum that represents Characteristic according to the new Clean Code Taxonomy. Should be used instead of {@link RuleType} as it will
 * become mandatory in the future to provide it.
 *
 * @since 9.16
 */
@Beta
public enum CodeCharacteristic {

  CLEAR(DEVELOPMENT), CONSISTENT(DEVELOPMENT), STRUCTURED(DEVELOPMENT), TESTED(DEVELOPMENT),

  ROBUST(PRODUCTION), SECURE(PRODUCTION), PORTABLE(PRODUCTION), COMPLIANT(PRODUCTION);

  private final CharacteristicsCategory characteristicsCategory;

  CodeCharacteristic(CharacteristicsCategory characteristicsCategory) {
    this.characteristicsCategory = characteristicsCategory;
  }

  public CharacteristicsCategory getCharacteristicsCategory() {
    return characteristicsCategory;
  }
}
