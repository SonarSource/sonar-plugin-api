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
package org.sonar.api.utils.log;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.sonar.api.testutils.log.LogTester;

public class Slf4jLoggerTest {

  @Rule
  public LogTester tester = new LogTester();
  Slf4jLogger underTest = new Slf4jLogger(LoggerFactory.getLogger(getClass()));

  @Test
  public void log() {
    // no assertions. Simply verify that calls do not fail.
    underTest.trace("message");
    underTest.trace("message {}", "foo");
    underTest.trace("message {} {}", "foo", "bar");
    underTest.trace("message {} {} {}", "foo", "bar", "baz");

    underTest.debug("message");
    underTest.debug("message {}", "foo");
    underTest.debug("message {} {}", "foo", "bar");
    underTest.debug("message {} {} {}", "foo", "bar", "baz");

    underTest.info("message");
    underTest.info("message {}", "foo");
    underTest.info("message {} {}", "foo", "bar");
    underTest.info("message {} {} {}", "foo", "bar", "baz");

    underTest.warn("message");
    underTest.warn("message {}", "foo");
    underTest.warn("message {} {}", "foo", "bar");
    underTest.warn("message {} {} {}", "foo", "bar", "baz");
    underTest.warn("message", new NullPointerException("boom!"));

    underTest.error("message");
    underTest.error("message {}", "foo");
    underTest.error("message {} {}", "foo", "bar");
    underTest.error("message {} {} {}", "foo", "bar", "baz");
    underTest.error("message", new IllegalArgumentException(""));
  }

}
