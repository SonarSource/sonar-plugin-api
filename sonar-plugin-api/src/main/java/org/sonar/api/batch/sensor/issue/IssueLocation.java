/*
 * SonarQube
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
package org.sonar.api.batch.sensor.issue;

import javax.annotation.CheckForNull;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.fs.TextRange;

/**
 * Represents an issue location.
 *
 * @since 5.2
 */
public interface IssueLocation {

  /**
   * The {@link InputComponent} this location belongs to.
   */
  InputComponent inputComponent();

  /**
   * Range of the issue. Null for global issues and issues on directories. Can also be null
   * for files (issue global to the file).
   */
  @CheckForNull
  TextRange textRange();

  /**
   * Message of the issue.
   */
  @CheckForNull
  String message();

}
