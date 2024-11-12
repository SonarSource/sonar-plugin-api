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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleScope;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.CleanCodeAttribute;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.Context;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.api.server.rule.RuleTagFormat;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.OwaspTop10;
import org.sonar.api.server.rule.RulesDefinition.OwaspTop10Version;
import org.sonar.api.server.rule.RulesDefinition.PciDssVersion;
import org.sonar.api.server.rule.RulesDefinition.OwaspAsvsVersion;
import org.sonar.api.server.rule.RulesDefinition.StigVersion;
import org.sonar.api.server.rule.StringPatternValidator;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.sonar.api.rules.RuleType.SECURITY_HOTSPOT;
import static org.sonar.api.server.rule.StringPatternValidator.validatorWithCommonPatternForKeys;
import static org.sonar.api.utils.Preconditions.checkArgument;
import static org.sonar.api.utils.Preconditions.checkState;

class DefaultNewRule extends RulesDefinition.NewRule {

  static final String SECTION_ALREADY_CONTAINS_DESCRIPTION_WITHOUT_CONTEXT = "Section with key %s already added without context information. "
    + "Impossible to mix for the same section key, context aware and non-context aware descriptions.";
  static final String CONTEXT_KEY_NOT_UNIQUE = "A context with key %s was already added to section with key %s";
  static final String SECTION_KEY_NOT_UNIQUE = "A section with key %s already exists";
  static final String MIXTURE_OF_CONTEXT_KEYS_BETWEEN_SECTIONS_ERROR_MESSAGE = "All sections providing contexts must provide the same contexts."
    + " Section '%s' has descriptions for contexts: %s, whereas section '%s' provides descriptions for contexts: %s. ";

  private static final StringPatternValidator EDUCATION_PRINCIPLE_KEYS_VALIDATOR = validatorWithCommonPatternForKeys("education principle keys");

  private final String pluginKey;
  private final String repoKey;
  private final String key;
  private RuleType type;
  private CleanCodeAttribute attribute;
  private String name;
  private String htmlDescription;
  private String markdownDescription;
  private String internalKey;
  private String severity;
  private final Map<SoftwareQuality, org.sonar.api.issue.impact.Severity> defaultImpacts = new EnumMap<>(SoftwareQuality.class);
  private boolean template;
  private RuleStatus status = RuleStatus.defaultStatus();
  private DebtRemediationFunction debtRemediationFunction;
  private String gapDescription;
  private final Set<String> tags = new TreeSet<>();
  private final Set<String> securityStandards = new TreeSet<>();
  private final Map<String, RulesDefinition.NewParam> paramsByKey = new HashMap<>();
  private final RulesDefinition.DebtRemediationFunctions functions;
  private boolean activatedByDefault;
  private RuleScope scope;
  private final Set<RuleKey> deprecatedRuleKeys = new TreeSet<>();
  private final List<RuleDescriptionSection> ruleDescriptionSections = new ArrayList<>();
  private final Set<String> educationPrincipleKeys = new TreeSet<>();

  DefaultNewRule(@Nullable String pluginKey, String repoKey, String key) {
    this.pluginKey = pluginKey;
    this.repoKey = repoKey;
    this.key = key;
    this.functions = new DefaultDebtRemediationFunctions(repoKey, key);
  }

  @Override
  public String key() {
    return this.key;
  }

  @CheckForNull
  @Override
  public RuleScope scope() {
    return this.scope;
  }

  @Override
  public DefaultNewRule setScope(RuleScope scope) {
    this.scope = scope;
    return this;
  }

  @Override
  public DefaultNewRule setName(String s) {
    this.name = trimToNull(s);
    return this;
  }

  @Override
  public DefaultNewRule setTemplate(boolean template) {
    this.template = template;
    return this;
  }

  @Override
  public DefaultNewRule setActivatedByDefault(boolean activatedByDefault) {
    this.activatedByDefault = activatedByDefault;
    return this;
  }

  @Override
  public DefaultNewRule setSeverity(String s) {
    checkArgument(Severity.ALL.contains(s), "Severity of rule %s is not correct: %s", this, s);
    this.severity = s;
    return this;
  }

