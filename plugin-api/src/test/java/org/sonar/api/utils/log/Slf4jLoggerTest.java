/*
 * Sonar Plugin API
 * Copyright (C) 2009-2023 SonarSource SA
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
package org.sonar.api.utils.log;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.sonar.api.testfixtures.log.LogTester;

import static org.assertj.core.api.Assertions.assertThat;

public class Slf4jLoggerTest {

  private Slf4jLogger underTest = new Slf4jLogger(LoggerFactory.getLogger(getClass()));

  @Rule
  public LogTester tester = new LogTester();

  @Test
  public void log() {
    tester.setLevel(LoggerLevel.TRACE);

    underTest.trace("trace message");
    underTest.trace("trace message {}", "foo");
    underTest.trace("trace message {} {}", "foo", "bar");
    underTest.trace("trace message {} {} {}", "foo", "bar", "baz");
    underTest.log(LoggerLevel.TRACE, "trace message");

    underTest.debug("debug message");
    underTest.debug("debug message {}", "foo");
    underTest.debug("debug message {} {}", "foo", "bar");
    underTest.debug("debug message {} {} {}", "foo", "bar", "baz");
    underTest.log(LoggerLevel.DEBUG, "debug message");

    underTest.info("info message");
    underTest.info("info message {}", "foo");
    underTest.info("info message {} {}", "foo", "bar");
    underTest.info("info message {} {} {}", "foo", "bar", "baz");
    underTest.log(LoggerLevel.INFO, "info message");

    underTest.warn("warn message");
    underTest.warn("warn message {}", "foo");
    underTest.warn("warn message {} {}", "foo", "bar");
    underTest.warn("warn message {} {} {}", "foo", "bar", "baz");
    underTest.warn("warn message", new NullPointerException("boom!"));
    underTest.log(LoggerLevel.WARN, "warn message");

    underTest.error("error message");
    underTest.error("error message {}", "foo");
    underTest.error("error message {} {}", "foo", "bar");
    underTest.error("error message {} {} {}", "foo", "bar", "baz");
    underTest.error("error message", new IllegalArgumentException(""));
    underTest.log(LoggerLevel.ERROR, "error message");

    assertThat(tester.logs(LoggerLevel.TRACE)).containsExactly(
      "trace message",
      "trace message foo",
      "trace message foo bar",
      "trace message foo bar baz",
      "trace message");
    assertThat(tester.logs(LoggerLevel.DEBUG)).containsExactly(
      "debug message",
      "debug message foo",
      "debug message foo bar",
      "debug message foo bar baz",
      "debug message");
    assertThat(tester.logs(LoggerLevel.INFO)).containsExactly(
      "info message",
      "info message foo",
      "info message foo bar",
      "info message foo bar baz",
      "info message");
    assertThat(tester.logs(LoggerLevel.WARN)).containsExactly(
      "warn message",
      "warn message foo",
      "warn message foo bar",
      "warn message foo bar baz",
      "warn message",
      "warn message");
    assertThat(tester.logs(LoggerLevel.ERROR)).containsExactly(
      "error message",
      "error message foo",
      "error message foo bar",
      "error message foo bar baz",
      "error message",
      "error message");
  }

  @Test
  public void testGetLevel() {
    tester.setLevel(Level.TRACE);
    assertThat(underTest.getLevel()).isEqualTo(LoggerLevel.TRACE);
    tester.setLevel(Level.DEBUG);
    assertThat(underTest.getLevel()).isEqualTo(LoggerLevel.DEBUG);
    tester.setLevel(Level.INFO);
    assertThat(underTest.getLevel()).isEqualTo(LoggerLevel.INFO);
    tester.setLevel(Level.WARN);
    assertThat(underTest.getLevel()).isEqualTo(LoggerLevel.WARN);
    tester.setLevel(Level.ERROR);
    assertThat(underTest.getLevel()).isEqualTo(LoggerLevel.ERROR);
  }

  @Test
  public void change_level_unsupported() {
    assertThat(underTest.setLevel(LoggerLevel.ERROR)).isFalse();
  }
}
