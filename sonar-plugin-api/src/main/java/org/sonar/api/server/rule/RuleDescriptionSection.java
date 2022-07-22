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

import java.util.Optional;

/**
 * Represents a sub-section of a rule description (What's the risk, Assess the risk, etc.)
 *
 * @since 9.6
 */
public interface RuleDescriptionSection {

  /**
   * This class is a placeholder for the supported rule description section key constants.
   *
   * @since 9.6
   */
  class RuleDescriptionSectionKeys {
    public static final String INTRODUCTION_SECTION_KEY = "introduction";
    public static final String ROOT_CAUSE_SECTION_KEY = "root_cause";
    public static final String ASSESS_THE_PROBLEM_SECTION_KEY = "assess_the_problem";
    public static final String HOW_TO_FIX_SECTION_KEY = "how_to_fix";
    public static final String RESOURCES_SECTION_KEY = "resources";

    private RuleDescriptionSectionKeys() {
      throw new IllegalStateException("this class only serves as a placeholder for section key constants, it must not be instantiated.");
    }
  }

  String getKey();

  String getHtmlContent();

  /**
   * @since 9.7
   */
  default Optional<Context> getContext() {
    return Optional.empty();
  }

  static RuleDescriptionSectionBuilder builder() {
    return new RuleDescriptionSectionBuilder();
  }

}
