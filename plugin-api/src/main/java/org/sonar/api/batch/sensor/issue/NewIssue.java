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

import javax.annotation.Nullable;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.issue.fix.NewQuickFix;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rule.RuleKey;

/**
 * Represents an issue detected by a {@link Sensor}.
 *
 * @since 5.1
 */
public interface NewIssue {

  /**
   * The type of the flow reported for a given issue. Issue may have many flows reported with different types.
   * @since 9.11
   */
  enum FlowType {
    UNDEFINED, DATA, EXECUTION
  }

  /**
   * The {@link RuleKey} of the issue.
   */
  NewIssue forRule(RuleKey ruleKey);

  /**
   * Gap used for the computation of the effort. 
   * @since 5.5
   */
  NewIssue gap(@Nullable Double gap);

  /**
   * Override severity of the issue.
   * Setting a null value or not calling this method means to use severity configured in quality profile.
   */
  NewIssue overrideSeverity(@Nullable Severity severity);

  /**
   * Override severity of an impact defined by the rule.
   * The impact for the SoftwareQuality must have already been defined by the rule.
   * @since 10.1
   */
  NewIssue overrideImpact(SoftwareQuality softwareQuality, org.sonar.api.issue.impact.Severity severity);

  /**
   * Primary location for this issue.
   * @since 5.2
   */
  NewIssue at(NewIssueLocation primaryLocation);

  /**
   * Add a secondary location for this issue. Several secondary locations can be registered.
   * @since 5.2
   */
  NewIssue addLocation(NewIssueLocation secondaryLocation);

  /**
   * Register if a QuickFix would be available on SonarLint for this issue.
   * @since 9.2
   */
  NewIssue setQuickFixAvailable(boolean quickFixAvailable);

  /**
   * Register a flow for this issue. A flow is an ordered list of issue locations that help to understand the issue.
   * It should be a <b>path that backtracks the issue from its primary location to the start of the flow</b>. 
   * Several flows can be registered. The type of the flow will be undefined.
   * @since 5.2
   */
  NewIssue addFlow(Iterable<NewIssueLocation> flowLocations);

  /**
   * Register a flow for this issue. A flow is an ordered list of issue locations that help to understand the issue.
   * It should be a <b>path that backtracks the issue from its primary location to the start of the flow</b>.
   * Several flows can be registered.
   *
   * @param flowType specifies type of flow that is being added. For details see
   * {@link FlowType FlowType}.
   * @param flowDescription optional description associated with a flow.
   * @since 9.11
   */
  NewIssue addFlow(Iterable<NewIssueLocation> flowLocations, FlowType flowType, @Nullable String flowDescription);

  /**
   * Create a new location for this issue. First registered location is considered as primary location.
   *
   * @since 5.2
   */
  NewIssueLocation newLocation();

  /**
   * Create a new quick fix. Use {@link #addQuickFix(NewQuickFix)} to finish registering the new quick fix.
   *
   * @return a new uninitialized instance of a quick fix for this issue
   * @since 9.13
   */
  NewQuickFix newQuickFix();

  /**
   * Add a new quick fix to this issue. While products should do their best to display quick fixes in the order they are contributed,
   * sometimes due to external factors the quick fixes might be displayed in a different order.
   *
   * @param newQuickFix the quick fix to add
   * @return this object
   * @since 9.13
   */
  NewIssue addQuickFix(NewQuickFix newQuickFix);

  /**
   * Save the issue. If rule key is unknown or rule not enabled in the current quality profile then a warning is logged but no exception
   * is thrown.
   */
  void save();

  /**
   * Set the optional rule description section context key.
   * The key needs to match the one present in {@link org.sonar.api.server.rule.Context#getKey()}.
   * @since 9.8
   */
  NewIssue setRuleDescriptionContextKey(@Nullable String ruleDescriptionContextKey);

  /**
   * Registers a list of code variants for this issue.
   * In C and C++, it is commonplace to have multiple code variants. Two source codes are defined here as variants of each other if there
   * is any difference in their preprocessed source code.
   * @since 9.17
   */
  NewIssue setCodeVariants(@Nullable Iterable<String> codeVariants);

}
