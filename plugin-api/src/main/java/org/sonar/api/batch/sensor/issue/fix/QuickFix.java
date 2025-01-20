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
package org.sonar.api.batch.sensor.issue.fix;


import java.util.List;

/**
 * Represents a quick fix for an {@link org.sonar.api.batch.sensor.issue.Issue}, with a description and a collection of {@link InputFileEdit}.
 * @since 9.13
 */
public interface QuickFix {

  /**
   * @return the message for this quick fix, which will be shown to the user as an action item.
   */
  String message();

  /**
   * Create a new input file edit
   * @return the list of input file edits for this quick fix
   */
  List<InputFileEdit> inputFileEdits();
}
