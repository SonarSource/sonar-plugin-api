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
package org.sonar.api.scanner;

import org.sonar.api.Beta;

/**
 * Operating system of the machine running the scanner, as detected while provisioning the Java runtime.
 * This API is experimental and can be changed or dropped at any time.
 *
 * @see HostPlatform#getOs()
 * @since 14.2
 */
@Beta
public enum Os {

  WINDOWS,
  LINUX,

  /**
   * Alpine Linux, or another Linux distribution based on the musl C library. It is reported instead of
   * {@link #LINUX}, so an analyzer selecting a Linux artifact must handle both {@link #LINUX} and {@link #ALPINE}.
   */
  ALPINE,
  MACOS,
  ZOS,

  /**
   * The operating system could not be determined, for example because the scanner engine did not report it or
   * reported a value this version of the API does not know about.
   */
  UNKNOWN;
}
