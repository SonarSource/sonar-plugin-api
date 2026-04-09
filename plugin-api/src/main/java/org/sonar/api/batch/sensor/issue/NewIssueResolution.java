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
package org.sonar.api.batch.sensor.issue;

import java.util.Collection;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.rule.RuleKey;

/**
 * Builder for an {@link IssueResolution}. Use {@code context.newIssueResolution()} to create.
 * {@link #save()} should be called once all parameters are provided.
 *
 * @since 13.5
 */
public interface NewIssueResolution {

  /**
   * Set the target file.
   */
  NewIssueResolution on(InputFile inputFile);

  /**
   * Set the target text range within the file.
   */
  NewIssueResolution at(TextRange textRange);

  /**
   * Set the resolution status. Defaults to {@link IssueResolution.Status#DEFAULT}.
   */
  NewIssueResolution status(IssueResolution.Status status);

  /**
   * Set the target rule keys. At least one rule key is required.
   */
  NewIssueResolution forRules(Collection<RuleKey> ruleKeys);

  /**
   * Set a justification comment. Required.
   */
  NewIssueResolution comment(String comment);

  /**
   * Save the issue resolution.
   */
  void save();
}