  @Override
  public DefaultNewRule setType(RuleType t) {
    this.type = t;
    return this;
  }

  @Override
  public RulesDefinition.NewRule addDefaultImpact(SoftwareQuality softwareQuality, org.sonar.api.issue.impact.Severity severity) {
    checkArgument(!defaultImpacts.containsKey(softwareQuality), "Impact for Software Quality %s has already been defined for rule %s", softwareQuality, this);
    this.defaultImpacts.put(softwareQuality, severity);
    return this;
  }

  @Override
  public RulesDefinition.NewRule setCleanCodeAttribute(CleanCodeAttribute attribute) {
    this.attribute = attribute;
    return this;
  }

  @Override
  public RulesDefinition.NewRule addDescriptionSection(RuleDescriptionSection ruleDescriptionSection) {
    assertRuleDescriptionSectionIsValid(ruleDescriptionSection);
    ruleDescriptionSections.add(ruleDescriptionSection);
    return this;
  }

  private void assertRuleDescriptionSectionIsValid(RuleDescriptionSection ruleDescriptionSection) {
    ruleDescriptionSection.getContext().ifPresent(context -> assertContextAwareRuleDescriptionIsValid(ruleDescriptionSection, context));
    if (ruleDescriptionSection.getContext().isEmpty()) {
      checkArgument(isSectionKeyUnique(ruleDescriptionSection.getKey()), SECTION_KEY_NOT_UNIQUE, ruleDescriptionSection.getKey());
    }
  }

  private void assertContextAwareRuleDescriptionIsValid(RuleDescriptionSection ruleDescriptionSection, Context context) {
    String sectionKey = ruleDescriptionSection.getKey();
    String contextKey = context.getKey();
    checkArgument(isContextSetForAllRuleDescriptionSectionWithKey(sectionKey), SECTION_ALREADY_CONTAINS_DESCRIPTION_WITHOUT_CONTEXT, sectionKey);
    checkArgument(isContextKeyUniqueForSectionKey(sectionKey, contextKey), CONTEXT_KEY_NOT_UNIQUE, contextKey, sectionKey);
  }

  private boolean isContextSetForAllRuleDescriptionSectionWithKey(String sectionKey) {
    return ruleDescriptionSections.stream()
      .filter(section -> section.getKey().equals(sectionKey))
      .map(RuleDescriptionSection::getContext)
      .allMatch(Optional::isPresent);
  }

  private boolean isContextKeyUniqueForSectionKey(String sectionKey, String contextKey) {
    return ruleDescriptionSections.stream()
      .filter(section -> section.getKey().equals(sectionKey))
      .map(RuleDescriptionSection::getContext)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .noneMatch(context -> contextKey.equals(context.getKey()));
  }

  private boolean isSectionKeyUnique(String sectionKey) {
    return ruleDescriptionSections.stream()
      .map(RuleDescriptionSection::getKey)
      .noneMatch(alreadyExistingKey -> alreadyExistingKey.equals(sectionKey));
  }

  @Override
  public DefaultNewRule setHtmlDescription(@Nullable String s) {
    checkState(markdownDescription == null, "Rule '%s' already has a Markdown description", this);
    this.htmlDescription = trimToNull(s);
    return this;
  }

  @Override
  public DefaultNewRule setHtmlDescription(@Nullable URL classpathUrl) {
    if (classpathUrl != null) {
      try {
        setHtmlDescription(IOUtils.toString(classpathUrl, UTF_8));
      } catch (IOException e) {
        throw new IllegalStateException("Fail to read: " + classpathUrl, e);
      }
    } else {
      this.htmlDescription = null;
    }
    return this;
  }

  @Override
  @Deprecated(since = "9.6", forRemoval = true)
  @SuppressWarnings({"removal"})
  public DefaultNewRule setMarkdownDescription(@Nullable String s) {
    checkState(htmlDescription == null, "Rule '%s' already has an HTML description", this);
    this.markdownDescription = trimToNull(s);
    return this;
  }

