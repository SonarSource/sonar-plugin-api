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

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Locale.ENGLISH;
import static java.util.stream.Collectors.toSet;

/**
 * The characters allowed in rule tags are the same as those on StackOverflow, basically lower-case
 * letters, digits, plus (+), sharp (#), dash (-) and dot (.)
 * See http://meta.stackoverflow.com/questions/22624/what-symbols-characters-are-not-allowed-in-tags
 * @since 4.2
 */
public class RuleTagFormat {

  private static final String VALID_CHARACTERS_REGEX = "^[a-z0-9\\+#\\-\\.]+$";

  private static final StringPatternValidator STRING_VALIDATOR = new StringPatternValidator("Rule tags", VALID_CHARACTERS_REGEX);

  private RuleTagFormat() {
    // only static methods
  }

  public static boolean isValid(String tag) {
    return STRING_VALIDATOR.isValid(tag);
  }

  public static String validate(String tag) {
    return STRING_VALIDATOR.validate(tag);
  }

  public static Set<String> validate(Collection<String> tags) {
    Set<String> sanitizedTags = tags.stream()
      .filter(Objects::nonNull)
      .filter(entry -> !entry.isEmpty())
      .map(entry -> entry.toLowerCase(ENGLISH))
      .collect(toSet());
    STRING_VALIDATOR.validate(sanitizedTags);
    return sanitizedTags;
  }

}
