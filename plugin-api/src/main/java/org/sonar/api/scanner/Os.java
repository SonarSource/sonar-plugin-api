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

import java.util.Locale;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.Beta;

/**
 * Operating system of the machine running the scanner, as detected while provisioning the Java runtime.
 * This API is experimental and can be changed or dropped at any time.
 *
 * @see ScannerRuntime#getOs()
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

  private static final Logger LOGGER = LoggerFactory.getLogger(Os.class);

  /**
   * Maps a raw operating system name, typically the value of the {@code sonar.scanner.os} property, to an {@link Os}.
   * The comparison is case-insensitive and ignores surrounding whitespace. The accepted values are the ones of the
   * scanner bootstrapping contract: {@code win}, {@code windows} and {@code win32} map to {@link #WINDOWS},
   * {@code linux} to {@link #LINUX}, {@code alpine} to {@link #ALPINE}, {@code mac}, {@code macos} and
   * {@code darwin} to {@link #MACOS}, and {@code zos} to {@link #ZOS}.
   * <p>
   * The property is not validated by the scanner, so this method never fails: any other value, including
   * {@code null} and blank values, is mapped to {@link #UNKNOWN}.
   * </p>
   *
   * @param os the raw operating system name, may be {@code null}
   * @return the matching operating system, or {@link #UNKNOWN} if the value is not recognized
   */
  public static Os of(@Nullable String os) {
    if (os == null || os.isBlank()) {
      return UNKNOWN;
    }
    switch (os.trim().toLowerCase(Locale.ROOT)) {
      case "win", "windows", "win32":
        return WINDOWS;
      case "linux":
        return LINUX;
      case "alpine":
        return ALPINE;
      case "mac", "macos", "darwin":
        return MACOS;
      case "zos":
        return ZOS;
      case "unknown":
        return UNKNOWN;
      default:
        LOGGER.warn("Unrecognized operating system '{}', falling back to {}", os, UNKNOWN);
        return UNKNOWN;
    }
  }
}