  @Override
  @Deprecated(since = "9.6", forRemoval = true)
  @SuppressWarnings({"removal"})
  public DefaultNewRule setMarkdownDescription(@Nullable URL classpathUrl) {
    if (classpathUrl != null) {
      try {
        setMarkdownDescription(IOUtils.toString(classpathUrl, UTF_8));
      } catch (IOException e) {
        throw new IllegalStateException("Fail to read: " + classpathUrl, e);
      }
    } else {
      this.markdownDescription = null;
    }
    return this;
  }

  @Override
  public DefaultNewRule setStatus(RuleStatus status) {
    checkArgument(RuleStatus.REMOVED != status, "Status 'REMOVED' is not accepted on rule '%s'", this);
    this.status = status;
    return this;
  }

  @Override
  public RulesDefinition.DebtRemediationFunctions debtRemediationFunctions() {
    return functions;
  }

  @Override
  public DefaultNewRule setDebtRemediationFunction(@Nullable DebtRemediationFunction fn) {
    this.debtRemediationFunction = fn;
    return this;
  }

  @Override
  public DefaultNewRule setGapDescription(@Nullable String s) {
    this.gapDescription = s;
    return this;
  }

  @Override
  public RulesDefinition.NewParam createParam(String paramKey) {
    checkArgument(!paramsByKey.containsKey(paramKey), "The parameter '%s' is declared several times on the rule %s", paramKey, this);
    DefaultNewParam param = new DefaultNewParam(paramKey);
    paramsByKey.put(paramKey, param);
    return param;
  }

  @CheckForNull
  @Override
  public RulesDefinition.NewParam param(String paramKey) {
    return paramsByKey.get(paramKey);
  }

  @Override
  public Collection<RulesDefinition.NewParam> params() {
    return paramsByKey.values();
  }

  @Override
  public DefaultNewRule addTags(String... list) {
    for (String tag : list) {
      RuleTagFormat.validate(tag);
      tags.add(tag);
    }
    return this;
  }

  @Override
  public DefaultNewRule setTags(String... list) {
    tags.clear();
    addTags(list);
    return this;
  }

  @Override
  public DefaultNewRule addOwaspTop10(OwaspTop10... standards) {
    return addOwaspTop10(OwaspTop10Version.Y2017, standards);
  }

  @Override
  public DefaultNewRule addOwaspTop10(OwaspTop10Version owaspTop10Version, OwaspTop10... standards) {
    requireNonNull(owaspTop10Version, "Owasp version must not be null");

    for (OwaspTop10 owaspTop10 : standards) {
      String standard = owaspTop10Version.prefix() + ":" + owaspTop10.name().toLowerCase(Locale.ENGLISH);
      securityStandards.add(standard);
    }
    return this;
  }

  @Override
  public DefaultNewRule addOwaspAsvs(OwaspAsvsVersion owaspAsvsVersion, String... requirements) {
    requireNonNull(owaspAsvsVersion, "OWASP ASVS version must not be null");
    requireNonNull(requirements, "Requirements for OWASP ASVS standard must not be null");
    for (String requirement : requirements) {
      String standard = owaspAsvsVersion.prefix() + ":" + requirement;
      securityStandards.add(standard);
    }
    return this;
  }

  @Override
  public DefaultNewRule addPciDss(PciDssVersion pciDssVersion, String... requirements) {
    requireNonNull(pciDssVersion, "PCI DSS version must not be null");
    requireNonNull(requirements, "Requirements for PCI DSS standard must not be null");

    for (String requirement : requirements) {
      String standard = pciDssVersion.prefix() + ":" + requirement;
      securityStandards.add(standard);
    }
    return this;
  }

  @Override
  public DefaultNewRule addCwe(int... nums) {
    for (int num : nums) {
      String standard = "cwe:" + num;
      securityStandards.add(standard);
    }
    return this;
  }

  @Override
  public DefaultNewRule addStig(StigVersion stigVersion, String... requirements) {
    requireNonNull(stigVersion, "STIG version must not be null");
    requireNonNull(requirements, "Requirements for STIG standard must not be null");

    for (String requirement : requirements) {
      String standard = stigVersion.prefix() + ":" + requirement;
      securityStandards.add(standard);
    }
    return this;
  }

  @Override
  public DefaultNewRule setInternalKey(@Nullable String s) {
    this.internalKey = s;
    return this;
  }

