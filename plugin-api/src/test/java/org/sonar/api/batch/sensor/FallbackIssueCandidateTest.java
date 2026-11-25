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
package org.sonar.api.batch.sensor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FallbackIssueCandidateTest {

  private final NewIssue issue = mock(NewIssue.class);

  @Test
  void optional_shouldDiscardTheFacts_andReturnTheIssue() {
    var underTest = FallbackIssueCandidate.optional(issue);

    assertThat(underTest.addFact("name", "value")).isSameAs(underTest);
    assertThat(underTest.issue()).isSameAs(issue);
  }

  @Test
  void required_shouldReturnAnIssueThatIsNeverSaved() {
    var underTest = FallbackIssueCandidate.required(issue);

    underTest.addFact("name", "value").issue().forRule(RuleKey.of("repo", "rule")).save();

    verify(issue).forRule(RuleKey.of("repo", "rule"));
    verify(issue, never()).save();
  }

  @Test
  void droppedIssue_shouldCreateLocationsWithTheRealIssue() {
    NewIssueLocation location = mock(NewIssueLocation.class);
    when(issue.newLocation()).thenReturn(location);

    assertThat(new FallbackIssueCandidate.DroppedIssue(issue).newLocation()).isSameAs(location);
  }

  @Test
  void droppedIssue_shouldForwardEveryBuilderMethod_andReturnItself() throws Exception {
    for (Method method : builderMethodsOfNewIssue()) {
      NewIssue wrapped = mock(NewIssue.class);
      var underTest = new FallbackIssueCandidate.DroppedIssue(wrapped);

      Object result = method.invoke(underTest, defaultArguments(method));

      assertThat(mockingDetails(wrapped).getInvocations()).as(method.toString())
        .anyMatch(invocation -> invocation.getMethod().equals(method));
      if (method.getReturnType() == NewIssue.class) {
        assertThat(result).as(method.toString()).isSameAs(underTest);
      }
    }
  }

  private static List<Method> builderMethodsOfNewIssue() {
    return Arrays.stream(NewIssue.class.getMethods())
      .filter(m -> !Modifier.isStatic(m.getModifiers()))
      .filter(m -> !m.getName().equals("save"))
      .toList();
  }

  private static Object[] defaultArguments(Method method) {
    return Arrays.stream(method.getParameterTypes())
      .map(type -> type == boolean.class ? (Object) false : null)
      .toArray();
  }
}
