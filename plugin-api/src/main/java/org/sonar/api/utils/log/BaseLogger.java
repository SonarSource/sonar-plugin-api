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

import javax.annotation.Nullable;

/**
 * @deprecated should not be used
 */
@Deprecated(since = "9.13")
abstract class BaseLogger implements Logger {
  @Override
  public void trace(String msg) {
    doTrace(msg);
  }

  @Override
  public void trace(String pattern, @Nullable Object arg) {
    doTrace(pattern, arg);
  }

  @Override
  public void trace(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    doTrace(msg, arg1, arg2);
  }

  @Override
  public void trace(String msg, Object... args) {
    doTrace(msg, args);
  }

  @Override
  public void debug(String msg) {
    doDebug(msg);
  }

  @Override
  public void debug(String pattern, @Nullable Object arg) {
    doDebug(pattern, arg);
  }

  @Override
  public void debug(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    doDebug(msg, arg1, arg2);
  }

  @Override
  public void debug(String msg, Object... args) {
    doDebug(msg, args);
  }

  @Override
  public void info(String msg) {
    doInfo(msg);
  }

  @Override
  public void info(String msg, @Nullable Object arg) {
    doInfo(msg, arg);
  }

  @Override
  public void info(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    doInfo(msg, arg1, arg2);
  }

  @Override
  public void info(String msg, Object... args) {
    doInfo(msg, args);
  }

  @Override
  public void warn(String msg) {
    doWarn(msg);
  }

  @Override
  public void warn(String msg, Throwable throwable) {
    doWarn(msg, throwable);
  }

  @Override
  public void warn(String msg, @Nullable Object arg) {
    doWarn(msg, arg);
  }

  @Override
  public void warn(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    doWarn(msg, arg1, arg2);
  }

  @Override
  public void warn(String msg, Object... args) {
    doWarn(msg, args);
  }

  @Override
  public void error(String msg) {
    doError(msg);
  }

  @Override
  public void error(String msg, @Nullable Object arg) {
    doError(msg, arg);
  }

  @Override
  public void error(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    doError(msg, arg1, arg2);
  }

  @Override
  public void error(String msg, Object... args) {
    doError(msg, args);
  }

  @Override
  public void error(String msg, Throwable thrown) {
    doError(msg, thrown);
  }

  abstract void doTrace(String msg);

  abstract void doTrace(String msg, @Nullable Object arg);

  abstract void doTrace(String msg, @Nullable Object arg1, @Nullable Object arg2);

  abstract void doTrace(String msg, Object... args);

  abstract void doDebug(String msg);

  abstract void doDebug(String msg, @Nullable Object arg);

  abstract void doDebug(String msg, @Nullable Object arg1, @Nullable Object arg2);

  abstract void doDebug(String msg, Object... args);

  abstract void doInfo(String msg);

  abstract void doInfo(String msg, @Nullable Object arg);

  abstract void doInfo(String msg, @Nullable Object arg1, @Nullable Object arg2);

  abstract void doInfo(String msg, Object... args);

  abstract void doWarn(String msg);

  abstract void doWarn(String msg, Throwable thrown);

  abstract void doWarn(String msg, @Nullable Object arg);

  abstract void doWarn(String msg, @Nullable Object arg1, @Nullable Object arg2);

  abstract void doWarn(String msg, Object... args);

  abstract void doError(String msg);

  abstract void doError(String msg, @Nullable Object arg);

  abstract void doError(String msg, @Nullable Object arg1, @Nullable Object arg2);

  abstract void doError(String msg, Object... args);

  abstract void doError(String msg, Throwable thrown);

  void log(LoggerLevel level, String msg) {
    switch (level) {
      case TRACE:
        trace(msg);
        break;
      case DEBUG:
        debug(msg);
        break;
      case INFO:
        info(msg);
        break;
      case WARN:
        warn(msg);
        break;
      case ERROR:
        error(msg);
        break;
      default:
        throw new IllegalArgumentException("Unsupported LoggerLevel value: " + level);
    }
  }
}
