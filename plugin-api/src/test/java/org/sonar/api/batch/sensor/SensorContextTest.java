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
import org.sonar.api.scanner.HostPlatform;
import org.sonar.api.scanner.Os;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class SensorContextTest {

  @Test
  void hostPlatform_shouldReturnUnknown_whenNotImplemented() {
    SensorContext underTest = mock(SensorContext.class, CALLS_REAL_METHODS);

    assertThat(underTest.hostPlatform()).isSameAs(HostPlatform.UNKNOWN);
    assertThat(underTest.hostPlatform().getOs()).isEqualTo(Os.UNKNOWN);
    assertThat(underTest.hostPlatform().getArch()).isEqualTo(Arch.UNKNOWN);
  }

  @Test
  void hostPlatform_shouldReturnValue_whenImplemented() {
    HostPlatform hostPlatform = new TestHostPlatform();
    SensorContext underTest = mock(SensorContext.class, CALLS_REAL_METHODS);
    doReturn(hostPlatform).when(underTest).hostPlatform();

    assertThat(underTest.hostPlatform()).isSameAs(hostPlatform);
    assertThat(underTest.hostPlatform().getOs()).isEqualTo(Os.LINUX);
    assertThat(underTest.hostPlatform().getArch()).isEqualTo(Arch.AARCH64);
  }

  private static class TestHostPlatform implements HostPlatform {
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
