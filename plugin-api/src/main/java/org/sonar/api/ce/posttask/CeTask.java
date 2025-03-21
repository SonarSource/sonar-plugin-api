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
package org.sonar.api.ce.posttask;

/**
 * @since 5.5
 */
public interface CeTask {
  /**
   * Id of the Compute Engine task.
   * <p>
   * This is the id under which the processing of the project analysis report has been added to the Compute Engine
   * queue.
   * 
   */
  String getId();

  /**
   * Indicates whether the Compute Engine task ended successfully or not.
   */
  Status getStatus();

  enum Status {
    SUCCESS, FAILED
  }
}