  void validate() {
    if (isEmpty(name)) {
      throw new IllegalStateException(format("Name of rule %s is empty", this));
    }
    if (isEmpty(htmlDescription) && isEmpty(markdownDescription)) {
      throw new IllegalStateException(format("One of HTML description or Markdown description must be defined for rule %s", this));
    }
    if (type != null && type.equals(SECURITY_HOTSPOT)){
      attribute = null;
      defaultImpacts.clear();
    }
    validateSameContextKeysExistsForAllContextualizedSections(ruleDescriptionSections);
  }

  private static void validateSameContextKeysExistsForAllContextualizedSections(List<RuleDescriptionSection> ruleDescriptionSections) {
    Map<String, Set<String>> sectionKeyToContextKeys = ruleDescriptionSections.stream()
      .filter(section -> section.getContext().isPresent())
      .collect(groupingBy(RuleDescriptionSection::getKey, mapping(s -> s.getContext().get().getKey(), Collectors.toSet())));

    assertAllSectionsContainsSameContextKeys(sectionKeyToContextKeys);
  }

  private static void assertAllSectionsContainsSameContextKeys(Map<String, Set<String>> sectionKeyToContextKeys) {
    if (sectionKeyToContextKeys.isEmpty()) {
      return;
    }

    Map.Entry<String, Set<String>> referenceSectionToContexts = sectionKeyToContextKeys.entrySet().iterator().next();
    String referenceSectionKey = referenceSectionToContexts.getKey();
    Set<String> referenceContexts = referenceSectionToContexts.getValue();

    sectionKeyToContextKeys.forEach((sectionKey, contextKeys) ->
      checkArgument(contextKeys.equals(referenceContexts), MIXTURE_OF_CONTEXT_KEYS_BETWEEN_SECTIONS_ERROR_MESSAGE,
        referenceSectionKey, referenceContexts, sectionKey, contextKeys)
    );
  }

  @Override
  public DefaultNewRule addDeprecatedRuleKey(String repository, String key) {
    deprecatedRuleKeys.add(RuleKey.of(repository, key));
    return this;
  }

  @Override
  public DefaultNewRule addEducationPrincipleKeys(String... keys) {
    Set<String> candidateEducationPrincipleKeys = Set.of(keys);
    EDUCATION_PRINCIPLE_KEYS_VALIDATOR.validate(candidateEducationPrincipleKeys);
    educationPrincipleKeys.addAll(candidateEducationPrincipleKeys);
    return this;
  }

  String pluginKey() {
    return pluginKey;
  }

  String repoKey() {
    return repoKey;
  }

  @CheckForNull
  RuleType type() {
    return type;
  }

  CleanCodeAttribute cleanCodeAttribute() {
    return attribute;
  }

  String name() {
    return name;
  }

  public List<RuleDescriptionSection> getRuleDescriptionSections() {
    return Collections.unmodifiableList(ruleDescriptionSections);
  }

  public Set<String> educationPrincipleKeys() {
    return Collections.unmodifiableSet(educationPrincipleKeys);
  }

  String htmlDescription() {
    return htmlDescription;
  }

  @Deprecated(since = "9.6", forRemoval = true)
  String markdownDescription() {
    return markdownDescription;
  }

  @CheckForNull
  String internalKey() {
    return internalKey;
  }

  @CheckForNull
  String severity() {
    return severity;
  }

  Map<SoftwareQuality, org.sonar.api.issue.impact.Severity> defaultImpacts() {
    return defaultImpacts;
  }

  boolean template() {
    return template;
  }

  RuleStatus status() {
    return status;
  }

  DebtRemediationFunction debtRemediationFunction() {
    return debtRemediationFunction;
  }

  String gapDescription() {
    return gapDescription;
  }

  Set<String> tags() {
    return tags;
  }

  Set<String> securityStandards() {
    return securityStandards;
  }

  Map<String, RulesDefinition.NewParam> paramsByKey() {
    return paramsByKey;
  }

  boolean activatedByDefault() {
    return activatedByDefault;
  }

  Set<RuleKey> deprecatedRuleKeys() {
    return deprecatedRuleKeys;
  }

  @Override
  public String toString() {
    return format("[repository=%s, key=%s]", repoKey, key);
  }
}
