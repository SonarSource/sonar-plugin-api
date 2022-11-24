/*
 * Sonar Plugin API
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

/**
 * Represents the data of the formatted part of the text in the issue message
 * @since 9.13
 */
public interface MessageFormatting {

  enum Type {
    CODE
  }

  /**
   * The index of the first character in the string that will be formatted
   */
  int start();

  /**
   * The index of the last character in the string that will be formatted
   */
  int end();

  /**
   * Type of the text that will be formatted
   */
  Type type();
}
