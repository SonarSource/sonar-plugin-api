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
package org.sonar.api.web;

import java.io.IOException;
import org.sonar.api.server.http.HttpRequest;
import org.sonar.api.server.http.HttpResponse;

/**
 * Filters use the FilterChain to invoke the next filter in the chain, or if the calling filter
 * is the last filter in the chain, to invoke the resource at the end of the chain.
 *
 * @see HttpFilter
 * @since 9.16
 **/
public interface FilterChain {

  /**
   * Causes the next filter in the chain to be invoked, or if the calling filter is the last filter
   * in the chain, causes the resource at the end of the chain to be invoked.
   *
   * @param request  the request to pass along the chain.
   * @param response the response to pass along the chain.
   */
  void doFilter(HttpRequest request, HttpResponse response) throws IOException;

}
