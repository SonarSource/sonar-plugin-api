/*
 * Sonar Plugin API
 * Copyright (C) 2009-2022 SonarSource SA
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
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.Context;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.api.server.rule.RuleDescriptionSectionBuilder;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.OwaspTop10;
import org.sonar.api.server.rule.RulesDefinition.OwaspTop10Version;
import org.sonar.api.server.rule.RulesDefinition.PciDssVersion;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.sonar.api.server.rule.internal.DefaultNewRule.CONTEXT_KEY_NOT_UNIQUE;
import static org.sonar.api.server.rule.internal.DefaultNewRule.SECTION_ALREADY_CONTAINS_DESCRIPTION_WITHOUT_CONTEXT;
import static org.sonar.api.server.rule.internal.DefaultNewRule.SECTION_KEY_NOT_UNIQUE;

public class DefaultNewRuleTest {

  private static final RuleDescriptionSection RULE_DESCRIPTION_SECTION = new RuleDescriptionSectionBuilder().sectionKey("section_key").htmlContent("html desc").build();
  private static final Context CONTEXT_WITH_KEY_1 = new Context("ctx1", "DISPLAY_1");
  private static final Context CONTEXT_WITH_KEY_2 = new Context("ctx2", "DISPLAY_2");
  private static final RuleDescriptionSection CONTEXT_AWARE_RULE_DESCRIPTION_SECTION = new RuleDescriptionSectionBuilder().sectionKey(
    RULE_DESCRIPTION_SECTION.getKey()).htmlContent("Html desc").context(CONTEXT_WITH_KEY_1).build();
  private final DefaultNewRule rule = new DefaultNewRule("plugin", "repo", "key");

  @Test
  public void testSimpleSetGet() {
    assertThat(rule.pluginKey()).isEqualTo("plugin");
    assertThat(rule.repoKey()).isEqualTo("repo");
    assertThat(rule.key()).isEqualTo("key");

    rule.setScope(RuleScope.MAIN);
    assertThat(rule.scope()).isEqualTo(RuleScope.MAIN);

    rule.setName("   name  ");
    assertThat(rule.name()).isEqualTo("name");

    rule.setHtmlDescription("   html  ");
    assertThat(rule.htmlDescription()).isEqualTo("html");

    rule.setTemplate(true);
    assertThat(rule.template()).isTrue();

    rule.setActivatedByDefault(true);
    assertThat(rule.activatedByDefault()).isTrue();

    RulesDefinition.NewParam param1 = rule.createParam("param1");
    assertThat(rule.param("param1")).isEqualTo(param1);
    assertThat(rule.params()).containsOnly(param1);

    rule.setTags("tag1", "tag2");
    rule.addTags("tag3");
    assertThat(rule.tags()).containsExactly("tag1", "tag2", "tag3");

    rule.setGapDescription("effort");
    assertThat(rule.gapDescription()).isEqualTo("effort");

    rule.setInternalKey("internal");
    assertThat(rule.internalKey()).isEqualTo("internal");

    rule.addDeprecatedRuleKey("deprecatedrepo", "deprecatedkey");
    assertThat(rule.deprecatedRuleKeys()).containsOnly(RuleKey.of("deprecatedrepo", "deprecatedkey"));

    rule.setStatus(RuleStatus.READY);
    assertThat(rule.status()).isEqualTo(RuleStatus.READY);

    rule.addCwe(12);
    rule.addCwe(10);
    assertThat(rule.securityStandards()).containsOnly("cwe:10", "cwe:12");

    rule.addOwaspTop10(OwaspTop10.A1, OwaspTop10.A2);
    rule.addOwaspTop10(OwaspTop10Version.Y2017, OwaspTop10.A4);
    rule.addOwaspTop10(OwaspTop10Version.Y2021, OwaspTop10.A5, OwaspTop10.A3);
    assertThat(rule.securityStandards())
      .contains("owaspTop10:a1", "owaspTop10:a2", "owaspTop10:a4", "owaspTop10-2021:a3", "owaspTop10-2021:a5");

    rule.addPciDss(PciDssVersion.V3_2, "6.5.1");
    rule.addPciDss(PciDssVersion.V3_2, "6.5");
    rule.addPciDss(PciDssVersion.V4_0, "6.5.2", "6.5.10");

    assertThat(rule.securityStandards())
      .contains("pciDss-3.2:6.5.1", "pciDss-3.2:6.5", "pciDss-4.0:6.5.2", "pciDss-4.0:6.5.10");

    rule.setType(RuleType.SECURITY_HOTSPOT);
    assertThat(rule.type()).isEqualTo(RuleType.SECURITY_HOTSPOT);

    DebtRemediationFunction f = mock(DebtRemediationFunction.class);
    rule.setDebtRemediationFunction(f);
    assertThat(rule.debtRemediationFunction()).isEqualTo(f);

    rule.setSeverity("MAJOR");
    assertThat(rule.severity()).isEqualTo("MAJOR");

    rule.addDescriptionSection(RULE_DESCRIPTION_SECTION);
    assertThat(rule.getRuleDescriptionSections()).containsExactly(RULE_DESCRIPTION_SECTION);
  }

  @Test
  public void validate_fails() {
    rule.setHtmlDescription("html");

    assertThatThrownBy(() -> rule.validate())
      .isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void validate_succeeds() {
    rule.setHtmlDescription("html");
    rule.setName("name");
    rule.validate();
  }

  @Test
  public void set_markdown_description() {
    rule.setMarkdownDescription("markdown");
    assertThat(rule.markdownDescription()).isEqualTo("markdown");
  }

  @Test
  public void fail_if_severity_is_invalid() {
    assertThatThrownBy(() -> rule.setSeverity("invalid"))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void fail_setting_markdown_if_html_is_set() {
    rule.setHtmlDescription("html");

    assertThatThrownBy(() -> rule.setMarkdownDescription("markdown"))
      .isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void fail_if_set_status_to_removed() {
    assertThatThrownBy(() -> rule.setStatus(RuleStatus.REMOVED))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void fail_if_null_owasp_version() {
    assertThatThrownBy(() -> rule.addOwaspTop10((OwaspTop10Version) null, OwaspTop10.A1))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("Owasp version must not be null");
  }

  @Test
  public void fail_if_null_pci_dss_version() {
    assertThatThrownBy(() -> rule.addPciDss(null, "6.5.1"))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("PCI DSS version must not be null");
  }

  @Test
  public void fail_if_null_pci_dss_array() {
    assertThatThrownBy(() -> rule.addPciDss(PciDssVersion.V3_2, (String[]) null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("Requirements for PCI DSS standard must not be null");
  }

  @Test
  public void fail_if_trying_to_insert_two_sections_with_same_keys() {
    rule.addDescriptionSection(new RuleDescriptionSectionBuilder().sectionKey(RULE_DESCRIPTION_SECTION.getKey()).htmlContent("Html desc").build());
    assertThatThrownBy(() -> rule.addDescriptionSection(RULE_DESCRIPTION_SECTION))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(format(SECTION_KEY_NOT_UNIQUE, RULE_DESCRIPTION_SECTION.getKey()));
  }

  @Test
  public void succeed_if_trying_to_insert_two_sections_with_different_keys() {
    rule.addDescriptionSection(RULE_DESCRIPTION_SECTION);
    RuleDescriptionSection ruleDescriptionSection2 = new RuleDescriptionSectionBuilder().sectionKey("key2").htmlContent("Html desc").build();
    rule.addDescriptionSection(ruleDescriptionSection2);
    RuleDescriptionSection ruleDescriptionSection3 = new RuleDescriptionSectionBuilder().sectionKey("key3").htmlContent("Html desc").build();
    rule.addDescriptionSection(ruleDescriptionSection3);

    assertThat(rule.getRuleDescriptionSections()).containsOnly(RULE_DESCRIPTION_SECTION, ruleDescriptionSection2, ruleDescriptionSection3);
  }

  @Test
  public void succeed_if_trying_to_insert_two_contexts_for_same_section_with_different_keys() {
    rule.addDescriptionSection(CONTEXT_AWARE_RULE_DESCRIPTION_SECTION);
    RuleDescriptionSection ruleDescriptionSection2 = new RuleDescriptionSectionBuilder()
      .sectionKey(RULE_DESCRIPTION_SECTION.getKey())
      .htmlContent("Html desc 2")
      .context(CONTEXT_WITH_KEY_2)
      .build();
    rule.addDescriptionSection(ruleDescriptionSection2);

    assertThat(rule.getRuleDescriptionSections()).containsOnly(CONTEXT_AWARE_RULE_DESCRIPTION_SECTION, ruleDescriptionSection2);
  }

  @Test
  public void succeed_if_trying_to_insert_two_contexts_for_two_sections_with_same_keys() {
    rule.addDescriptionSection(CONTEXT_AWARE_RULE_DESCRIPTION_SECTION);
    RuleDescriptionSection ruleDescriptionSection2 = new RuleDescriptionSectionBuilder()
      .sectionKey("key2")
      .htmlContent("Html desc 2")
      .context(CONTEXT_WITH_KEY_2)
      .build();
    rule.addDescriptionSection(ruleDescriptionSection2);

    assertThat(rule.getRuleDescriptionSections()).containsOnly(CONTEXT_AWARE_RULE_DESCRIPTION_SECTION, ruleDescriptionSection2);
  }

  @Test
  public void fail_if_trying_to_insert_two_contexts_for_same_section_with_same_keys() {
    String contextKey = CONTEXT_AWARE_RULE_DESCRIPTION_SECTION.getContext().orElseThrow().getKey();
    rule.addDescriptionSection(CONTEXT_AWARE_RULE_DESCRIPTION_SECTION);
    RuleDescriptionSection ruleDescriptionSection2 = new RuleDescriptionSectionBuilder()
      .sectionKey(CONTEXT_AWARE_RULE_DESCRIPTION_SECTION.getKey())
      .htmlContent("Html desc 2")
      .context(new Context(contextKey, "bla"))
      .build();

    assertThatThrownBy(() -> rule.addDescriptionSection(ruleDescriptionSection2))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(format(CONTEXT_KEY_NOT_UNIQUE, contextKey, RULE_DESCRIPTION_SECTION.getKey()));
  }

  @Test
  public void fail_if_trying_to_insert_non_context_aware_section_and_then_context_aware_section_for_same_section_key() {
    rule.addDescriptionSection(RULE_DESCRIPTION_SECTION);

    assertThatThrownBy(() -> rule.addDescriptionSection(CONTEXT_AWARE_RULE_DESCRIPTION_SECTION))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(format(SECTION_ALREADY_CONTAINS_DESCRIPTION_WITHOUT_CONTEXT, RULE_DESCRIPTION_SECTION.getKey()));
  }

  @Test
  public void fail_if_trying_to_insert_context_aware_section_and_then_non_context_aware_section_for_same_section_key() {
    rule.addDescriptionSection(CONTEXT_AWARE_RULE_DESCRIPTION_SECTION);

    assertThatThrownBy(() -> rule.addDescriptionSection(RULE_DESCRIPTION_SECTION))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(format(SECTION_KEY_NOT_UNIQUE, RULE_DESCRIPTION_SECTION.getKey()));
  }
}
