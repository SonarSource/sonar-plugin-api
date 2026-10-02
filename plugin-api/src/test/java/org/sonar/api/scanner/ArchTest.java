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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.event.Level;
import org.sonar.api.testfixtures.log.LogTesterJUnit5;

import static org.assertj.core.api.Assertions.assertThat;

class ArchTest {

  @RegisterExtension
  LogTesterJUnit5 logTester = new LogTesterJUnit5().setLevel(Level.WARN);

  @Test
  void of_shouldMapToCorrectArch() {
    assertThat(Arch.of("x86_64")).isEqualTo(Arch.X64);
    assertThat(Arch.of("x86-64")).isEqualTo(Arch.X64);
    assertThat(Arch.of("amd64")).isEqualTo(Arch.X64);
    assertThat(Arch.of("x64")).isEqualTo(Arch.X64);
    assertThat(Arch.of("arm64")).isEqualTo(Arch.AARCH64);
    assertThat(Arch.of("aarch64")).isEqualTo(Arch.AARCH64);
    assertThat(Arch.of("s390x")).isEqualTo(Arch.S390X);
    assertThat(Arch.of("ppc64le")).isEqualTo(Arch.PPC64LE);
    assertThat(Arch.of("unknown")).isEqualTo(Arch.UNKNOWN);

    assertThat(logTester.logs(Level.WARN)).isEmpty();
  }

  @Test
  void of_shouldBeCaseInsensitiveAndTrimInput() {
    assertThat(Arch.of("X86_64")).isEqualTo(Arch.X64);
    assertThat(Arch.of("AArch64")).isEqualTo(Arch.AARCH64);
    assertThat(Arch.of(" ppc64le ")).isEqualTo(Arch.PPC64LE);
    assertThat(Arch.of("\tS390X\n")).isEqualTo(Arch.S390X);

    assertThat(logTester.logs(Level.WARN)).isEmpty();
  }

  @Test
  void of_shouldReturnUnknown_whenValueIsNotRecognized() {
    assertThat(Arch.of("sparc")).isEqualTo(Arch.UNKNOWN);
    assertThat(Arch.of("riscv64")).isEqualTo(Arch.UNKNOWN);
  }

  @Test
  void of_shouldReturnUnknown_whenValueIsNullOrBlank() {
    assertThat(Arch.of(null)).isEqualTo(Arch.UNKNOWN);
    assertThat(Arch.of("")).isEqualTo(Arch.UNKNOWN);
    assertThat(Arch.of("   ")).isEqualTo(Arch.UNKNOWN);
  }

  @Test
  void of_shouldLogWarning_whenValueIsNotRecognized() {
    Arch.of("sparc");

    assertThat(logTester.logs(Level.WARN)).containsExactly("Unrecognized architecture 'sparc', falling back to UNKNOWN");
  }

  @Test
  void of_shouldNotLogWarning_whenValueIsNullOrBlank() {
    Arch.of(null);
    Arch.of("");
    Arch.of("   ");

    assertThat(logTester.logs(Level.WARN)).isEmpty();
  }

  @Test
  void values_shouldContainExpectedConstants() {
    assertThat(Arch.values()).containsExactly(Arch.X64, Arch.AARCH64, Arch.S390X, Arch.PPC64LE, Arch.UNKNOWN);
  }
}
