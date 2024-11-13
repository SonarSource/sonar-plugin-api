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
package org.sonar.api.server.rule.internal;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.sonar.api.issue.impact.Severity;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rules.RuleType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.sonar.api.issue.impact.Severity.BLOCKER;
import static org.sonar.api.issue.impact.Severity.HIGH;
import static org.sonar.api.issue.impact.Severity.INFO;
import static org.sonar.api.issue.impact.Severity.LOW;
import static org.sonar.api.issue.impact.Severity.MEDIUM;

public class ImpactMapperTest {

  @Test
  public void convertToSoftwareQuality_givenCodeSmell_returnMaintainability() {
    SoftwareQuality softwareQuality = ImpactMapper.convertToSoftwareQuality(RuleType.CODE_SMELL);

    assertThat(softwareQuality).isEqualTo(SoftwareQuality.MAINTAINABILITY);
  }

  @Test
  public void convertToSoftwareQuality_givenBug_returnReliability() {
    SoftwareQuality softwareQuality = ImpactMapper.convertToSoftwareQuality(RuleType.BUG);

    assertThat(softwareQuality).isEqualTo(SoftwareQuality.RELIABILITY);
  }

  @Test
  public void convertToSoftwareQuality_givenVulnerability_returnSecurity() {
    SoftwareQuality softwareQuality = ImpactMapper.convertToSoftwareQuality(RuleType.VULNERABILITY);

    assertThat(softwareQuality).isEqualTo(SoftwareQuality.SECURITY);
  }

  @Test
  public void convertToSoftwareQuality_givenSecurityHotspot_throwException() {
    ThrowableAssert.ThrowingCallable methodUnderTest = () -> ImpactMapper.convertToSoftwareQuality(RuleType.SECURITY_HOTSPOT);

    assertThatThrownBy(methodUnderTest).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void convertToSoftwareQuality_givenNull_throwException() {
    ThrowableAssert.ThrowingCallable methodUnderTest = () -> ImpactMapper.convertToSoftwareQuality(null);

    assertThatThrownBy(methodUnderTest).isInstanceOf(Throwable.class);
  }

  @Test
  public void convertToRuleType_givenMaintainability_returnCodeSmell() {
    RuleType ruleType = ImpactMapper.convertToRuleType(SoftwareQuality.MAINTAINABILITY);

    assertThat(ruleType).isEqualTo(RuleType.CODE_SMELL);
  }

  @Test
  public void convertToRuleType_givenReliability_returnBug() {
    RuleType ruleType = ImpactMapper.convertToRuleType(SoftwareQuality.RELIABILITY);

    assertThat(ruleType).isEqualTo(RuleType.BUG);
  }

  @Test
  public void convertToRuleType_givenSecurity_returnVulnerability() {
    RuleType ruleType = ImpactMapper.convertToRuleType(SoftwareQuality.SECURITY);

    assertThat(ruleType).isEqualTo(RuleType.VULNERABILITY);
  }

  @Test
  public void convertToRuleType_givenNull_throwException() {
    ThrowableAssert.ThrowingCallable methodUnderTest = () -> ImpactMapper.convertToRuleType(null);

    assertThatThrownBy(methodUnderTest).isInstanceOf(Throwable.class);
  }

  @Test
  public void convertToRuleSeverity_givenHigh_returnCritical() {
    String oldSeverity = ImpactMapper.convertToRuleSeverity(HIGH);

    assertThat(oldSeverity).isEqualTo("CRITICAL");
  }

  @Test
  public void convertToRuleSeverity_givenInfo_returnInfo() {
    String oldSeverity = ImpactMapper.convertToRuleSeverity(INFO);

    assertThat(oldSeverity).isEqualTo("INFO");
  }

  @Test
  public void convertToRuleSeverity_givenBlocker_returnBlocker() {
    String oldSeverity = ImpactMapper.convertToRuleSeverity(BLOCKER);

    assertThat(oldSeverity).isEqualTo("BLOCKER");
  }

  @Test
  public void convertToRuleSeverity_givenNull_throwException() {
    ThrowableAssert.ThrowingCallable methodUnderTest = () -> ImpactMapper.convertToRuleSeverity(null);

    assertThatThrownBy(methodUnderTest).isInstanceOf(Throwable.class);
  }

  @Test
  public void convertToSeverity_givenCritical_returnHigh() {
    Severity severity = ImpactMapper.convertToImpactSeverity("CRITICAL");

    assertThat(severity).isEqualTo(HIGH);
  }

  @Test
  public void convertToSeverity_givenMajor_returnMedium() {
    Severity severity = ImpactMapper.convertToImpactSeverity("MAJOR");

    assertThat(severity).isEqualTo(MEDIUM);
  }

  @Test
  public void convertToSeverity_givenMinor_returnLow() {
    Severity severity = ImpactMapper.convertToImpactSeverity("MINOR");

    assertThat(severity).isEqualTo(LOW);
  }

  @Test
  public void convertToSeverity_givenInfo_returnLow() {
    Severity severity = ImpactMapper.convertToImpactSeverity("INFO");

    assertThat(severity).isEqualTo(LOW);
  }

  @Test
  public void convertToSeverity_givenBlocker_returnHigh() {
    Severity severity = ImpactMapper.convertToImpactSeverity("BLOCKER");

    assertThat(severity).isEqualTo(HIGH);
  }

  @Test
  public void convertToSeverity_givenInvalidString_throwException() {
    ThrowableAssert.ThrowingCallable methodUnderTest = () -> ImpactMapper.convertToImpactSeverity("Not a severity");

    assertThatThrownBy(methodUnderTest).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void getBestImpactForBackMapping_givenEmptyMap_throwException() {
    ThrowableAssert.ThrowingCallable methodUnderTest = () -> ImpactMapper.getBestImpactForBackmapping(new HashMap<>());

    assertThatThrownBy(methodUnderTest).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void getBestImpactForBackMapping_givenOneImpact_returnThisImpact() {
    Map<SoftwareQuality, Severity> map = new HashMap<>();
    map.put(SoftwareQuality.MAINTAINABILITY, HIGH);

    Map.Entry<SoftwareQuality, Severity> bestImpactForBackMapping = ImpactMapper.getBestImpactForBackmapping(map);

    assertThat(bestImpactForBackMapping.getKey()).isEqualTo(SoftwareQuality.MAINTAINABILITY);
    assertThat(bestImpactForBackMapping.getValue()).isEqualTo(HIGH);
  }

  @Test
  public void getBestImpactForBackMapping_givenAllImpacts_returnSecurity() {
    Map<SoftwareQuality, Severity> map = new LinkedHashMap<>();
    map.put(SoftwareQuality.RELIABILITY, MEDIUM);
    map.put(SoftwareQuality.SECURITY, LOW);
    map.put(SoftwareQuality.MAINTAINABILITY, HIGH);

    Map.Entry<SoftwareQuality, Severity> bestImpactForBackMapping = ImpactMapper.getBestImpactForBackmapping(map);

    assertThat(bestImpactForBackMapping.getKey()).isEqualTo(SoftwareQuality.SECURITY);
    assertThat(bestImpactForBackMapping.getValue()).isEqualTo(LOW);
  }

  @Test
  public void orderOfSoftwareQualities_shouldIncludeAllValuesFromEnum() {
    List<SoftwareQuality> orderedSoftwareQualities = ImpactMapper.ORDERED_SOFTWARE_QUALITIES;

    assertThat(orderedSoftwareQualities).containsExactlyInAnyOrder(SoftwareQuality.values());
  }

}
