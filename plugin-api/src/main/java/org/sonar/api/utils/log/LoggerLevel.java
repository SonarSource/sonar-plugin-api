/*
 * Sonar Plugin API
 * Copyright (C) 2009-2023 SonarSource SA
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
package org.sonar.api.utils.log;

import java.util.Arrays;
import org.slf4j.event.Level;

public enum LoggerLevel {
  TRACE(Level.TRACE),
  DEBUG(Level.DEBUG),
  INFO(Level.INFO),
  WARN(Level.WARN),
  ERROR(Level.ERROR);

  private final Level slf4jLevel;

  LoggerLevel(Level slf4jLevel) {
    this.slf4jLevel = slf4jLevel;
  }

  /**
   * @since 9.15
   */
  public static LoggerLevel fromSlf4j(Level slf4jLevel) {
    return Arrays.stream(LoggerLevel.values()).filter(l -> l.slf4jLevel == slf4jLevel).findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Unsupported level: " + slf4jLevel));
  }

  /**
   * @since 9.15
   */
  public Level toSlf4j() {
    return slf4jLevel;
  }
}
