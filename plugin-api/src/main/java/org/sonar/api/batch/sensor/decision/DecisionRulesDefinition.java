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

import java.net.URL;
import org.sonar.api.Beta;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scanner.ScannerSide;

/**
 * Declares the decision specifications of the rules of a plugin, so that the scanner engine can decide the
 * {@link NewIssueCandidate issue candidates} raised for them.
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
     * Declares the decision specification of a rule, as copied from RSPEC into the plugin resources by rule-api.
     * The engine reads, parses and validates the resource. An invalid or missing specification is skipped with a warning,
     * and the candidates of that rule then take the fallback of their kind.
     *
     * @param ruleKey the key of the rule, including its repository
     * @param decisionJson the {@code <rule>_decision.json} resource of the plugin; {@code null} is reported as a missing specification
     */
    void addDecisionRule(RuleKey ruleKey, URL decisionJson);
  }
}
