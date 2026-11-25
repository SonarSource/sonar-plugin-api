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
package org.sonar.api.batch.sensor.decision.internal;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.decision.NewIssueCandidate;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.batch.sensor.issue.fix.NewQuickFix;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rule.RuleKey;

/**
 * Implementation of the {@code SensorContext} defaults for engines without decision support: the facts are ignored, and
 * {@link #save()} saves the wrapped issue unless the candidate is required.
 * Every {@link NewIssue} method must be overridden and return this candidate, so that the {@code save()} ending a chain
 * always runs here. A test checks it, default methods included.
 */
public final class FallbackIssueCandidate implements NewIssueCandidate {
  private final NewIssue issue;
  private final boolean required;

  public FallbackIssueCandidate(NewIssue issue, boolean required) {
    this.issue = issue;
    this.required = required;
  }

  @Override
  public NewIssueCandidate addFact(String name, String value) {
    return this;
  }

  @Override
  public NewIssueCandidate setFacts(Map<String, String> facts) {
    return this;
  }

  @Override
  public void save() {
    if (!required) {
      issue.save();
    }
  }

  @Override
  public NewIssueCandidate forRule(RuleKey ruleKey) {
    issue.forRule(ruleKey);
    return this;
  }

  @Override
  public NewIssueCandidate gap(@Nullable Double gap) {
    issue.gap(gap);
    return this;
  }

  @Override
  public NewIssueCandidate overrideSeverity(@Nullable Severity severity) {
    issue.overrideSeverity(severity);
    return this;
  }

  @Override
  public NewIssueCandidate overrideImpact(SoftwareQuality softwareQuality, org.sonar.api.issue.impact.Severity severity) {
    issue.overrideImpact(softwareQuality, severity);
    return this;
  }

  @Override
  public NewIssueCandidate at(NewIssueLocation primaryLocation) {
    issue.at(primaryLocation);
    return this;
  }

  @Override
  public NewIssueCandidate addLocation(NewIssueLocation secondaryLocation) {
    issue.addLocation(secondaryLocation);
    return this;
  }

  @Override
  public NewIssueCandidate setQuickFixAvailable(boolean quickFixAvailable) {
    issue.setQuickFixAvailable(quickFixAvailable);
    return this;
  }

  @Override
  public NewIssueCandidate addFlow(Iterable<NewIssueLocation> flowLocations) {
    issue.addFlow(flowLocations);
    return this;
  }

  @Override
  public NewIssueCandidate addFlow(Iterable<NewIssueLocation> flowLocations, FlowType flowType, @Nullable String flowDescription) {
    issue.addFlow(flowLocations, flowType, flowDescription);
    return this;
  }

  @Override
  public NewIssueLocation newLocation() {
    return issue.newLocation();
  }

  @Override
  public NewQuickFix newQuickFix() {
    return issue.newQuickFix();
  }

  @Override
  public NewIssueCandidate addQuickFix(NewQuickFix newQuickFix) {
    issue.addQuickFix(newQuickFix);
    return this;
  }

  @Override
  public NewIssueCandidate setRuleDescriptionContextKey(@Nullable String ruleDescriptionContextKey) {
    issue.setRuleDescriptionContextKey(ruleDescriptionContextKey);
    return this;
  }

  @Override
  public NewIssueCandidate setCodeVariants(@Nullable Iterable<String> codeVariants) {
    issue.setCodeVariants(codeVariants);
    return this;
  }

  @Override
  public NewIssueCandidate addInternalTag(String tag) {
    issue.addInternalTag(tag);
    return this;
  }

  @Override
  public NewIssueCandidate addInternalTags(Collection<String> tags) {
    issue.addInternalTags(tags);
    return this;
  }

  @Override
  public NewIssueCandidate setInternalTags(@Nullable Collection<String> tags) {
    issue.setInternalTags(tags);
    return this;
  }
}
