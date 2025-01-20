/*
 * Sonar Plugin API
 * Copyright (C) 2009-2025 SonarSource SA
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
package org.sonar.api.testfixtures.log;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.event.Level;
import org.sonar.api.utils.log.LoggerLevel;
import org.sonar.api.utils.log.Loggers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class LogTesterTest {
  private final LogTester underTest = new LogTester();

  @Before
  public void prepare() {
    underTest.before();
  }

  @After
  public void reset_level() {
    underTest.after();
  }

  @Test
  public void info_level_by_default() {
    // when LogTester is used, then info logs are enabled by default
    assertThat(underTest.getLevel()).isEqualTo(LoggerLevel.INFO);
  }

  @Test
  public void should_change_default_level_before_test() {
    // Change default level to DEBUG instead of INFO by default
    LogTester underTest = new LogTester().setLevel(Level.DEBUG);
    // First test
    underTest.before();
    assertThat(underTest.getLevel()).isEqualTo(LoggerLevel.DEBUG);
    // changing level during a test
    underTest.setLevel(LoggerLevel.ERROR);
  }

  @Test
  public void change_log_level() {
    // change level
    underTest.setLevel(Level.DEBUG);
    assertThat(underTest.getLevel()).isEqualTo(LoggerLevel.DEBUG);
  }

  @Test
  public void intercept_sonar_logs_with_throwables() {
    Exception e = new IllegalStateException("exception msg");
    Loggers.get("logger2").error("error1: {}", 42, e);
    assertThat(underTest.getLogs()).extracting(LogAndArguments::getFormattedMsg, l -> l.getThrowable().getClass().getName(), l -> l.getThrowable().getMessage())
      .containsOnly(tuple("error1: 42", "java.lang.IllegalStateException", "exception msg"));
  }

  @Test
  public void intercept_slf4j_logs_with_throwables() {
    Exception e = new IllegalStateException("exception msg");
    org.slf4j.LoggerFactory.getLogger("logger1").error("error1: {}", 42, e);
    assertThat(underTest.getLogs()).extracting(LogAndArguments::getFormattedMsg, l -> l.getThrowable().getClass().getName(), l -> l.getThrowable().getMessage())
      .containsOnly(tuple("error1: 42", "java.lang.IllegalStateException", "exception msg"));
  }

  @Test
  public void intercept_slf4j_debug_logs() {
    org.slf4j.LoggerFactory.getLogger("logger1").debug("debug0: {}", 41);

    underTest.setLevel(Level.DEBUG);
    org.slf4j.LoggerFactory.getLogger("logger1").debug("debug1: {}", 42);
    assertThat(underTest.getLogs()).extracting(LogAndArguments::getFormattedMsg).containsOnly("debug1: 42");
    assertThat(underTest.getLogs(Level.DEBUG)).extracting(LogAndArguments::getFormattedMsg).containsOnly("debug1: 42");
  }

  @Test
  public void intercept_sonar_logs() {
    Loggers.get("logger1").info("an information");
    Loggers.get("logger2").warn("warning: {}", 42);

    assertThat(underTest.logs()).containsExactly("an information", "warning: 42");
    assertThat(underTest.getLogs()).extracting(LogAndArguments::getRawMsg, LogAndArguments::getFormattedMsg, l -> l.getArgs().map(List::of).orElse(null))
      .containsExactly(
        tuple("an information", "an information", null),
        tuple("warning: {}", "warning: 42", List.of(42)));

    assertThat(underTest.logs(Level.ERROR)).isEmpty();
    assertThat(underTest.getLogs(Level.ERROR)).isEmpty();

    assertThat(underTest.logs(Level.INFO)).containsOnly("an information");
    assertThat(underTest.getLogs(Level.INFO)).extracting(LogAndArguments::getRawMsg, LogAndArguments::getFormattedMsg, l -> l.getArgs().map(List::of).orElse(null))
      .containsExactly(
        tuple("an information", "an information", null));

    assertThat(underTest.logs(Level.WARN)).containsOnly("warning: 42");
    assertThat(underTest.getLogs(Level.WARN)).extracting(LogAndArguments::getRawMsg, LogAndArguments::getFormattedMsg, l -> l.getArgs().map(List::of).orElse(null))
      .containsExactly(
        tuple("warning: {}", "warning: 42", List.of(42)));

    underTest.clear();
    assertThat(underTest.logs()).isEmpty();
    assertThat(underTest.logs(Level.INFO)).isEmpty();
  }

  @Test
  public void use_suppliers() {
    // when LogTester is used, then info logs are enabled by default
    AtomicBoolean touchedTrace = new AtomicBoolean();
    AtomicBoolean touchedDebug = new AtomicBoolean();
    Loggers.get("logger1").trace(() -> {
      touchedTrace.set(true);
      return "a trace information";
    });
    Loggers.get("logger1").debug(() -> {
      touchedDebug.set(true);
      return "a debug information";
    });

    assertThat(underTest.logs()).isEmpty();
    assertThat(touchedTrace.get()).isFalse();
    assertThat(touchedDebug.get()).isFalse();

    // change level to DEBUG
    underTest.setLevel(Level.DEBUG);
    Loggers.get("logger1").trace(() -> {
      touchedTrace.set(true);
      return "a trace information";
    });
    Loggers.get("logger1").debug(() -> {
      touchedDebug.set(true);
      return "a debug information";
    });

    assertThat(underTest.logs()).containsOnly("a debug information");
    assertThat(touchedTrace.get()).isFalse();
    assertThat(touchedDebug.get()).isTrue();
    touchedDebug.set(false);
    underTest.clear();

    // change level to TRACE
    underTest.setLevel(Level.TRACE);
    Loggers.get("logger1").trace(() -> {
      touchedTrace.set(true);
      return "a trace information";
    });
    Loggers.get("logger1").debug(() -> {
      touchedDebug.set(true);
      return "a debug information";
    });

    assertThat(underTest.logs()).containsExactly("a trace information", "a debug information");
    assertThat(touchedTrace.get()).isTrue();
    assertThat(touchedDebug.get()).isTrue();
  }
}
