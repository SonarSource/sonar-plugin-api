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
package org.sonar.api.resources;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.sonar.api.measures.Metric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.sonar.api.measures.CoreMetrics.ACCEPTED_ISSUES;
import static org.sonar.api.measures.CoreMetrics.DEFERRED_ISSUES;
import static org.sonar.api.measures.CoreMetrics.DEFERRED_ISSUES_KEY;
import static org.sonar.api.measures.CoreMetrics.DOMAIN_ISSUES;
import static org.sonar.api.measures.CoreMetrics.FILES;
import static org.sonar.api.measures.CoreMetrics.NCLOC;
import static org.sonar.api.measures.CoreMetrics.NEW_DEFERRED_ISSUES;
import static org.sonar.api.measures.CoreMetrics.NEW_DEFERRED_ISSUES_KEY;
import static org.sonar.api.measures.CoreMetrics.NEW_NCLOC;
import static org.sonar.api.measures.CoreMetrics.getMetric;
import static org.sonar.api.measures.CoreMetrics.getMetrics;

public class CoreMetricsTest {

  @Test
  public void read_metrics_from_class_reflection() {
    List<Metric> metrics = getMetrics();
    assertThat(metrics.size()).isGreaterThan(100);
    assertThat(metrics).contains(NCLOC, NEW_NCLOC, FILES, ACCEPTED_ISSUES, DEFERRED_ISSUES, NEW_DEFERRED_ISSUES);
  }

  @Test
  public void new_ncloc_is_registered() {
    assertThat(getMetric("new_ncloc")).isEqualTo(NEW_NCLOC);
  }

  @Test
  public void deferred_metrics_are_registered_with_issue_count_properties() {
    assertThat(DEFERRED_ISSUES_KEY).isEqualTo("deferred_issues");
    assertThat(NEW_DEFERRED_ISSUES_KEY).isEqualTo("new_deferred_issues");
    assertThat(getMetric(DEFERRED_ISSUES_KEY)).isSameAs(DEFERRED_ISSUES);
    assertThat(getMetric(NEW_DEFERRED_ISSUES_KEY)).isSameAs(NEW_DEFERRED_ISSUES);
    assertThat(DEFERRED_ISSUES.getName()).isEqualTo("Deferred Issues");
    assertThat(NEW_DEFERRED_ISSUES.getName()).isEqualTo("New Deferred Issues");
    for (Metric<Integer> metric : List.of(DEFERRED_ISSUES, NEW_DEFERRED_ISSUES)) {
      assertThat(metric.getType()).isEqualTo(Metric.ValueType.INT);
      assertThat(metric.getDirection()).isEqualTo(Metric.DIRECTION_WORST);
      assertThat(metric.getDomain()).isEqualTo(DOMAIN_ISSUES);
      assertThat(metric.getBestValue()).isZero();
      assertThat(metric.isOptimizedBestValue()).isTrue();
    }
    assertThat(DEFERRED_ISSUES.getDeleteHistoricalData()).isFalse();
    assertThat(NEW_DEFERRED_ISSUES.getDeleteHistoricalData()).isTrue();
  }

  @Test
  public void get_metric_by_key() {
    Metric metric = getMetric("ncloc");
    assertThat(metric.getKey()).isEqualTo("ncloc");
  }

  @Test
  public void fail_get_unknown_metric_by_key() {
    assertThatThrownBy(() -> getMetric("unknown"))
      .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  public void someMetricsAreMeantToBeHidden() {
    Metric metric = getMetric("analysis_from_sonarqube_9_4");
    assertThat(metric.isHidden()).isTrue();
  }
}
