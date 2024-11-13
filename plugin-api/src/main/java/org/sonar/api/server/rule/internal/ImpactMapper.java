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
package org.sonar.api.server.rule.internal;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.sonar.api.issue.impact.Severity;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rules.RuleType;

import static org.sonar.api.rule.Severity.BLOCKER;
import static org.sonar.api.rule.Severity.CRITICAL;
import static org.sonar.api.rule.Severity.INFO;
import static org.sonar.api.rule.Severity.MAJOR;
import static org.sonar.api.rule.Severity.MINOR;

public class ImpactMapper {

  static final List<SoftwareQuality> ORDERED_SOFTWARE_QUALITIES = List.of(SoftwareQuality.SECURITY,
    SoftwareQuality.RELIABILITY, SoftwareQuality.MAINTAINABILITY);

  private ImpactMapper() {
    // This class is designed to be static
  }

  public static SoftwareQuality convertToSoftwareQuality(RuleType ruleType) {
    switch (ruleType) {
      case CODE_SMELL:
        return SoftwareQuality.MAINTAINABILITY;
      case BUG:
        return SoftwareQuality.RELIABILITY;
      case VULNERABILITY:
        return SoftwareQuality.SECURITY;
      case SECURITY_HOTSPOT:
        throw new IllegalStateException("Can not map Security Hotspot to Software Quality");
      default:
        throw new IllegalStateException("Unknown rule type");
    }
  }

  public static RuleType convertToRuleType(SoftwareQuality softwareQuality) {
    switch (softwareQuality) {
      case MAINTAINABILITY:
        return RuleType.CODE_SMELL;
      case RELIABILITY:
        return RuleType.BUG;
      case SECURITY:
        return RuleType.VULNERABILITY;
      default:
        throw new IllegalStateException("Unknown software quality");
    }
  }

  public static String convertToRuleSeverity(Severity severity) {
    switch (severity) {
      case BLOCKER:
        return BLOCKER;
      case HIGH:
        return CRITICAL;
      case MEDIUM:
        return MAJOR;
      case LOW:
        return MINOR;
      case INFO:
        return INFO;
      default:
        throw new IllegalStateException("This severity value " + severity + " is illegal.");
    }
  }

  /**
   * @deprecated since 10.14, use {@link ImpactMapper#convertToRuleSeverity(Severity)} instead.
   */
  @Deprecated(since = "10.14")
  public static String convertToDeprecatedSeverity(Severity severity) {
    return ImpactMapper.convertToRuleSeverity(severity);
  }

  public static Severity convertToImpactSeverity(String ruleSeverity) {
    switch (ruleSeverity) {
      case BLOCKER:
      case CRITICAL:
        return Severity.HIGH;
      case MAJOR:
        return Severity.MEDIUM;
      case MINOR:
      case INFO:
        return Severity.LOW;
      default:
        throw new IllegalStateException("This severity value " + ruleSeverity + " is illegal.");
    }
  }

  /**
   * This method is needed for picking the best impact on which we are going to base backmapping (getting type and severity from impact).
   * As Impacts like any ordering (there is no "best" impact, all of them are equal) we just need to ensure that our choice is consistent
   * to make sure we always pick the same impact when the list (map) of impacts is the same.
   */
  public static Map.Entry<SoftwareQuality, Severity> getBestImpactForBackmapping(Map<SoftwareQuality, Severity> impacts) {
    return impacts.entrySet()
      .stream().min(Comparator.comparing(i -> ORDERED_SOFTWARE_QUALITIES.indexOf(i.getKey())))
      .orElseThrow(() -> new IllegalArgumentException("There is no impact to choose from."));
  }
}
