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
package org.sonar.api.batch.sensor.decision.internal;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class FallbackIssueCandidateTest {

  @Test
  void optional_candidate_saves_the_wrapped_issue() {
    NewIssue issue = mock(NewIssue.class);

    new FallbackIssueCandidate(issue, false)
      .addFact("testName", "should add")
      .setFacts(Map.of("testName", "should add"))
      .save();

    verify(issue).save();
    verifyNoMoreInteractions(issue);
  }

  @Test
  void required_candidate_saves_nothing() {
    NewIssue issue = mock(NewIssue.class);

    new FallbackIssueCandidate(issue, true)
      .addFact("testName", "should add")
      .save();

    verify(issue, never()).save();
  }

  @Test
  void sensor_context_defaults_wrap_the_issue() {
    SensorContext context = mock(SensorContext.class, CALLS_REAL_METHODS);
    NewIssue optionalIssue = mock(NewIssue.class);
    NewIssue requiredIssue = mock(NewIssue.class);

    context.newOptionalDecisionCandidate(optionalIssue).save();
    context.newRequiredDecisionCandidate(requiredIssue).save();

    verify(optionalIssue).save();
    verify(requiredIssue, never()).save();
  }
}
