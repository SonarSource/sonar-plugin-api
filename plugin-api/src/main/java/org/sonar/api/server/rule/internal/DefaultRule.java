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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;
import org.sonar.api.issue.impact.Severity;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleScope;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.CleanCodeAttribute;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.api.server.rule.RuleTagsToTypeConverter;
import org.sonar.api.server.rule.RulesDefinition;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static org.sonar.api.rule.Severity.MAJOR;

@Immutable
public class DefaultRule extends RulesDefinition.Rule {
  private final String pluginKey;
  private final RulesDefinition.Repository repository;
  private final String repoKey;
  private final String key;
  private final String name;
  private final RuleType type;
  private final CleanCodeAttribute cleanCodeAttribute;
  private final String htmlDescription;
  private final String markdownDescription;
  private final String internalKey;
  private final String severity;
  private final Map<SoftwareQuality, Severity> defaultImpacts;
  private final boolean template;
  private final DebtRemediationFunction debtRemediationFunction;
  private final String gapDescription;
  private final Set<String> tags;
  private final Set<String> securityStandards;
  private final Map<String, RulesDefinition.Param> params;
  private final RuleStatus status;
  private final boolean activatedByDefault;
  private final RuleScope scope;
  private final Set<RuleKey> deprecatedRuleKeys;
  private final List<RuleDescriptionSection> ruleDescriptionSections;
  private final Set<String> educationPrincipleKeys;

  @SuppressWarnings({"removal"})
  DefaultRule(DefaultRepository repository, DefaultNewRule newRule) {
    this.pluginKey = newRule.pluginKey();
    this.repository = repository;
    this.repoKey = newRule.repoKey();
    this.key = newRule.key();
    this.name = newRule.name();
    this.htmlDescription = newRule.htmlDescription();
    this.markdownDescription = newRule.markdownDescription();
    this.internalKey = newRule.internalKey();
    this.template = newRule.template();
    this.status = newRule.status();
    this.debtRemediationFunction = newRule.debtRemediationFunction();
    this.gapDescription = newRule.gapDescription();
    this.scope = newRule.scope() == null ? RuleScope.MAIN : newRule.scope();
    this.cleanCodeAttribute = newRule.cleanCodeAttribute();
    Set<String> tagsBuilder = new TreeSet<>(newRule.tags());
    tagsBuilder.removeAll(RuleTagsToTypeConverter.RESERVED_TAGS);
    this.tags = Collections.unmodifiableSet(tagsBuilder);
    this.securityStandards = Collections.unmodifiableSet(new TreeSet<>(newRule.securityStandards()));
    Map<String, RulesDefinition.Param> paramsBuilder = new HashMap<>();
    for (RulesDefinition.NewParam newParam : newRule.paramsByKey().values()) {
      paramsBuilder.put(newParam.key(), new DefaultParam((DefaultNewParam) newParam));
    }
    this.params = Collections.unmodifiableMap(paramsBuilder);
    this.activatedByDefault = newRule.activatedByDefault();
    this.deprecatedRuleKeys = Collections.unmodifiableSet(new TreeSet<>(newRule.deprecatedRuleKeys()));
    this.ruleDescriptionSections = newRule.getRuleDescriptionSections();
    this.educationPrincipleKeys = Collections.unmodifiableSet(newRule.educationPrincipleKeys());

    this.type = determineType(newRule);
    this.severity = determineSeverity(newRule);
    this.defaultImpacts = determineImpacts(newRule);
  }

  private static RuleType determineType(DefaultNewRule newRule) {
    RuleType type = newRule.type() == null ? RuleTagsToTypeConverter.convert(newRule.tags()) : newRule.type();
    if (type != null) {
      return type;
    }

    if (shouldUseBackmapping(newRule)) {
      SoftwareQuality softwareQuality = ImpactMapper.getBestImpactForBackmapping(newRule.defaultImpacts()).getKey();
      return ImpactMapper.convertToRuleType(softwareQuality);
    }
    return RuleType.CODE_SMELL;
  }

  private static String determineSeverity(DefaultNewRule newRule) {
    String severity = newRule.severity();
    if (severity != null) {
      return severity;
    }

    if (shouldUseBackmapping(newRule)) {
      Severity impactSeverity = ImpactMapper.getBestImpactForBackmapping(newRule.defaultImpacts()).getValue();
      return ImpactMapper.convertToRuleSeverity(impactSeverity);
    }
    return MAJOR;
  }

  private Map<SoftwareQuality, Severity> determineImpacts(DefaultNewRule newRule) {
    if (!newRule.defaultImpacts().isEmpty() || type == RuleType.SECURITY_HOTSPOT) {
      return Collections.unmodifiableMap(newRule.defaultImpacts());
    }
    SoftwareQuality softwareQuality = ImpactMapper.convertToSoftwareQuality(type);
    Severity impactSeverity = ImpactMapper.convertToImpactSeverity(severity);

    return Map.of(softwareQuality, impactSeverity);
  }

  private static boolean shouldUseBackmapping(DefaultNewRule newRule) {
    return newRule.type() == null && newRule.severity() == null && !newRule.defaultImpacts().isEmpty();
  }

  @Override
  public RulesDefinition.Repository repository() {
    return repository;
  }

  @Override
  @CheckForNull
  public String pluginKey() {
    return pluginKey;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public RuleScope scope() {
    return scope;
  }

  @Override
  public RuleType type() {
    return type;
  }

  @CheckForNull
  @Override
  public CleanCodeAttribute cleanCodeAttribute() {
    return cleanCodeAttribute;
  }

  @Override
  public String severity() {
    return severity;
  }

  @Override
  public Map<SoftwareQuality, Severity> defaultImpacts() {
    return defaultImpacts;
  }

  @Override
  public List<RuleDescriptionSection> ruleDescriptionSections() {
    return Collections.unmodifiableList(ruleDescriptionSections);
  }

  @Override
  public Set<String> educationPrincipleKeys() {
    return educationPrincipleKeys;
  }

  @Override
  @CheckForNull
  public String htmlDescription() {
    return htmlDescription;
  }

  /**
   * @deprecated since 9.6 markdown support fur rule descriptions will be dropped
   */
  @Override
  @CheckForNull
  @Deprecated(since = "9.6", forRemoval = true)
  @SuppressWarnings({"removal"})
  public String markdownDescription() {
    return markdownDescription;
  }

  @Override
  public boolean template() {
    return template;
  }

  @Override
  public boolean activatedByDefault() {
    return activatedByDefault;
  }

  @Override
  public RuleStatus status() {
    return status;
  }

  @CheckForNull
  @Override
  public DebtRemediationFunction debtRemediationFunction() {
    return debtRemediationFunction;
  }

  @CheckForNull
  @Override
  public String gapDescription() {
    return gapDescription;
  }

  @CheckForNull
  @Override
  public RulesDefinition.Param param(String key) {
    return params.get(key);
  }

  @Override
  public List<RulesDefinition.Param> params() {
    return unmodifiableList(new ArrayList<>(params.values()));
  }

  @Override
  public Set<String> tags() {
    return tags;
  }

  @Override
  public Set<String> securityStandards() {
    return securityStandards;
  }

  @Override
  public Set<RuleKey> deprecatedRuleKeys() {
    return deprecatedRuleKeys;
  }

  @CheckForNull
  @Override
  public String internalKey() {
    return internalKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultRule other = (DefaultRule) o;
    return key.equals(other.key) && repoKey.equals(other.repoKey);
  }

  @Override
  public int hashCode() {
    int result = repoKey.hashCode();
    result = 31 * result + key.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return format("[repository=%s, key=%s]", repoKey, key);
  }

}
