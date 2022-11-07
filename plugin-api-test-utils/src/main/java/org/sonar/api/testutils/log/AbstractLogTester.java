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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;

class AbstractLogTester<G extends AbstractLogTester<G>> {

  private final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
  private final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

  protected void before() {
    // this shared instance breaks compatibility with parallel execution of tests
    logger.addAppender(listAppender);
    listAppender.start();
    setLevel(Level.INFO);
  }

  protected void after() {
    listAppender.stop();
    listAppender.list.clear();
    logger.detachAppender(listAppender);
    setLevel(Level.INFO);
  }

  /**
   * Enable/disable debug logs. Info, warn and error logs are always enabled.
   * By default INFO logs are enabled when LogTester is started.
   */
  public G setLevel(Level level) {
    Logger logbackLogger = (Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
    logbackLogger.setLevel(ch.qos.logback.classic.Level.toLevel(level.toString()));
    return (G) this;
  }

  /**
   * Logs in chronological order (item at index 0 is the oldest one)
   */
  public List<String> logs() {
    return listAppender.list.stream().map(ILoggingEvent::getFormattedMessage).collect(Collectors.toList());
  }

  /**
   * Logs in chronological order (item at index 0 is the oldest one) for
   * a given level
   */
  public List<String> logs(Level level) {
    return listAppender.list.stream()
      .filter(e -> e.getLevel() == ch.qos.logback.classic.Level.toLevel(level.toString()))
      .map(ILoggingEvent::getFormattedMessage)
      .collect(Collectors.toList());
  }


  public List<LoggingEvent> getLoggingEvents() {
    return listAppender.list.stream().map(LoggingEvent.class::cast).collect(Collectors.toList());
  }

  public List<LoggingEvent> getLoggingEvents(Level level) {
    return listAppender.list.stream()
      .filter(e -> e.getLevel() == ch.qos.logback.classic.Level.toLevel(level.toString()))
      .map(LoggingEvent.class::cast)
      .collect(Collectors.toList());
  }

  public G clear() {
    listAppender.list.clear();
    return (G) this;
  }
}
