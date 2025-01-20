/*
 * Sonar Plugin API
 * Copyright (C) 2009-2025 SonarSource SA
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
package org.sonar.api.batch.rule;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckForNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.event.Level;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.testfixtures.log.LogTesterJUnit5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

class ChecksTest {

  private static final String REPOSITORY = "my-rule-repository";
  private static final RuleKey RULE_KEY_1 = RuleKey.of(REPOSITORY, CheckWithoutProperties.class.getCanonicalName());
  private static final RuleKey RULE_KEY_2 = RuleKey.of(REPOSITORY, CheckWithStringProperty.class.getCanonicalName());

  @RegisterExtension
  LogTesterJUnit5 logTester = new LogTesterJUnit5().setLevel(Level.DEBUG);

  @Test
  void unknown_parameter_is_ignored() {
    ActiveRule ruleWithoutProperty = new ActiveRuleWithParameter(RULE_KEY_1, "unknown-param", "ignored");
    ActiveRule ruleWithProperty = new ActiveRuleWithParameter(RULE_KEY_2, Map.of("pattern", "*", "other-param", "ignored"));
    ActiveRules activeRules = new TestActiveRules(List.of(ruleWithoutProperty, ruleWithProperty));
    CheckFactory checkFactory = new CheckFactory(activeRules);
    Checks<?> underTest = checkFactory.create(REPOSITORY);

    underTest.addAnnotatedChecks(CheckWithoutProperties.class);
    underTest.addAnnotatedChecks(CheckWithStringProperty.class);

    assertThat(underTest.of(RULE_KEY_1)).isInstanceOf(CheckWithoutProperties.class);
    assertThat(underTest.of(RULE_KEY_2))
      .asInstanceOf(type(CheckWithStringProperty.class))
      .extracting(CheckWithStringProperty::getPattern)
      .isEqualTo("*");
    assertThat(logTester.logs(Level.DEBUG))
      .contains("The field 'unknown-param' does not exist or is not annotated with @RuleProperty in the class org.sonar.api.batch.rule.CheckWithoutProperties")
      .contains("The field 'other-param' does not exist or is not annotated with @RuleProperty in the class org.sonar.api.batch.rule.CheckWithStringProperty");
  }

  @Test
  void parameter_with_a_blank_value_is_ignored() {
    ActiveRule ruleWithProperty = new ActiveRuleWithParameter(RULE_KEY_2, Map.of("pattern", ""));
    ActiveRules activeRules = new TestActiveRules(List.of(ruleWithProperty));
    CheckFactory checkFactory = new CheckFactory(activeRules);
    Checks<?> underTest = checkFactory.create(REPOSITORY);

    underTest.addAnnotatedChecks(CheckWithStringProperty.class);

    assertThat(underTest.of(RULE_KEY_2))
      .asInstanceOf(type(CheckWithStringProperty.class))
      .extracting(CheckWithStringProperty::getPattern)
      .isNull();
  }

  @Test
  void exception_is_thrown_if_parameter_type_in_invalid() {
    RuleKey ruleKey = RuleKey.of(REPOSITORY, CheckWithPrimitiveProperties.class.getCanonicalName());
    ActiveRule ruleWithProperty = new ActiveRuleWithParameter(ruleKey, Map.of("max", "not_an_integer"));
    ActiveRules activeRules = new TestActiveRules(List.of(ruleWithProperty));
    CheckFactory checkFactory = new CheckFactory(activeRules);
    Checks<?> underTest = checkFactory.create(REPOSITORY);

    assertThatExceptionOfType(NumberFormatException.class)
      .isThrownBy(() -> underTest.addAnnotatedChecks(CheckWithPrimitiveProperties.class));

    assertThat(underTest.all()).isEmpty();
  }

  private static class TestActiveRules implements ActiveRules {
    private final List<ActiveRule> rules;

    private TestActiveRules(List<ActiveRule> rules) {
      this.rules = rules;
    }

    @CheckForNull
    @Override
    public ActiveRule find(RuleKey ruleKey) {
      return null;
    }

    @Override
    public Collection<ActiveRule> findAll() {
      return null;
    }

    @Override
    public Collection<ActiveRule> findByRepository(String repository) {
      return rules;
    }

    @Override
    public Collection<ActiveRule> findByLanguage(String language) {
      return null;
    }

    @CheckForNull
    @Override
    public ActiveRule findByInternalKey(String repository, String internalKey) {
      return null;
    }
  }

  private static class ActiveRuleWithParameter implements ActiveRule {
    private final RuleKey ruleKey;
    private final Map<String, String> parameters;

    private ActiveRuleWithParameter(RuleKey ruleKey, String parameterName, String parameterValue) {
      this(ruleKey, Map.of(parameterName, parameterValue));
    }

    private ActiveRuleWithParameter(RuleKey ruleKey, Map<String, String> parameters) {
      this.ruleKey = ruleKey;
      this.parameters = parameters;
    }

    @Override
    public RuleKey ruleKey() {
      return ruleKey;
    }

    @Override
    public String severity() {
      return null;
    }

    @Override
    public String language() {
      return null;
    }

    @CheckForNull
    @Override
    public String param(String key) {
      return null;
    }

    @Override
    public Map<String, String> params() {
      return parameters;
    }

    @CheckForNull
    @Override
    public String internalKey() {
      return null;
    }

    @CheckForNull
    @Override
    public String templateRuleKey() {
      return null;
    }

    @Override
    public String qpKey() {
      return null;
    }
  }
}

