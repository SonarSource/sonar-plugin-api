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
package org.sonar.api.batch.sensor.decision;

import org.sonar.api.Beta;
import org.sonar.api.batch.sensor.issue.NewIssue;

/**
 * Builder for an issue candidate: an issue whose reporting depends on a decision made after the analysis, together with the facts that
 * decision needs. Created by {@link org.sonar.api.batch.sensor.SensorContext#newOptionalDecisionCandidate()} or
 * {@link org.sonar.api.batch.sensor.SensorContext#newRequiredDecisionCandidate()}.
 * <p>
 * Add the facts first, then build and save {@link #issue()} as any issue. Saving the issue saves the candidate.
 * </p>
 * This API is experimental and can be changed or dropped at any time.
 *
 * @since 14.4
 */
@Beta
public interface NewIssueCandidate {

  /**
   * Adds a fact for the decision. Adding a fact with the same name again replaces its value.
   *
   * @param name the name of the fact, as declared in the decision specification of the rule
   * @param value the value of the fact
   * @throws IllegalStateException if {@link #issue()} was already called
   */
  NewIssueCandidate addFact(String name, String value);

  /**
   * The issue of the candidate, to build and save as any issue. Always returns the same instance.
   */
  NewIssue issue();
}
