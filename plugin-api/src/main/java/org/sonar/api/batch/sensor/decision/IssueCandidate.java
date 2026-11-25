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
import org.sonar.api.batch.sensor.issue.Issue;

/**
 * Read view of a saved {@link NewIssueCandidate}, for the engine and the test fixtures.
 * <p>
 * This API is experimental and can be changed or dropped at any time.
 *
 * @since 14.4
 */
@Beta
public interface IssueCandidate {

  /**
   * @return the wrapped issue
   */
  Issue issue();

  /**
   * @return the actual facts, immutable
   */
  Map<String, String> getFacts();

  /**
   * @return {@code true} for a required candidate, dropped without a decision; {@code false} for an optional one, reported without a decision
   */
  boolean requiresDecision();
}
