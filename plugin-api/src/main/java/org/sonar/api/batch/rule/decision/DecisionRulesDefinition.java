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
package org.sonar.api.batch.rule.decision;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import javax.annotation.CheckForNull;
import org.sonar.api.Beta;
import org.sonar.api.ExtensionPoint;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scanner.ScannerSide;

/**
 * Defines the decision specifications of rules whose issues are reported as issue candidates.
 * <h2>How to use</h2>
 * <pre>
 * public class MyDecisionRulesDefinition implements DecisionRulesDefinition {
 *   {@literal @}Override
 *   public void define(Context context) {
 *     NewDecisionRepository repository = context.createRepository("my_js");
 *     repository.createRule("x1")
 *       .setVersion(1)
 *       .setQuestion("...")
 *       .setInstructions("...")
 *       .addFact("fact1", "...")
 *       .setPolicy(Policy.PREFER_FALSE_NEGATIVE)
 *       .setKeyFacts("fact1");
 *     // don't forget to call done() to register the repository
 *     repository.done();
 *   }
 * }
 * </pre>
 * This API is experimental and can be changed or dropped at any time.
 *
 * @since 14.3
 */
@Beta
@ScannerSide
@ExtensionPoint
public interface DecisionRulesDefinition {

  void define(Context context);

  interface Context {

    /**
     * Creates the decision repository of a rule repository. Call {@link NewDecisionRepository#done()} to register it.
     *
     * @param repositoryKey the key of an existing rule repository. The specifications of rules that are not active are ignored.
     */
    NewDecisionRepository createRepository(String repositoryKey);

    @CheckForNull
    DecisionRepository repository(String repositoryKey);

    List<DecisionRepository> repositories();
  }

  interface NewDecisionRepository {

    /**
     * Creates the decision specification of a rule of this repository. The key must be unique in the repository.
     *
     * @throws IllegalArgumentException if the key is not unique
     */
    NewDecisionRule createRule(String ruleKey);

    /**
     * Registers this repository. The engine ignores invalid specifications, with a warning.
     */
    void done();
  }

  /**
   * Builder of the decision specification of a rule.
   */
  interface NewDecisionRule {

    /**
     * The version of the specification. Any change to the specification must increase it. Must be positive.
     */
    NewDecisionRule setVersion(int version);

    /**
     * The main question the decision answers. Must not be blank.
     */
    NewDecisionRule setQuestion(String question);

    /**
     * General instructions for the decision. Must not be blank.
     */
    NewDecisionRule setInstructions(String instructions);

    /**
     * Declares a fact that the issue candidates of the rule provide. Facts are kept in declaration order.
     *
     * @param name the name of the fact, as used in {@link org.sonar.api.batch.sensor.decision.NewIssueCandidate#addFact(String, String)}
     * @param description the description of the fact for the decision
     */
    NewDecisionRule addFact(String name, String description);

    /**
     * How inconclusive answers are treated.
     */
    NewDecisionRule setPolicy(Policy policy);

    /**
     * The facts that identify an instance of the decision. Must be declared facts.
     */
    NewDecisionRule setKeyFacts(String... factNames);

    /**
     * Replaces the default confidence threshold of the decision. Optional. Must be in (0, 1].
     */
    NewDecisionRule setConfidenceThresholdOverride(double threshold);
  }

  interface DecisionRepository {

    String key();

    @CheckForNull
    DecisionRule rule(String ruleKey);

    List<DecisionRule> rules();
  }

  /**
   * A registered decision specification. See {@link NewDecisionRule} for the meaning of each field.
   */
  interface DecisionRule {

    RuleKey ruleKey();

    int version();

    String question();

    String instructions();

    /**
     * The facts, from name to description, in declaration order.
     */
    Map<String, String> facts();

    Policy policy();

    List<String> keyFacts();

    OptionalDouble confidenceThresholdOverride();
  }

  /**
   * The reporting policy of a rule, applied when the decision is inconclusive.
   */
  enum Policy {
    /**
     * Report the issue unless the decision confidently suppresses it.
     */
    PREFER_FALSE_POSITIVE,
    /**
     * Report the issue only if the decision confidently confirms it.
     */
    PREFER_FALSE_NEGATIVE
  }
}
