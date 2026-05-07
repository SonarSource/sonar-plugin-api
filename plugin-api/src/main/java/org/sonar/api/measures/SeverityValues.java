/*
 * Sonar Plugin API
 * Copyright (C) SonarSource Sàrl
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
package org.sonar.api.measures;

import org.sonar.api.issue.impact.Severity;

/**
 * Integer values that map to severity levels used by severity-based metrics.
 * Severities increase in numeric value; a higher value means a worse severity.
 *
 * <p>
 * When configuring a quality gate condition threshold in the UI, store the value
 * one less than the target level (e.g. {@code LOW - 1 = 9}) so that the
 * {@code GREATER_THAN} operator catches issues at or above that level.
 * </p>
 *
 * @since 13.6
 */
public final class SeverityValues {
  /** No issues present. */
  public static final int NO_ISSUES = 0;
  public static final int INFO = 5;
  public static final int LOW = 10;
  public static final int MEDIUM = 15;
  public static final int HIGH = 20;
  public static final int BLOCKER = 25;

  private SeverityValues() {
    // constants only
  }

  /**
   * Maps a rule severity string (see {@link org.sonar.api.rule.Severity}) to a {@link SeverityValues} integer.
   *
   * @throws IllegalArgumentException if the severity string is not recognised
   * @since 13.6
   */
  public static int fromRuleSeverity(String severity) {
    switch (severity) {
      case org.sonar.api.rule.Severity.INFO:
        return INFO;
      case org.sonar.api.rule.Severity.MINOR:
        return LOW;
      case org.sonar.api.rule.Severity.MAJOR:
        return MEDIUM;
      case org.sonar.api.rule.Severity.CRITICAL:
        return HIGH;
      case org.sonar.api.rule.Severity.BLOCKER:
        return BLOCKER;
      default:
        throw new IllegalArgumentException("Unknown rule severity: " + severity);
    }
  }

  /**
   * Maps impact severity (see {@link org.sonar.api.issue.impact.Severity}) to a {@link SeverityValues} integer.
   *
   * @since 13.6
   */
  public static int fromImpactSeverity(Severity severity) {
    switch (severity) {
      case INFO:
        return INFO;
      case LOW:
        return LOW;
      case MEDIUM:
        return MEDIUM;
      case HIGH:
        return HIGH;
      case BLOCKER:
        return BLOCKER;
      default:
        return 0;
    }
  }
}
