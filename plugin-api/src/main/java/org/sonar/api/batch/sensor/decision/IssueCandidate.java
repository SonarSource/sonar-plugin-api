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
 * An issue candidate saved by a sensor through a {@link NewIssueCandidate}: the issue, the facts its decision needs, and whether the issue
 * may be reported without a decision.
 * <p>
 * This API is experimental and can be changed or dropped at any time.
 * </p>
 *
 * @since 14.3
 */
@Beta
public interface IssueCandidate {

  /**
   * The issue to report if the candidate is kept.
   */
  Issue issue();

  /**
   * The facts of the candidate, by name. Never null.
   */
  Map<String, String> facts();

  /**
   * {@code true} if the issue is reported only when a decision confirms it (a required candidate), {@code false} if it is reported when no
   * decision can be made (an optional candidate).
   */
  boolean requiresDecision();
}
