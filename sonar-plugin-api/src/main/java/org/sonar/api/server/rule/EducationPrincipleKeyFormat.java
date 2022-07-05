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

/**
 * The characters allowed for the education principle keys are limited by the following regex pattern: {@value EducationPrincipleKeyFormat#VALID_CHARACTERS_REGEX}
 */
public class EducationPrincipleKeyFormat {

  public static final String ERROR_MESSAGE_SUFFIX = "Education principle keys accept only the characters: a-z, 0-9, '_'";

  private static final String VALID_CHARACTERS_REGEX = "^[a-z0-9_]+$";

  private static final StringPatternValidator STRING_VALIDATOR = new StringPatternValidator(VALID_CHARACTERS_REGEX, ERROR_MESSAGE_SUFFIX);

  private EducationPrincipleKeyFormat() {
    // only static methods
  }

  public static void validate(String educationPrincipleKey) {
    STRING_VALIDATOR.validate(educationPrincipleKey);
  }

}
