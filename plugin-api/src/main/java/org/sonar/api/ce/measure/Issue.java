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
package org.sonar.api.ce.measure;

import java.util.Map;
import javax.annotation.CheckForNull;
import org.sonar.api.issue.IssueStatus;
import org.sonar.api.issue.impact.Severity;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.RuleType;
import org.sonar.api.utils.Duration;

/**
 * Issue that can be used in a {@link MeasureComputer}
 *
 * @since 5.2
 */
public interface Issue {

  String key();

  RuleKey ruleKey();

  /**
   * Available list of status can be found in {@link org.sonar.api.issue.Issue#STATUSES}
   *
   * @deprecated since 10.4 in favor of {@link Issue#issueStatus()}
   */
  @Deprecated(since = "10.4")
  String status();

  /**
   * Available list of resolutions can be found in {@link org.sonar.api.issue.Issue#RESOLUTIONS}
   *
   * @deprecated since 10.4 in favor of {@link Issue#issueStatus()}
   */
  @CheckForNull
  @Deprecated(since = "10.4")
  String resolution();

  /**
   * @since 10.4
   * Available list of status can be found in {@link IssueStatus#values()}
   */
  IssueStatus issueStatus();

  /**
   * See constants in {@link org.sonar.api.rule.Severity}.
   */
  String severity();

  /**
   * @since 5.5
   */
  @CheckForNull
  Duration effort();

  /**
   * @since 5.5
   */
  RuleType type();

  /**
   * @since 10.1
   */
  Map<SoftwareQuality, Severity> impacts();

}
