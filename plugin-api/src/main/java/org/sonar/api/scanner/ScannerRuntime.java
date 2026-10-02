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
 * is to select the platform-specific flavour of a resource to download through {@link ResourceFetcher}. Such code
 * must map {@link Os#ALPINE} to the Linux artifact, and must cope with a platform that was not detected, since
 * {@link #getOs()} and {@link #getArch()} then return their {@code UNKNOWN} constant:
 * </p>
 * <pre>
 * public class MySensor implements Sensor {
 *
 *   private final ResourceFetcher resourceFetcher;
 *
 *   public MySensor(ResourceFetcher resourceFetcher) {
 *     this.resourceFetcher = resourceFetcher;
 *   }
 *
 *   public void execute(SensorContext context) {
 *     if (!context.runtime().getApiVersion().isGreaterThanOrEqual(Version.create(14, 2))) {
 *       return;
 *     }
 *     ScannerRuntime scannerRuntime = context.scannerRuntime();
 *     String osDir = switch (scannerRuntime.getOs()) {
 *       case LINUX, ALPINE -&gt; "linux";
 *       case WINDOWS -&gt; "windows";
 *       case MACOS -&gt; "macos";
 *       case ZOS, UNKNOWN -&gt; null;
 *     };
 *     if (osDir == null || scannerRuntime.getArch() == Arch.UNKNOWN) {
 *       return;
 *     }
 *     String path = osDir + "-" + scannerRuntime.getArch().name().toLowerCase(Locale.ROOT) + "/native-tool";
 *     Path binary = resourceFetcher.fetch("mypluginkey", path);
 *     // ... run binary
 *   }
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
   * A {@link ScannerRuntime} that reports no information: {@link #getOs()} returns {@link Os#UNKNOWN} and
   * {@link #getArch()} returns {@link Arch#UNKNOWN}. It is returned by
   * {@link org.sonar.api.batch.sensor.SensorContext#scannerRuntime()} when the implementation does not provide
   * this information.
   */
  ScannerRuntime UNKNOWN = new ScannerRuntime() {
    @Override
    public Os getOs() {
      return Os.UNKNOWN;
    }

    @Override
    public Arch getArch() {
      return Arch.UNKNOWN;
    }
  };

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
