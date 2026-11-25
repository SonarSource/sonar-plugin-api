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
 * decision needs. Use {@link org.sonar.api.batch.sensor.SensorContext#newOptionalDecisionCandidate()} or
 * {@link org.sonar.api.batch.sensor.SensorContext#newRequiredDecisionCandidate()} to create one.
 * <p>
 * Add the facts first, then call {@link #issue()} and build and save the issue as any issue. Saving the issue saves the candidate: what
 * happens to it then depends on the engine. Engines that do not support decisions report an optional candidate as an ordinary issue and
 * drop a required one. In both cases the facts are discarded.
 * </p>
 * This API is experimental and can be changed or dropped at any time.
 *
 * @since 14.3
 */
@Beta
public interface NewIssueCandidate {

  /**
   * Add a fact for the decision. A fact added again with the same name replaces the previous value.
   *
   * @param name the name of the fact, as declared in the decision specification of the rule
   * @param value the value of the fact
   * @throws IllegalStateException if {@link #issue()} was already called
   */
  NewIssueCandidate addFact(String name, String value);

  /**
   * The issue of the candidate. Build it and call {@link NewIssue#save()} as for any issue. Calling this method again returns the same
   * issue.
   */
  NewIssue issue();
}
