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
package org.sonar.api.testfixtures.measure;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Issue;
import org.sonar.api.ce.measure.Settings;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.utils.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.sonar.api.ce.measure.MeasureComputer.MeasureComputerDefinition;
import static org.sonar.api.issue.impact.Severity.HIGH;

class TestMeasureComputerContextTest {

  static final String INPUT_METRIC = "INPUT_METRIC";
  static final String OUTPUT_METRIC = "OUTPUT_METRIC";

  final static Component PROJECT = new TestComponent("Project", Component.Type.PROJECT, null);

  final static MeasureComputerDefinition DEFINITION = new TestMeasureComputerDefinition.MeasureComputerDefinitionBuilderImpl()
    .setInputMetrics(INPUT_METRIC)
    .setOutputMetrics(OUTPUT_METRIC)
    .build();

  Settings settings = new TestSettings();

  TestMeasureComputerContext underTest = new TestMeasureComputerContext(PROJECT, settings, DEFINITION);

  @Test
  void get_component() {
    assertThat(underTest.getComponent()).isEqualTo(PROJECT);
  }

  @Test
  void get_settings() {
    assertThat(underTest.getSettings()).isEqualTo(settings);
  }

  @Test
  void get_int_measure() {
    underTest.addInputMeasure(INPUT_METRIC, 10);

    assertThat(underTest.getMeasure(INPUT_METRIC).getIntValue()).isEqualTo(10);
  }

  @Test
  void get_double_measure() {
    underTest.addInputMeasure(INPUT_METRIC, 10d);

    assertThat(underTest.getMeasure(INPUT_METRIC).getDoubleValue()).isEqualTo(10d);
  }

  @Test
  void get_long_measure() {
    underTest.addInputMeasure(INPUT_METRIC, 10L);

    assertThat(underTest.getMeasure(INPUT_METRIC).getLongValue()).isEqualTo(10L);
  }

  @Test
  void get_string_measure() {
    underTest.addInputMeasure(INPUT_METRIC, "text");

    assertThat(underTest.getMeasure(INPUT_METRIC).getStringValue()).isEqualTo("text");
  }

  @Test
  void get_boolean_measure() {
    underTest.addInputMeasure(INPUT_METRIC, true);

    assertThat(underTest.getMeasure(INPUT_METRIC).getBooleanValue()).isTrue();
  }

  @Test
  void fail_with_IAE_when_trying_to_get_measure_on_unknown_metric() {
    assertThatThrownBy(() -> underTest.getMeasure("unknown"))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Only metrics in [INPUT_METRIC] can be used to load measures");
  }

  @Test
  void get_int_children_measures() {
    underTest.addChildrenMeasures(INPUT_METRIC, 10, 20);

    assertThat(underTest.getChildrenMeasures(INPUT_METRIC)).hasSize(2);
  }

  @Test
  void get_doublet_children_measures() {
    underTest.addChildrenMeasures(INPUT_METRIC, 10d, 20d);

    assertThat(underTest.getChildrenMeasures(INPUT_METRIC)).hasSize(2);
  }

  @Test
  void get_long_children_measures() {
    underTest.addChildrenMeasures(INPUT_METRIC, 10L, 20L);

    assertThat(underTest.getChildrenMeasures(INPUT_METRIC)).hasSize(2);
  }

  @Test
  void get_string_children_measures() {
    underTest.addChildrenMeasures(INPUT_METRIC, "value1", "value2");

    assertThat(underTest.getChildrenMeasures(INPUT_METRIC)).hasSize(2);
  }

  @Test
  void fail_with_IAE_when_trying_to_get_children_measures_on_unknown_metric() {
    assertThatThrownBy(() -> underTest.getChildrenMeasures("unknown"))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Only metrics in [INPUT_METRIC] can be used to load measures");
  }

  @Test
  void add_int_measure() {
    underTest.addMeasure(OUTPUT_METRIC, 10);

    assertThat(underTest.getMeasure(OUTPUT_METRIC).getIntValue()).isEqualTo(10);
  }

  @Test
  void add_double_measure() {
    underTest.addMeasure(OUTPUT_METRIC, 10d);

    assertThat(underTest.getMeasure(OUTPUT_METRIC).getDoubleValue()).isEqualTo(10d);
  }

  @Test
  void add_long_measure() {
    underTest.addMeasure(OUTPUT_METRIC, 10L);

    assertThat(underTest.getMeasure(OUTPUT_METRIC).getLongValue()).isEqualTo(10L);
  }

  @Test
  void add_string_measure() {
    underTest.addMeasure(OUTPUT_METRIC, "text");

    assertThat(underTest.getMeasure(OUTPUT_METRIC).getStringValue()).isEqualTo("text");
  }

  @Test
  void fail_with_IAE_when_trying_to_add_measure_on_unknown_metric() {
    assertThatThrownBy(() -> underTest.addMeasure("unknown", 10))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Only metrics in [OUTPUT_METRIC] can be used to add measures. Metric 'unknown' is not allowed.");
  }

  @Test
  void fail_with_IAE_when_trying_to_add_measure_on_input_metric() {
    assertThatThrownBy(() -> underTest.addMeasure(INPUT_METRIC, 10))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Only metrics in [OUTPUT_METRIC] can be used to add measures. Metric 'INPUT_METRIC' is not allowed.");
  }

  @Test
  void fail_with_UOE_when_trying_to_add_same_measures_twice() {
    underTest.addMeasure(OUTPUT_METRIC, 10);

    assertThatThrownBy(() -> underTest.addMeasure(OUTPUT_METRIC, 20))
      .isInstanceOf(UnsupportedOperationException.class)
      .hasMessage("A measure on metric 'OUTPUT_METRIC' already exists");
  }

  @Test
  void get_issues() {
    Issue issue = new TestIssue.Builder()
      .setKey("ABCD")
      .setRuleKey(RuleKey.of("xoo", "S01"))
      .setSeverity(Severity.BLOCKER)
      .setStatus(org.sonar.api.issue.Issue.STATUS_RESOLVED)
      .setResolution(org.sonar.api.issue.Issue.RESOLUTION_FIXED)
      .setEffort(Duration.create(10L))
      .setType(RuleType.BUG)
      .addImpact(SoftwareQuality.MAINTAINABILITY, HIGH)
      .build();
    underTest.setIssues(Arrays.asList(issue));

    assertThat(underTest.getIssues()).hasSize(1);
  }
}
