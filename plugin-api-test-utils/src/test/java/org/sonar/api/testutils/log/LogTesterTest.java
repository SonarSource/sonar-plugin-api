/*
 * Sonar Plugin API
 * Copyright (C) 2009-2022 SonarSource SA
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
package org.sonar.api.testutils.log;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.sonar.api.utils.log.Loggers;

import static org.assertj.core.api.Assertions.assertThat;

public class LogTesterTest {

  LogTester underTest = new LogTester();

  @Test
  public void info_level_by_default() throws Throwable {
    // when LogTester is used, then info logs are enabled by default
    underTest.before();

    LoggerFactory.getLogger("test").info("Some info");
    LoggerFactory.getLogger("test").debug("Some debug");

    assertThat(underTest.logs()).containsOnly("Some info");
    assertThat(LoggerFactory.getLogger("test").isDebugEnabled()).isFalse();

    underTest.clear();

    // change
    underTest.setLevel(Level.DEBUG);

    LoggerFactory.getLogger("test").info("Some info");
    LoggerFactory.getLogger("test").debug("Some debug");

    assertThat(underTest.logs()).containsOnly("Some info", "Some debug");
    assertThat(LoggerFactory.getLogger("test").isDebugEnabled()).isTrue();

    // reset to initial level after execution of test
    underTest.after();
    assertThat(LoggerFactory.getLogger("test").isDebugEnabled()).isFalse();
  }

  @Test
  public void intercept_logs() throws Throwable {
    underTest.before();
    LoggerFactory.getLogger("logger1").info("an information");
    LoggerFactory.getLogger("logger2").warn("warning: {}", 42);

    assertThat(underTest.logs()).containsExactly("an information", "warning: 42");
    assertThat(underTest.logs(Level.ERROR)).isEmpty();
    assertThat(underTest.logs(Level.INFO)).containsOnly("an information");
    assertThat(underTest.logs(Level.WARN)).containsOnly("warning: 42");

    underTest.clear();
    assertThat(underTest.logs()).isEmpty();
    assertThat(underTest.logs(Level.INFO)).isEmpty();

    underTest.after();
  }

}
