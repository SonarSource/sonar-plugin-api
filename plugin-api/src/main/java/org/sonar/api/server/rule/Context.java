/*
 * Sonar Plugin API
 * Copyright (C) 2009-2024 SonarSource SA
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

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.sonar.api.server.rule.StringPatternValidator.validatorWithCommonPatternForKeys;

/**
 * Describes the context in which a {@link RuleDescriptionSection} is defined. Contexts can be for example frameworks - each rule may have
 * slightly different description section for each framework (context).
 *
 * @since 9.7
 */
public class Context {

  private static final StringPatternValidator CONTEXT_KEY_VALIDATOR = validatorWithCommonPatternForKeys("context keys");

  private final String key;
  private final String displayName;

  /**
   * @param key         the context Key, must follow the pattern defined in {@link StringPatternValidator#COMMON_PATTERN_FOR_KEYS}
   * @param displayName
   */
  public Context(String key, String displayName) {
    failIfEmpty(key, "key must be provided and can't be empty");
    failIfEmpty(displayName, "displayName must be provided and can't be empty");
    CONTEXT_KEY_VALIDATOR.validate(key);
    this.key = key;
    this.displayName = displayName;
  }

  public String getKey() {
    return key;
  }

  public String getDisplayName() {
    return displayName;
  }

  private static void failIfEmpty(String str, String msg) {
    if (isEmpty(str)) {
      throw new IllegalArgumentException(msg);
    }
  }
}
