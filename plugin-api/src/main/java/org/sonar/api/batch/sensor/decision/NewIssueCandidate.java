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

import java.util.Map;
import org.sonar.api.Beta;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;

/**
 * A {@link NewIssue} with the facts its decision needs. It becomes a reported issue only through a decision, or through the fallback of
 * its kind when no decision is available: an {@link SensorContext#newOptionalDecisionCandidate(NewIssue) optional} candidate is reported,
 * a {@link SensorContext#newRequiredDecisionCandidate(NewIssue) required} one is dropped.
 * <p>
 * Once wrapped, the issue is saved only through {@link #save()}: do not save it directly.
 * <p>
 * Facts are sent to the decision model as untrusted data. They stay in memory: they are never written to the report or to the logs.
 * <p>
 * This API is experimental and can be changed or dropped at any time.
 *
 * @since 14.4
 */
@Beta
public interface NewIssueCandidate {

  /**
   * Adds or replaces an actual fact. Its name must be declared in the decision specification of the rule.
   *
   * @throws IllegalArgumentException if the name is blank
   * @throws NullPointerException if the value is null
   */
  NewIssueCandidate addFact(String name, String value);

  /**
   * Replaces all the facts.
   *
   * @throws IllegalArgumentException if a name is blank
   * @throws NullPointerException if a value is null
   */
  NewIssueCandidate setFacts(Map<String, String> facts);

  /**
   * Saves the candidate. The wrapped issue must be complete, as for {@link NewIssue#save()}.
   */
  void save();
}
