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

import java.util.Map;
import org.sonar.api.batch.sensor.decision.NewIssueCandidate;
import org.sonar.api.batch.sensor.issue.NewIssue;

import static java.util.Objects.requireNonNull;
import static org.sonar.api.utils.Preconditions.checkArgument;

/**
 * Implementation of the {@code SensorContext} defaults for engines without decision support: the facts are validated and ignored, and
 * {@link #save()} saves the wrapped issue unless the candidate is required.
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
    checkArgument(name != null && !name.isBlank(), "Fact name can't be blank");
    requireNonNull(value, () -> "Value of fact '" + name + "' can't be null");
    return this;
  }

  @Override
  public NewIssueCandidate setFacts(Map<String, String> facts) {
    requireNonNull(facts, "Facts can't be null");
    facts.forEach(this::addFact);
    return this;
  }

  @Override
  public void save() {
    if (!required) {
      issue.save();
    }
  }
}
