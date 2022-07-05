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

import java.util.Collection;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.stream.Collectors.toSet;

/**
 * Validates strings based on the defined regex. The error message is based on the error message passed in parameter.
 */
public class StringPatternValidator {

  private final String validCharacterRegex;
  private final String errorMessageSuffix;

  public StringPatternValidator(String validCharacterRegex, String errorMessageSuffix) {
    this.validCharacterRegex = validCharacterRegex;
    this.errorMessageSuffix = errorMessageSuffix;
  }

  public boolean isValid(String entry) {
    return StringUtils.isNotBlank(entry) && entry.matches(validCharacterRegex);
  }

  public String validate(String entry) {
    if (!isValid(entry)) {
      throw new IllegalArgumentException(format("Entry '%s' is invalid. %s", entry, errorMessageSuffix));
    }
    return entry;
  }

  public void validate(Collection<String> entries) {
    Set<String> invalidEntries = entries.stream()
      .filter(entry -> !isValid(entry))
      .collect(toSet());
    if (!invalidEntries.isEmpty()) {
      throw new IllegalArgumentException(format("Entries '%s' are invalid. %s", join(", ", invalidEntries), errorMessageSuffix));
    }
  }
}
