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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.System2;

class DefaultProfiler extends Profiler {

  private static final String CONTEXT_SEPARATOR = " | ";
  private static final String DONE_SUFFIX = " (done)";

  private final LinkedHashMap<String, Object> context = new LinkedHashMap<>();
  private final Logger logger;

  private long startTime = 0L;
  private String startMessage = null;

  public DefaultProfiler(Logger logger) {
    this.logger = logger;
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  @Override
  public Profiler start() {
    this.startTime = System2.INSTANCE.now();
    this.startMessage = null;
    return this;
  }

  @Override
  public Profiler startTrace(String message) {
    this.startTime = System2.INSTANCE.now();
    this.startMessage = message;
    StringBuilder sb = new StringBuilder();
    sb.append(message);
    appendContext(sb);
    logger.trace(sb.toString());
    return this;
  }

  @Override
  public Profiler startDebug(String message) {
    this.startTime = System2.INSTANCE.now();
    this.startMessage = message;
    StringBuilder sb = new StringBuilder();
    sb.append(message);
    appendContext(sb);
    logger.debug(sb.toString());
    return this;
  }

  @Override
  public Profiler startInfo(String message) {
    this.startTime = System2.INSTANCE.now();
    this.startMessage = message;
    StringBuilder sb = new StringBuilder();
    sb.append(message);
    appendContext(sb);
    logger.info(sb.toString());
    return this;
  }


  @Override
  public Profiler stopTrace() {
    return doStopWithoutMessage(logger::trace, DONE_SUFFIX);
  }

  @Override
  public Profiler stopDebug() {
    return doStopWithoutMessage(logger::debug, DONE_SUFFIX);
  }

  @Override
  public Profiler stopInfo() {
    return stopInfo(false);
  }
  

  @Override
  public Profiler stopInfo(boolean cacheUsed) {
    String suffix = cacheUsed ? " (done from cache)" : DONE_SUFFIX;
    return doStopWithoutMessage(logger::info, suffix);
  }

  private Profiler doStopWithoutMessage(Consumer<String> logMethodToUse, String suffix) {
    if (startMessage == null) {
      throw new IllegalStateException("Profiler#stopXXX() can't be called without any message defined in start methods");
    }
    doStop(logMethodToUse, startMessage, suffix);
    return this;
  }

  @Override
  public Profiler stopTrace(String message) {
    doStop(logger::trace, message, "");
    return this;
  }

  @Override
  public Profiler stopDebug(String message) {
    doStop(logger::debug, message, "");
    return this;
  }

  @Override
  public Profiler stopInfo(String message) {
    doStop(logger::info, message, "");
    return this;
  }

  private void doStop(Consumer<String> logMethodToUse, @Nullable String message, String messageSuffix) {
    if (startTime == 0L) {
      throw new IllegalStateException("Profiler must be started before being stopped");
    }
    long duration = System2.INSTANCE.now() - startTime;
    StringBuilder sb = new StringBuilder();
    if (!StringUtils.isEmpty(message)) {
      sb.append(message);
      sb.append(messageSuffix);
      sb.append(CONTEXT_SEPARATOR);
    }
    sb.append("time=").append(duration).append("ms");
    appendContext(sb);
    logMethodToUse.accept(sb.toString());
    startTime = 0L;
    startMessage = null;
    context.clear();
  }

  private void appendContext(StringBuilder sb) {
    for (Map.Entry<String, Object> entry : context.entrySet()) {
      if (sb.length() > 0) {
        sb.append(CONTEXT_SEPARATOR);
      }
      sb.append(entry.getKey()).append("=").append(entry.getValue());
    }
  }

  @Override
  public Profiler addContext(String key, @Nullable Object value) {
    if (value == null) {
      context.remove(key);
    } else {
      context.put(key, value);
    }
    return this;
  }

}
