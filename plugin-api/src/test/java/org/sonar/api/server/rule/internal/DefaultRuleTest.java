/*
 * Sonar Plugin API
 * Copyright (C) 2009-2023 SonarSource SA
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

import org.junit.Test;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleScope;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.CleanCodeAttribute;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.api.server.rule.RuleDescriptionSectionBuilder;
import org.sonar.api.server.rule.RulesDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class DefaultRuleTest {

  private static final RuleDescriptionSection RULE_DESCRIPTION_SECTION = new RuleDescriptionSectionBuilder().sectionKey("section_key").htmlContent("html desc").build();
  private static final RuleDescriptionSection RULE_DESCRIPTION_SECTION_2 = new RuleDescriptionSectionBuilder().sectionKey("section_key_2").htmlContent("html desc 2").build();

  @Test
  public void getters() {
    DefaultRepository repo = mock(DefaultRepository.class);
    DefaultNewRule rule = new DefaultNewRule("plugin", "repo", "key");

    rule.setScope(RuleScope.MAIN);
    rule.setName("   name  ");
    rule.setHtmlDescription("   html  ");
    rule.addDescriptionSection(RULE_DESCRIPTION_SECTION);
    rule.addDescriptionSection(RULE_DESCRIPTION_SECTION_2);
    rule.setTemplate(true);
    rule.setActivatedByDefault(true);
    RulesDefinition.NewParam param1 = rule.createParam("param1");
    rule.setTags("tag1", "tag2");
    rule.addTags("tag3");
    rule.setGapDescription("gap");
    rule.setInternalKey("internal");
    rule.addDeprecatedRuleKey("deprecatedrepo", "deprecatedkey");
    rule.setStatus(RuleStatus.READY);
    rule.addCwe(12);
    rule.addCwe(10);
    rule.setType(RuleType.SECURITY_HOTSPOT);
    rule.setCleanCodeAttribute(CleanCodeAttribute.COMPLETE);
    DebtRemediationFunction f = mock(DebtRemediationFunction.class);
    rule.setDebtRemediationFunction(f);
    rule.setSeverity("MAJOR");
    rule.addEducationPrincipleKeys("principle_key1", "principle_key2", "principle_key3");

    DefaultRule defaultRule = new DefaultRule(repo, rule);
    assertThat(defaultRule.scope()).isEqualTo(RuleScope.MAIN);
    assertThat(defaultRule.name()).isEqualTo("name");
    assertThat(defaultRule.htmlDescription()).isEqualTo("html");
    assertThat(defaultRule.ruleDescriptionSections()).containsExactly(RULE_DESCRIPTION_SECTION, RULE_DESCRIPTION_SECTION_2);
    assertThat(defaultRule.template()).isTrue();
    assertThat(defaultRule.activatedByDefault()).isTrue();
    assertThat(defaultRule.params()).containsOnly(new DefaultParam(new DefaultNewParam("param1")));
    assertThat(defaultRule.tags()).containsOnly("tag1", "tag2", "tag3");
    assertThat(defaultRule.gapDescription()).isEqualTo("gap");
    assertThat(defaultRule.internalKey()).isEqualTo("internal");
    assertThat(defaultRule.deprecatedRuleKeys()).containsOnly(RuleKey.of("deprecatedrepo", "deprecatedkey"));
    assertThat(defaultRule.status()).isEqualTo(RuleStatus.READY);
    assertThat(rule.securityStandards()).containsOnly("cwe:10", "cwe:12");
    assertThat(defaultRule.type()).isEqualTo(RuleType.SECURITY_HOTSPOT);
    assertThat(defaultRule.cleanCodeAttribute()).isEqualTo(CleanCodeAttribute.COMPLETE);
    assertThat(defaultRule.debtRemediationFunction()).isEqualTo(f);
    assertThat(defaultRule.markdownDescription()).isNull();
    assertThat(defaultRule.severity()).isEqualTo("MAJOR");
    assertThat(defaultRule.educationPrincipleKeys()).containsOnly("principle_key1", "principle_key2", "principle_key3");
  }

  @Test
  public void to_string() {
    DefaultRepository repo = mock(DefaultRepository.class);
    DefaultNewRule rule = new DefaultNewRule("plugin", "repo", "key");
    DefaultRule defaultRule = new DefaultRule(repo, rule);

    assertThat(defaultRule).hasToString("[repository=repo, key=key]");
  }
}
