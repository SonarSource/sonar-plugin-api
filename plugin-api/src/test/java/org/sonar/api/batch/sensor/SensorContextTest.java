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
package org.sonar.api.batch.sensor;

import org.junit.jupiter.api.Test;
import org.sonar.api.scanner.Arch;
import org.sonar.api.scanner.Os;
import org.sonar.api.scanner.ScannerRuntime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class SensorContextTest {

  @Test
  void scannerRuntime_shouldReturnUnknown_whenNotImplemented() {
    SensorContext underTest = mock(SensorContext.class, CALLS_REAL_METHODS);

    assertThat(underTest.scannerRuntime()).isSameAs(ScannerRuntime.UNKNOWN);
    assertThat(underTest.scannerRuntime().getOs()).isEqualTo(Os.UNKNOWN);
    assertThat(underTest.scannerRuntime().getArch()).isEqualTo(Arch.UNKNOWN);
  }

  @Test
  void scannerRuntime_shouldReturnValue_whenImplemented() {
    ScannerRuntime scannerRuntime = new TestScannerRuntime();
    SensorContext underTest = mock(SensorContext.class, CALLS_REAL_METHODS);
    doReturn(scannerRuntime).when(underTest).scannerRuntime();

    assertThat(underTest.scannerRuntime()).isSameAs(scannerRuntime);
    assertThat(underTest.scannerRuntime().getOs()).isEqualTo(Os.LINUX);
    assertThat(underTest.scannerRuntime().getArch()).isEqualTo(Arch.AARCH64);
  }

  private static class TestScannerRuntime implements ScannerRuntime {
    @Override
    public Os getOs() {
      return Os.LINUX;
    }

    @Override
    public Arch getArch() {
      return Arch.AARCH64;
    }
  }
}
