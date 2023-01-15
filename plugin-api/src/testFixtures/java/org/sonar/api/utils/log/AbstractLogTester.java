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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

class AbstractLogTester<G extends AbstractLogTester> {

  private final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

  protected void before() {
    getRootLogger().addAppender(listAppender);
    listAppender.start();
    setLevel(Level.INFO);
  }

  protected void after() {
    listAppender.stop();
    listAppender.list.clear();
    getRootLogger().detachAppender(listAppender);
    setLevel(Level.INFO);
  }

  Level getLevel() {
    return Level.intToLevel(ch.qos.logback.classic.Level.toLocationAwareLoggerInteger(getRootLogger().getLevel()));
  }

  /**
   * Enable/disable debug logs. Info, warn and error logs are always enabled.
   * By default, INFO logs are enabled when LogTester is started.
   */
  public G setLevel(Level level) {
    getRootLogger().setLevel(ch.qos.logback.classic.Level.fromLocationAwareLoggerInteger(level.toInt()));
    return (G) this;
  }

  /**
   * @deprecated use {@link #setLevel(Level)}
   */
  @Deprecated(since = "9.13")
  public G setLevel(LoggerLevel sonarLevel) {
    return setLevel(toSlf4jLevel(sonarLevel));

  }

  private static Logger getRootLogger() {
    return (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
  }

  /**
   * Logs in chronological order (item at index 0 is the oldest one)
   */
  public List<String> logs() {
    return listAppender.list.stream().map(e -> (LoggingEvent) e)
      .map(LoggingEvent::getFormattedMessage)
      .collect(Collectors.toList());
  }

  /**
   * Logs in chronological order (item at index 0 is the oldest one) for
   * a given level
   */
  public List<String> logs(Level level) {
    return listAppender.list.stream().map(e -> (LoggingEvent) e)
      .filter(e -> e.getLevel().equals(ch.qos.logback.classic.Level.fromLocationAwareLoggerInteger(level.toInt())))
      .map(LoggingEvent::getFormattedMessage)
      .collect(Collectors.toList());
  }

  /**
   * @deprecated use {@link #logs(Level)}
   */
  @Deprecated(since = "9.13")
  public List<String> logs(LoggerLevel sonarLevel) {
    return logs(toSlf4jLevel(sonarLevel));
  }

  private Level toSlf4jLevel(LoggerLevel sonarLevel) {
    switch (sonarLevel) {
      case TRACE:
        return Level.TRACE;
      case DEBUG:
        return Level.DEBUG;
      case INFO:
        return Level.INFO;
      case WARN:
        return Level.WARN;
      case ERROR:
        return Level.ERROR;
      default:
        throw new IllegalArgumentException("Unsupported level: " + sonarLevel);
    }
  }

  public G clear() {
    listAppender.list.clear();
    return (G) this;
  }
}
