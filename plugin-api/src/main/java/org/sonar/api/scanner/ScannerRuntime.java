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
 * Information about the machine running the scanner. It exposes the operating system and the CPU architecture that
 * were already detected while provisioning the Java runtime, so that analyzers do not have to implement their own
 * platform detection.
 *
 * <p>
 * It can be injected in the constructor of any scanner-side extension, and {@link org.sonar.api.batch.sensor.Sensor}
 * extensions can also get it from {@link org.sonar.api.batch.sensor.SensorContext#scannerRuntime()}. A typical usage
 * is to select the platform-specific flavour of a resource to download through {@link ResourceFetcher}:
 * </p>
 * <pre>
 * public void execute(SensorContext context) {
 *   Os os = context.scannerRuntime().getOs();
 *   Arch arch = context.scannerRuntime().getArch();
 *   String path = os.name().toLowerCase(Locale.ROOT) + "-" + arch.name().toLowerCase(Locale.ROOT) + "/native-tool";
 *   Path binary = resourceFetcher.fetch("mypluginkey", path);
 *   // ... run binary
 * }
 * </pre>
 *
 * This API is experimental and can be changed or dropped at any time.
 *
 * @since 14.2
 */
@Beta
@ScannerSide
public interface ScannerRuntime {

  /**
   * Operating system of the machine running the scanner.
   *
   * @return the detected operating system, or {@link Os#UNKNOWN} if it could not be determined. Never {@code null}.
   */
  Os getOs();

  /**
   * CPU architecture of the machine running the scanner.
   *
   * @return the detected architecture, or {@link Arch#UNKNOWN} if it could not be determined. Never {@code null}.
   */
  Arch getArch();

}
