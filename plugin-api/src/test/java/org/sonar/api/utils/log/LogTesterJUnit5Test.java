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

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Test;
import org.slf4j.event.Level;

import static org.assertj.core.api.Assertions.assertThat;

public class LogTesterJUnit5Test {

  LogTesterJUnit5 underTest = new LogTesterJUnit5();

  @Test
  public void info_level_by_default() throws Throwable {
    // when LogTester is used, then info logs are enabled by default
    underTest.beforeEach(null);
    assertThat(underTest.getLevel()).isEqualTo(Level.INFO);

    // change
    underTest.setLevel(Level.DEBUG);
    assertThat(underTest.getLevel()).isEqualTo(Level.DEBUG);

    // reset to initial level after execution of test
    underTest.afterEach(null);
    assertThat(underTest.getLevel()).isEqualTo(Level.INFO);
  }

  @Test
  public void intercept_logs() throws Throwable {
    underTest.beforeEach(null);
    Loggers.get("logger1").info("an information");
    Loggers.get("logger2").warn("warning: {}", 42);

    assertThat(underTest.logs()).containsExactly("an information", "warning: 42");
    assertThat(underTest.logs(Level.ERROR)).isEmpty();
    assertThat(underTest.logs(Level.INFO)).containsOnly("an information");
    assertThat(underTest.logs(Level.WARN)).containsOnly("warning: 42");

    underTest.clear();
    assertThat(underTest.logs()).isEmpty();
    assertThat(underTest.logs(Level.INFO)).isEmpty();

    underTest.afterEach(null);
  }

  @Test
  public void can_change_log_level() throws Throwable {
    underTest.beforeEach(null);
    Loggers.get("logger1").info("an information");
    Loggers.get("logger2").warn("warning: {}", 42);

    assertThat(underTest.logs()).containsExactly("an information", "warning: 42");

    underTest.clear();
    assertThat(underTest.logs()).isEmpty();

    underTest.setLevel(Level.WARN);
    Loggers.get("logger1").info("an information");
    Loggers.get("logger2").warn("warning: {}", 42);

    assertThat(underTest.logs()).containsExactly("warning: 42");

    underTest.afterEach(null);
  }

  @Test
  public void use_suppliers() throws Throwable {
    // when LogTester is used, then info logs are enabled by default
    underTest.beforeEach(null);
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
