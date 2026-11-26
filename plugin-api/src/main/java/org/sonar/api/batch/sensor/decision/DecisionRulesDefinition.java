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
package org.sonar.api.batch.sensor.decision;

import java.util.List;
import org.sonar.api.Beta;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scanner.ScannerSide;

/**
 * Declares the decision rules of a plugin: the rules whose {@link NewIssueCandidate issue candidates} the scanner engine can decide, with
 * what it needs to ask for the decision.
 * <p>
 * Add the implementation as an extension in {@code Plugin.define}, guarded by
 * {@code SonarRuntime.getApiVersion().isGreaterThanOrEqual(Version.create(14, 4))}: the interface does not exist on older engines.
 * The engine calls {@link #define(Context)} once per analysis, before the sensors run. Engines without decision support never call it.
 * <p>
 * This API is experimental and can be changed or dropped at any time.
 *
 * @since 14.4
 */
@Beta
@ScannerSide
public interface DecisionRulesDefinition {

  void define(Context context);

  interface Context {
    /**
     * Starts the declaration of the decision of a rule. Call {@link NewDecisionRule#save()} to register it.
     *
     * @param ruleKey the key of the rule, including its repository
     */
    NewDecisionRule newDecisionRule(RuleKey ruleKey);
  }

  interface NewDecisionRule {
    /**
     * The version of the decision. A change to the question, the instructions, the facts or the policy must come with a new version.
     */
    NewDecisionRule setVersion(int version);

    /**
     * The question asked about each candidate of the rule.
     */
    NewDecisionRule setQuestion(String question);

    /**
     * The general instructions for answering the question.
     */
    NewDecisionRule setInstructions(String instructions);

    /**
     * Declares a fact that every candidate of the rule carries, in the order the facts are presented.
     *
     * @param name the name of the fact, as passed to {@link NewIssueCandidate#addFact(String, String)}
     * @param description what the fact contains
     */
    NewDecisionRule addFact(String name, String description);

    /**
     * The facts whose values identify a candidate when its answer is reused. By default, every fact.
     */
    NewDecisionRule setKeyFacts(List<String> names);

    NewDecisionRule setPolicy(Policy policy);

    /**
     * The probability an answer needs to be conclusive, in (0, 1]. By default, the engine's threshold.
     */
    NewDecisionRule setConfidenceThreshold(double threshold);

    /**
     * Registers the decision of the rule. An invalid declaration is not registered: the engine logs it, and the candidates of the rule take
     * the fallback of their kind.
     */
    void save();
  }

  /**
   * What an inconclusive answer does to the candidate.
   */
  enum Policy {
    /** An inconclusive answer reports the issue: only a conclusive false positive suppresses it. */
    PREFER_FALSE_POSITIVE,
    /** An inconclusive answer suppresses the issue: only a conclusive true positive reports it. */
    PREFER_FALSE_NEGATIVE
  }
}
