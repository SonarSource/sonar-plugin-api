/*
 * Sonar Plugin API
 * Copyright (C) 2009-2025 SonarSource Sàrl
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

import ch.qos.logback.classic.spi.ThrowableProxy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogAndArgumentsTest {
  @Test
  public void toString_displays_null_throwable() {
    LogAndArguments logAndArguments = new LogAndArguments("msg", "raw", null, "arg1");
    assertThat(logAndArguments).hasToString("LogAndArguments{rawMsg='raw', args=[arg1], msg='msg', throwable='null'}");
  }

  @Test
  public void toString_includes_all_fields() {
    Throwable t = new IllegalStateException("tmsg");
    ThrowableProxy proxy = new ThrowableProxy(t);
    LogAndArguments logAndArguments = new LogAndArguments("msg", "raw", proxy, "arg1");
    assertThat(logAndArguments).hasToString("LogAndArguments{rawMsg='raw', args=[arg1], msg='msg', throwable='java.lang.IllegalStateException: tmsg'}");
  }
}
