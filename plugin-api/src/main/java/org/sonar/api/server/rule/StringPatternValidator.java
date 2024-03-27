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

import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.stream.Collectors.toSet;

/**
 * Validates strings based on the defined regex. The error message is based on the error message passed in parameter.
 */
public class StringPatternValidator {

  public static final String COMMON_PATTERN_FOR_KEYS = "^[a-z0-9_]+$";
  private static final String IDENTIFIER_REGEX_DEFINITION = "For %s the entry has to match the regexp %s";

  private final String fieldUnderValidation;
  private final String validCharacterRegex;

  public StringPatternValidator(String fieldUnderValidation, String validCharacterRegex) {
    this.fieldUnderValidation = fieldUnderValidation;
    this.validCharacterRegex = validCharacterRegex;
  }

  public static StringPatternValidator validatorWithCommonPatternForKeys(String fieldUnderValidation) {
    return new StringPatternValidator(fieldUnderValidation, COMMON_PATTERN_FOR_KEYS);
  }

  public boolean isValid(@Nullable String entry) {
    return StringUtils.isNotBlank(entry) && entry.matches(validCharacterRegex);
  }

  public String validate(String entry) {
    if (!isValid(entry)) {
      throw new IllegalArgumentException(format("Entry '%s' is invalid. " + IDENTIFIER_REGEX_DEFINITION, entry, fieldUnderValidation, validCharacterRegex));
    }
    return entry;
  }

  public void validate(Collection<String> entries) {
    Set<String> invalidEntries = entries.stream()
      .filter(entry -> !isValid(entry))
      .collect(toSet());
    if (!invalidEntries.isEmpty()) {
      throw new IllegalArgumentException(format("Entries '%s' are invalid. " + IDENTIFIER_REGEX_DEFINITION, join(", ", invalidEntries), fieldUnderValidation, validCharacterRegex));
    }
  }
}
