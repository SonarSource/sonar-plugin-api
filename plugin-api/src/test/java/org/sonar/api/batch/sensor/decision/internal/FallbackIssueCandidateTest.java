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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FallbackIssueCandidateTest {

  private static final RuleKey RULE = RuleKey.of("javascript", "S1234");

  @Test
  void overrides_every_new_issue_method() {
    List<String> notOverridden = Arrays.stream(NewIssue.class.getMethods())
      .filter(m -> !Modifier.isStatic(m.getModifiers()))
      .filter(m -> declaringClassOf(m).isInterface())
      .map(Method::toGenericString)
      .toList();

    assertThat(notOverridden).isEmpty();
  }

  @Test
  void chained_methods_return_the_candidate() throws Exception {
    for (boolean required : new boolean[] {false, true}) {
      var candidate = new FallbackIssueCandidate(mock(NewIssue.class, RETURNS_SELF), required);

      for (Method method : NewIssue.class.getMethods()) {
        if (method.getReturnType().equals(NewIssue.class)) {
          assertThat(method.invoke(candidate, defaultArguments(method))).as(method.getName() + ", required=" + required).isSameAs(candidate);
        }
      }
    }
  }

  @Test
  void optional_candidate_saves_the_wrapped_issue() {
    NewIssue issue = mock(NewIssue.class, RETURNS_SELF);
    NewIssueLocation location = mock(NewIssueLocation.class);

    new FallbackIssueCandidate(issue, false)
      .addFact("testName", "should add")
      .forRule(RULE)
      .at(location)
      .save();

    verify(issue).forRule(RULE);
    verify(issue).at(location);
    verify(issue).save();
  }

  @Test
  void required_candidate_saves_nothing() {
    NewIssue issue = mock(NewIssue.class, RETURNS_SELF);

    new FallbackIssueCandidate(issue, true)
      .addFact("testName", "should add")
      .forRule(RULE)
      .save();

    verify(issue, never()).save();
  }

  @Test
  void sensor_context_defaults_wrap_new_issue() {
    SensorContext context = mock(SensorContext.class, CALLS_REAL_METHODS);
    NewIssue optionalIssue = mock(NewIssue.class, RETURNS_SELF);
    NewIssue requiredIssue = mock(NewIssue.class, RETURNS_SELF);
    when(context.newIssue()).thenReturn(optionalIssue, requiredIssue);

    context.newOptionalDecisionCandidate().save();
    context.newRequiredDecisionCandidate().save();

    verify(optionalIssue).save();
    verify(requiredIssue, never()).save();
  }

  private static Class<?> declaringClassOf(Method method) {
    try {
      return FallbackIssueCandidate.class.getMethod(method.getName(), method.getParameterTypes()).getDeclaringClass();
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }

  private static Object[] defaultArguments(Method method) {
    return Arrays.stream(method.getParameterTypes())
      .map(type -> type.equals(boolean.class) ? Boolean.FALSE : null)
      .toArray();
  }
}
