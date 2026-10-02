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

class OsTest {

  @RegisterExtension
  LogTesterJUnit5 logTester = new LogTesterJUnit5().setLevel(Level.WARN);

  @Test
  void of_shouldMapToCorrectOs() {
    assertThat(Os.of("win")).isEqualTo(Os.WINDOWS);
    assertThat(Os.of("windows")).isEqualTo(Os.WINDOWS);
    assertThat(Os.of("win32")).isEqualTo(Os.WINDOWS);
    assertThat(Os.of("linux")).isEqualTo(Os.LINUX);
    assertThat(Os.of("alpine")).isEqualTo(Os.ALPINE);
    assertThat(Os.of("mac")).isEqualTo(Os.MACOS);
    assertThat(Os.of("macos")).isEqualTo(Os.MACOS);
    assertThat(Os.of("darwin")).isEqualTo(Os.MACOS);
    assertThat(Os.of("zos")).isEqualTo(Os.ZOS);
    assertThat(Os.of("unknown")).isEqualTo(Os.UNKNOWN);

    assertThat(logTester.logs(Level.WARN)).isEmpty();
  }

  @Test
  void of_shouldBeCaseInsensitiveAndTrimInput() {
    assertThat(Os.of("Windows")).isEqualTo(Os.WINDOWS);
    assertThat(Os.of("LINUX")).isEqualTo(Os.LINUX);
    assertThat(Os.of(" Darwin ")).isEqualTo(Os.MACOS);
    assertThat(Os.of("\tAlpine\n")).isEqualTo(Os.ALPINE);

    assertThat(logTester.logs(Level.WARN)).isEmpty();
  }

  @Test
  void of_shouldReturnUnknown_whenValueIsNotRecognized() {
    assertThat(Os.of("solaris")).isEqualTo(Os.UNKNOWN);
    assertThat(Os.of("freebsd")).isEqualTo(Os.UNKNOWN);
  }

  @Test
  void of_shouldReturnUnknown_whenValueIsNullOrBlank() {
    assertThat(Os.of(null)).isEqualTo(Os.UNKNOWN);
    assertThat(Os.of("")).isEqualTo(Os.UNKNOWN);
    assertThat(Os.of("   ")).isEqualTo(Os.UNKNOWN);
  }

  @Test
  void of_shouldLogWarning_whenValueIsNotRecognized() {
    Os.of("solaris");

    assertThat(logTester.logs(Level.WARN)).containsExactly("Unrecognized operating system 'solaris', falling back to UNKNOWN");
  }

  @Test
  void of_shouldNotLogWarning_whenValueIsNullOrBlank() {
    Os.of(null);
    Os.of("");
    Os.of("   ");

    assertThat(logTester.logs(Level.WARN)).isEmpty();
  }

  @Test
  void values_shouldContainExpectedConstants() {
    assertThat(Os.values()).containsExactly(Os.WINDOWS, Os.LINUX, Os.ALPINE, Os.MACOS, Os.ZOS, Os.UNKNOWN);
  }
}
