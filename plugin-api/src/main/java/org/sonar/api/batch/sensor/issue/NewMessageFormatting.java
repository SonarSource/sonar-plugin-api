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
package org.sonar.api.batch.sensor.issue;

/**
 * Builder to create new MessageFormatting.
 * Should not be implemented by client.
 * @since 9.13
 */
public interface NewMessageFormatting {

  /**
   * @param index The index of the first character in the string that will be formatted (inclusive)
   * @return builder object
   */
  NewMessageFormatting start(int index);

  /**
   * @param index The index at which formatting will stop (exclusive)
   * @return builder object
   */
  NewMessageFormatting end(int index);

  /**
   * @param type Type of the text that will be formatted
   * @return builder object
   */
  NewMessageFormatting type(MessageFormatting.Type type);
}
