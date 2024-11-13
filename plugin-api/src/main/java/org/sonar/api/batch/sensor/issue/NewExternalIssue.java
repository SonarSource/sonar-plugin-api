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
import org.sonar.api.Beta;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.CleanCodeAttribute;
import org.sonar.api.rules.RuleType;

/**
 * Builder for an issue imported from an external rule engine by a {@link Sensor}.
 * Don't forget to {@link #save()} after setting the fields.
 * 
 * @since 7.2 
 */
public interface NewExternalIssue {
  /**
   * The {@link RuleKey} of the issue.
   * @deprecated since 7.4. It is misleading, because of the "external_" prefix that is added on server side. Use {@link #engineId(String)} and {@link #ruleId(String)}
   */
  @Deprecated
  NewExternalIssue forRule(RuleKey ruleKey);

  /**
   * Unique identifier of the external analyzer (e.g. eslint, pmd, ...)
   * @since 7.4
   */
  NewExternalIssue engineId(String engineId);

  /**
   * Unique rule identifier for a given {@link #engineId(String)}
   * @since 7.4
   */
  NewExternalIssue ruleId(String ruleId);

  /**
   * Type of issue.
   */
  NewExternalIssue type(RuleType type);

  /**
   * Attribute of the issue according to Clean Code Taxonomy.
   * Providing it is optional for now but will become mandatory in the future.
   * This method is experimental and might change in the future.
   * @since 10.1
   */
  @Beta
  NewExternalIssue cleanCodeAttribute(CleanCodeAttribute attribute);

  /**
   * Effort to fix the issue, in minutes.
   */
  NewExternalIssue remediationEffortMinutes(@Nullable Long effortInMinutes);

  /**
   * Set the severity of the issue.
   */
  NewExternalIssue severity(Severity severity);

  /**
   * Add a new impact or override the severity of an impact already defined by the rule.
   * It is only possible to define a default impact for a given {@link SoftwareQuality}
   * This method is experimental and might change in the future.
   * @since 10.1
   */
  @Beta
  NewExternalIssue addImpact(SoftwareQuality softwareQuality, org.sonar.api.issue.impact.Severity severity);

  /**
   * Primary location for this issue.
   */
  NewExternalIssue at(NewIssueLocation primaryLocation);

  /**
   * Add a secondary location for this issue. Several secondary locations can be registered.
   */
  NewExternalIssue addLocation(NewIssueLocation secondaryLocation);

  /**
   * Register a flow for this issue. A flow is an ordered list of issue locations that help to understand the issue.
   * It should be a <b>path that backtracks the issue from its primary location to the start of the flow</b>. 
   * Several flows can be registered.
   */
  NewExternalIssue addFlow(Iterable<NewIssueLocation> flowLocations);

  /**
   * Create a new location for this issue. First registered location is considered as primary location.
   */
  NewIssueLocation newLocation();

  /**
   * Save the issue. If rule key is unknown or rule not enabled in the current quality profile then a warning is logged but no exception
   * is thrown.
   */
  void save();

}
