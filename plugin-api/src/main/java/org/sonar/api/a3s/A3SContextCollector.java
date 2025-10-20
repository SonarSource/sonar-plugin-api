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
package org.sonar.api.a3s;

import java.nio.file.Path;
import java.util.Collection;
import org.sonar.api.Beta;
import org.sonar.api.scanner.ScannerSide;

/**
 * Reserved for SonarSource internal usage.
 * @since 13.4
 */
@Beta
@ScannerSide
public interface A3SContextCollector {

  /**
   * Check if the A3S context collector is enabled.
   */
  boolean isEnabled();

  /**
   * Collect a context for the current analysis. If called multiple times with the same kind, the behavior is unspecified.
   *
   * @param kind a unique identifier in case there are multiple contexts for the same project. Can be used to retrieve this specific context later.
   * @param metadata opaque metadata (for example in JSON format) describing the project
   * @param items attachments to the context, for more efficient storage. Currently only file items are supported.
   */
  void collect(String kind, String metadata, Collection<Item> items);

  Item newFileItem(String id, Path path);

  interface Item {

    String id();

  }

}
