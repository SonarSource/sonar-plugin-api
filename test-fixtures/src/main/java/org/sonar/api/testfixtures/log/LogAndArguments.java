/*
 * Sonar Plugin API
 * Copyright (C) 2009-2024 SonarSource SA
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

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import java.util.Arrays;
import java.util.Optional;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public final class LogAndArguments {
  private final String rawMsg;
  private final Throwable throwable;
  private final Object[] args;
  private final String msg;

  LogAndArguments(String msg, String rawMsg, @Nullable IThrowableProxy t, Object... args) {
    this.rawMsg = rawMsg;
    this.msg = msg;
    this.throwable = t instanceof ThrowableProxy ? ((ThrowableProxy) t).getThrowable() : null;
    this.args = args;
  }

  @CheckForNull
  public Throwable getThrowable() {
    return throwable;
  }

  public String getRawMsg() {
    return rawMsg;
  }

  public Optional<Object[]> getArgs() {
    return Optional.ofNullable(args);
  }

  public String getFormattedMsg() {
    return msg;
  }

  @Override
  public String toString() {
    String throwableStr = throwable != null ? (throwable.getClass().getName() + ": " + throwable.getMessage()) : null;
    return "LogAndArguments{" +
      "rawMsg='" + rawMsg + '\'' +
      ", args=" + Arrays.toString(args) +
      ", msg='" + msg + '\'' +
      ", throwable='" + throwableStr + '\'' +
      '}';
  }
}
