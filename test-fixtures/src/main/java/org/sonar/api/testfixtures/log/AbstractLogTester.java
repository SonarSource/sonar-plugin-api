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

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.sonar.api.utils.log.LoggerLevel;

class AbstractLogTester<G extends AbstractLogTester<G>> {

  private static final Map<LoggerLevel, Level> sonarToSlf4jLevel = Map.of(
    LoggerLevel.TRACE, Level.TRACE,
    LoggerLevel.DEBUG, Level.DEBUG,
    LoggerLevel.INFO, Level.INFO,
    LoggerLevel.WARN, Level.WARN,
    LoggerLevel.ERROR, Level.ERROR
  );

  private static final Map<Level, LoggerLevel> slf4jToSonarLevel = Map.of(
    Level.TRACE, LoggerLevel.TRACE,
    Level.DEBUG, LoggerLevel.DEBUG,
    Level.INFO, LoggerLevel.INFO,
    Level.WARN, LoggerLevel.WARN,
    Level.ERROR, LoggerLevel.ERROR
  );

  private final ConcurrentListAppender<ILoggingEvent> listAppender = new ConcurrentListAppender<>();

  protected AbstractLogTester() {
    setLevel(Level.INFO);
  }

  protected void before() {
    getRootLogger().addAppender(listAppender);
    listAppender.start();
  }

  protected void after() {
    listAppender.stop();
    listAppender.list.clear();
    getRootLogger().detachAppender(listAppender);
    // Reset the level for following-up test suites
    setLevel(Level.INFO);
  }

  LoggerLevel getLevel() {
    return slf4jToSonarLevel.get(toSlf4j(getRootLogger().getLevel()));
  }

  private static Level toSlf4j(ch.qos.logback.classic.Level level) {
    var slf4jIntLevel = ch.qos.logback.classic.Level.toLocationAwareLoggerInteger(level);
    return Arrays.stream(Level.values()).filter(l -> l.toInt() == slf4jIntLevel).findFirst().orElseThrow(() -> new IllegalArgumentException("Unsupported level: " + level));
  }

  /**
   * Change log level.
   * By default, INFO logs are enabled when LogTester is started.
   */
  public G setLevel(Level level) {
    getRootLogger().setLevel(ch.qos.logback.classic.Level.fromLocationAwareLoggerInteger(level.toInt()));
    return (G) this;
  }

  /**
   * @deprecated use {@link #setLevel(Level)}
   */
  @Deprecated(since = "9.15")
  public G setLevel(LoggerLevel sonarLevel) {
    return setLevel(sonarToSlf4jLevel.get(sonarLevel));

  }

  private static ch.qos.logback.classic.Logger getRootLogger() {
    return (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
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
  @Deprecated(since = "9.15")
  public List<String> logs(LoggerLevel sonarLevel) {
    return logs(sonarToSlf4jLevel.get(sonarLevel));
  }

  /**
   * Logs with arguments in chronological order (item at index 0 is the oldest one)
   */
  public List<LogAndArguments> getLogs() {
    return listAppender.list.stream().map(e -> (LoggingEvent) e)
      .map(AbstractLogTester::convert)
      .collect(Collectors.toList());
  }

  /**
   * Logs with arguments in chronological order (item at index 0 is the oldest one) for
   * a given level
   */
  public List<LogAndArguments> getLogs(Level level) {
    return listAppender.list.stream().map(e -> (LoggingEvent) e)
      .filter(e -> e.getLevel().equals(ch.qos.logback.classic.Level.fromLocationAwareLoggerInteger(level.toInt())))
      .map(AbstractLogTester::convert)
      .collect(Collectors.toList());
  }

  /**
   * @deprecated use {@link #getLogs(Level)}
   */
  @Deprecated(since = "9.15")
  public List<LogAndArguments> getLogs(LoggerLevel sonarLevel) {
    return getLogs(sonarToSlf4jLevel.get(sonarLevel));
  }

  private static LogAndArguments convert(LoggingEvent e) {
    return new LogAndArguments(e.getFormattedMessage(), e.getMessage(), e.getThrowableProxy(), e.getArgumentArray());
  }

  public G clear() {
    listAppender.list.clear();
    return (G) this;
  }
}
