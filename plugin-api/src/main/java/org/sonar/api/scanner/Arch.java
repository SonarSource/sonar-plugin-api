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
 * CPU architecture of the machine running the scanner, as detected while provisioning the Java runtime.
 * This API is experimental and can be changed or dropped at any time.
 *
 * @see ScannerRuntime#getArch()
 * @since 14.2
 */
@Beta
public enum Arch {

  X64,
  AARCH64,
  S390X,
  PPC64LE,

  /**
   * The architecture could not be determined, for example because the scanner engine did not report it or
   * reported a value this version of the API does not know about.
   */
  UNKNOWN;

  private static final Logger LOGGER = LoggerFactory.getLogger(Arch.class);

  /**
   * Maps a raw architecture name, typically the value of the {@code sonar.scanner.arch} property, to an
   * {@link Arch}. The comparison is case-insensitive and ignores surrounding whitespace. The accepted values are the
   * ones of the scanner bootstrapping contract: {@code x86_64}, {@code x86-64}, {@code amd64} and {@code x64} map to
   * {@link #X64}, and {@code arm64} and {@code aarch64} to {@link #AARCH64}. In addition, {@code s390x} maps to
   * {@link #S390X} and {@code ppc64le} to {@link #PPC64LE}.
   * <p>
   * The property is not validated by the scanner, so this method never fails: any other value, including
   * {@code null} and blank values, is mapped to {@link #UNKNOWN}.
   * </p>
   *
   * @param arch the raw architecture name, may be {@code null}
   * @return the matching architecture, or {@link #UNKNOWN} if the value is not recognized
   */
  public static Arch of(@Nullable String arch) {
    if (arch == null || arch.isBlank()) {
      return UNKNOWN;
    }
    switch (arch.trim().toLowerCase(Locale.ROOT)) {
      case "x86_64", "x86-64", "amd64", "x64":
        return X64;
      case "arm64", "aarch64":
        return AARCH64;
      case "s390x":
        return S390X;
      case "ppc64le":
        return PPC64LE;
      case "unknown":
        return UNKNOWN;
      default:
        LOGGER.warn("Unrecognized architecture '{}', falling back to {}", arch, UNKNOWN);
        return UNKNOWN;
    }
  }
}
