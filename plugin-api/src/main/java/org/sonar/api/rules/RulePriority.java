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
package org.sonar.api.rules;

import org.sonar.check.Priority;

/**
 * @deprecated since 4.2
 * @see org.sonar.api.rule.Severity
 */
@Deprecated
public enum RulePriority {

  /**
   * WARNING : DO NOT CHANGE THE ENUMERATION ORDER
   * the enum ordinal is used for db persistence
   */
  INFO, MINOR, MAJOR, CRITICAL, BLOCKER;

  private static final String UNKNOWN_PRIORITY = "Unknown priority ";

  public static RulePriority fromCheckPriority(Priority checkPriority) {
    if (checkPriority == Priority.BLOCKER) {
      return RulePriority.BLOCKER;
    }
    if (checkPriority == Priority.CRITICAL) {
      return RulePriority.CRITICAL;
    }
    if (checkPriority == Priority.MAJOR) {
      return RulePriority.MAJOR;
    }
    if (checkPriority == Priority.MINOR) {
      return RulePriority.MINOR;
    }
    if (checkPriority == Priority.INFO) {
      return RulePriority.INFO;
    }
    throw new IllegalArgumentException(UNKNOWN_PRIORITY + checkPriority);
  }

  public static RulePriority valueOfInt(int ordinal) {
    return RulePriority.values()[ordinal];
  }
}
