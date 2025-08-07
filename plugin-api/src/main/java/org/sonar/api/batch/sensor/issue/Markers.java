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
package org.sonar.api.batch.sensor.issue;

public enum Markers {
  /**
   * SAST issue markers
   * <ul>
   *   <li>BASIC: issues detected by SAST without taint analysis</li>
   *   <li>TAINT: issues detected by taint analysis</li>
   *   <li>ADVANCED: issues detected by taint analysis including Advanced SAST (ASAST) elements in their flow.</li>
   * </ul>
   *
   * Note:
   * <ul>
   *   <li>BASIC and TAINT are mutually exclusive, i.e., any issued marked as BASIC should never be also marked as TAINT and vice versa.</li>
   *   <li>Issues marked as ADVANCED should always be a subset of issues marked as TAINT.</li>
   * </ul>
   */
  SAST_BASIC,
  SAST_TAINT,
  SAST_TAINT_ADVANCED
}
