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
package org.sonar.api.utils.log;

import javax.annotation.Nullable;

/**
 * Implements Sonar logging facade on top of slf4j
 */
public class Slf4jLogger implements Logger {

  private final org.slf4j.Logger slf4jLogger;

  protected Slf4jLogger(org.slf4j.Logger slf4jLogger) {
    this.slf4jLogger = slf4jLogger;
  }

  @Override
  public boolean isTraceEnabled() {
    return slf4jLogger.isTraceEnabled();
  }

  @Override
  public void trace(String msg) {
    slf4jLogger.trace(msg);
  }

  @Override
  public void trace(String msg, @Nullable Object arg) {
    slf4jLogger.trace(msg, arg);
  }

  @Override
  public void trace(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.trace(msg, arg1, arg2);
  }

  @Override
  public void trace(String msg, Object... args) {
    slf4jLogger.trace(msg, args);
  }

  @Override
  public boolean isDebugEnabled() {
    return slf4jLogger.isDebugEnabled();
  }


  @Override
  public void debug(String msg) {
    slf4jLogger.debug(msg);
  }

  @Override
  public void debug(String msg, @Nullable Object arg) {
    slf4jLogger.debug(msg, arg);
  }

  @Override
  public void debug(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.debug(msg, arg1, arg2);
  }

  @Override
  public void debug(String msg, Object... args) {
    slf4jLogger.debug(msg, args);
  }

  @Override
  public void info(String msg) {
    slf4jLogger.info(msg);
  }

  @Override
  public void info(String msg, @Nullable Object arg) {
    slf4jLogger.info(msg, arg);
  }

  @Override
  public void info(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.info(msg, arg1, arg2);
  }

  @Override
  public void info(String msg, Object... args) {
    slf4jLogger.info(msg, args);
  }

  @Override
  public void warn(String msg) {
    slf4jLogger.warn(msg);
  }

  @Override
  public void warn(String msg, Throwable thrown) {
    slf4jLogger.warn(msg, thrown);
  }

  @Override
  public void warn(String msg, @Nullable Object arg) {
    slf4jLogger.warn(msg, arg);
  }

  @Override
  public void warn(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.warn(msg, arg1, arg2);
  }

  @Override
  public void warn(String msg, Object... args) {
    slf4jLogger.warn(msg, args);
  }

  @Override
  public void error(String msg) {
    slf4jLogger.error(msg);
  }

  @Override
  public void error(String msg, @Nullable Object arg) {
    slf4jLogger.error(msg, arg);
  }

  @Override
  public void error(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.error(msg, arg1, arg2);
  }

  @Override
  public void error(String msg, Object... args) {
    slf4jLogger.error(msg, args);
  }

  @Override
  public void error(String msg, Throwable thrown) {
    slf4jLogger.error(msg, thrown);
  }

  @Override
  public boolean setLevel(LoggerLevel level) {
    return false;
  }

  @Override
  public LoggerLevel getLevel() {
    if (slf4jLogger.isTraceEnabled()) {
      return LoggerLevel.TRACE;
    } else if (slf4jLogger.isDebugEnabled()) {
      return LoggerLevel.DEBUG;
    } else if (slf4jLogger.isInfoEnabled()) {
      return LoggerLevel.INFO;
    } else if (slf4jLogger.isWarnEnabled()) {
      return LoggerLevel.WARN;
    }
    return LoggerLevel.ERROR;
  }

}
