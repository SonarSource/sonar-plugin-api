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
package org.sonar.api.batch.sensor;

import java.util.Collection;
import javax.annotation.Nullable;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.decision.NewIssueCandidate;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.batch.sensor.issue.fix.NewQuickFix;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rule.RuleKey;

/**
 * The candidate returned by the default {@link SensorContext} methods, on engines that do not support decisions. The facts are discarded.
 * The issue of an optional candidate is an ordinary issue; the issue of a required candidate is a {@link DroppedIssue}.
 */
final class FallbackIssueCandidate implements NewIssueCandidate {

  private final NewIssue issue;

  private FallbackIssueCandidate(NewIssue issue) {
    this.issue = issue;
  }

  static NewIssueCandidate optional(NewIssue issue) {
    return new FallbackIssueCandidate(issue);
  }

  static NewIssueCandidate required(NewIssue issue) {
    return new FallbackIssueCandidate(new DroppedIssue(issue));
  }

  @Override
  public NewIssueCandidate addFact(String name, String value) {
    return this;
  }

  @Override
  public NewIssue issue() {
    return issue;
  }

  /**
   * The issue of a required candidate: it forwards every call to a real issue, so that sensors get the same checks as on any engine, but
   * {@link #save()} does nothing. Every forwarded method returns this issue, never the real one, so that a chain ending in {@code save()}
   * never saves the real issue.
   */
  static final class DroppedIssue implements NewIssue {

    private final NewIssue issue;

    DroppedIssue(NewIssue issue) {
      this.issue = issue;
    }

    @Override
    public void save() {
      // A required candidate is dropped when decisions are not supported
    }

    @Override
    public NewIssue forRule(RuleKey ruleKey) {
      issue.forRule(ruleKey);
      return this;
    }

    @Override
    public NewIssue gap(@Nullable Double gap) {
      issue.gap(gap);
      return this;
    }

    @Override
    public NewIssue overrideSeverity(@Nullable Severity severity) {
      issue.overrideSeverity(severity);
      return this;
    }

    @Override
    public NewIssue overrideImpact(SoftwareQuality softwareQuality, org.sonar.api.issue.impact.Severity severity) {
      issue.overrideImpact(softwareQuality, severity);
      return this;
    }

    @Override
    public NewIssue at(NewIssueLocation primaryLocation) {
      issue.at(primaryLocation);
      return this;
    }

    @Override
    public NewIssue addLocation(NewIssueLocation secondaryLocation) {
      issue.addLocation(secondaryLocation);
      return this;
    }

    @Override
    public NewIssue setQuickFixAvailable(boolean quickFixAvailable) {
      issue.setQuickFixAvailable(quickFixAvailable);
      return this;
    }

    @Override
    public NewIssue addFlow(Iterable<NewIssueLocation> flowLocations) {
      issue.addFlow(flowLocations);
      return this;
    }

    @Override
    public NewIssue addFlow(Iterable<NewIssueLocation> flowLocations, FlowType flowType, @Nullable String flowDescription) {
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
    public NewIssue addQuickFix(NewQuickFix newQuickFix) {
      issue.addQuickFix(newQuickFix);
      return this;
    }

    @Override
    public NewIssue setRuleDescriptionContextKey(@Nullable String ruleDescriptionContextKey) {
      issue.setRuleDescriptionContextKey(ruleDescriptionContextKey);
      return this;
    }

    @Override
    public NewIssue setCodeVariants(@Nullable Iterable<String> codeVariants) {
      issue.setCodeVariants(codeVariants);
      return this;
    }

    @Override
    public NewIssue addInternalTag(String tag) {
      issue.addInternalTag(tag);
      return this;
    }

    @Override
    public NewIssue addInternalTags(Collection<String> tags) {
      issue.addInternalTags(tags);
      return this;
    }

    @Override
    public NewIssue setInternalTags(@Nullable Collection<String> tags) {
      issue.setInternalTags(tags);
      return this;
    }
  }
}
