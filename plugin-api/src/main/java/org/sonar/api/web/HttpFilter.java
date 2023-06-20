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
package org.sonar.api.web;

import java.io.IOException;
import org.sonar.api.ExtensionPoint;
import org.sonar.api.server.ServerSide;
import org.sonar.api.server.http.HttpRequest;
import org.sonar.api.server.http.HttpResponse;

/**
 * A filter is an object that performs filtering tasks on either the
 * request to a resource (a servlet or static content), or on the response
 * from a resource, or both.
 * This extension point is not intended to intercept all requests. When the
 * platform intercepts and serves a request (for example with a web service filter),
 * plugin filters do not get invoked, as the filters defined by plugins are at the end of the filter chain.
 *
 * @since 9.16
 */
@ServerSide
@ExtensionPoint
public abstract class HttpFilter {

  /**
   * This method is called exactly once after instantiating the filter. The init
   * method must complete successfully before the filter is asked to do any
   * filtering work.
   */
  public void init() {
  }

  /**
   * The <code>doFilter</code> method of the Filter is called by the
   * SonarQube each time a request/response pair is passed through the
   * chain due to a client request for a resource at the end of the chain.
   * The FilterChain passed in to this method allows the Filter to pass
   * on the request and response to the next entity in the chain.
   * <p>A typical implementation of this method would follow the following
   * pattern:
   * <ol>
   * <li>Examine the request
   * <li>Optionally wrap the request object with a custom implementation to
   * filter content or headers for input filtering
   * <li>Optionally wrap the response object with a custom implementation to
   * filter content or headers for output filtering
   * <li>
   * <ul>
   * <li><strong>Either</strong> invoke the next entity in the chain
   * using the FilterChain object
   * (<code>chain.doFilter()</code>),
   * <li><strong>or</strong> not pass on the request/response pair to
   * the next entity in the filter chain to
   * block the request processing
   * </ul>
   * <li>Directly set headers on the response after invocation of the
   * next entity in the filter chain.
   * </ol>
   */
  public abstract void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws IOException;

  /**
   * Called by the SonarQube to indicate to a filter that it is being
   * taken out of service.
   * <p>This method is only called once all threads within the filter's
   * doFilter method have exited or after a timeout period has passed.
   * After the web container calls this method, it will not call the
   * doFilter method again on this instance of the filter.
   * <p>This method gives the filter an opportunity to clean up any
   * resources that are being held (for example, memory, file handles,
   * threads) and make sure that any persistent state is synchronized
   * with the filter's current state in memory.
   */
  public void destroy() {
  }

  /**
   * Override to change URL. Default is /*
   */
  public UrlPattern doGetPattern() {
    return UrlPattern.builder().build();
  }
}
