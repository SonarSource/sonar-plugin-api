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

import org.slf4j.Logger;
import javax.annotation.Nullable;

/**
 * Adapter of the deprecated {@link org.sonar.api.utils.log.Logger} interface
 * @deprecated should not be used
 */
@Deprecated(since = "9.13")
class Slf4jLogger extends BaseLogger {

  private final Logger slf4jLogger;

  Slf4jLogger(Logger slf4jLogger) {
    this.slf4jLogger = slf4jLogger;
  }

  @Override
  public boolean isTraceEnabled() {
    return slf4jLogger.isTraceEnabled();
  }

  @Override
  void doTrace(String msg) {
    slf4jLogger.trace(msg);
  }

  @Override
  void doTrace(String msg, @Nullable Object arg) {
    slf4jLogger.trace(msg, arg);
  }

  @Override
  void doTrace(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.trace(msg, arg1, arg2);
  }

  @Override
  void doTrace(String msg, Object... args) {
    slf4jLogger.trace(msg, args);
  }

  @Override
  public boolean isDebugEnabled() {
    return slf4jLogger.isDebugEnabled();
  }


  @Override
  protected void doDebug(String msg) {
    slf4jLogger.debug(msg);
  }

  @Override
  protected void doDebug(String msg, @Nullable Object arg) {
    slf4jLogger.debug(msg, arg);
  }

  @Override
  protected void doDebug(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.debug(msg, arg1, arg2);
  }

  @Override
  protected void doDebug(String msg, Object... args) {
    slf4jLogger.debug(msg, args);
  }

  @Override
  protected void doInfo(String msg) {
    slf4jLogger.info(msg);
  }

  @Override
  protected void doInfo(String msg, @Nullable Object arg) {
    slf4jLogger.info(msg, arg);
  }

  @Override
  protected void doInfo(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.info(msg, arg1, arg2);
  }

  @Override
  protected void doInfo(String msg, Object... args) {
    slf4jLogger.info(msg, args);
  }

  @Override
  protected void doWarn(String msg) {
    slf4jLogger.warn(msg);
  }

  @Override
  void doWarn(String msg, Throwable thrown) {
    slf4jLogger.warn(msg, thrown);
  }

  @Override
  protected void doWarn(String msg, @Nullable Object arg) {
    slf4jLogger.warn(msg, arg);
  }

  @Override
  protected void doWarn(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.warn(msg, arg1, arg2);
  }

  @Override
  protected void doWarn(String msg, Object... args) {
    slf4jLogger.warn(msg, args);
  }

  @Override
  protected void doError(String msg) {
    slf4jLogger.error(msg);
  }

  @Override
  protected void doError(String msg, @Nullable Object arg) {
    slf4jLogger.error(msg, arg);
  }

  @Override
  protected void doError(String msg, @Nullable Object arg1, @Nullable Object arg2) {
    slf4jLogger.error(msg, arg1, arg2);
  }

  @Override
  protected void doError(String msg, Object... args) {
    slf4jLogger.error(msg, args);
  }

  @Override
  protected void doError(String msg, Throwable thrown) {
    slf4jLogger.error(msg, thrown);
  }

  @Override
  public boolean setLevel(LoggerLevel level) {
    return false;
  }

  @Override
  public LoggerLevel getLevel() {
    return LoggerLevel.INFO;
  }

  Logger slf4jLogger() {
    return slf4jLogger;
  }
}
