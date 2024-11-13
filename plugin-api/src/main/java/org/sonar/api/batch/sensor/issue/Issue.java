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
package org.sonar.api.batch.sensor.issue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.CheckForNull;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.issue.NewIssue.FlowType;
import org.sonar.api.batch.sensor.issue.fix.QuickFix;
import org.sonar.api.issue.impact.SoftwareQuality;

/**
 * Represents an issue detected by a {@link Sensor}.
 *
 * @since 5.1
 */
public interface Issue extends IIssue {
  interface Flow {
    /**
     * @return Ordered list of locations for the execution flow
     */
    List<IssueLocation> locations();

    /**
     * @since 9.11
     * @return Flow description. Can be null if it's not specified.
     */
    @CheckForNull
    String description();

    /**
     * @since 9.11
     * @return Type of the flow
     */
    FlowType type();
  }

  /**
   * Gap used to compute the effort for fixing the issue.
   * @since 5.5
   */
  @CheckForNull
  Double gap();

  /**
   * Overridden severity.
   */
  @CheckForNull
  Severity overriddenSeverity();

  /**
   * Retrieve the overriden impacts for this issue.
   * @since 10.1
   */
  Map<SoftwareQuality, org.sonar.api.issue.impact.Severity> overridenImpacts();

  /**
   * Primary locations for this issue.
   * @since 5.2
   */
  @Override
  IssueLocation primaryLocation();

  /**
   * List of flows for this issue. Can be empty.
   * @since 5.2
   */
  @Override
  List<Flow> flows();

  /**
   * Is there a QuickFix available in SonarLint for this issue
   * @since 9.2
   */
  boolean isQuickFixAvailable();

  /**
   * The optional rule description section context key, in case the analyzer detects a context for the issue.
   * The key will match the one present in {@link org.sonar.api.server.rule.Context#getKey()}.
   * @since 9.8
   */
  Optional<String> ruleDescriptionContextKey();

  /**
   * The list of quick fixes contributed for this issue. Could be empty but never null.
   * @since 9.13
   */
  List<QuickFix> quickFixes();

  /**
   * @since 9.17
   */
  @CheckForNull
  List<String> codeVariants();
}
