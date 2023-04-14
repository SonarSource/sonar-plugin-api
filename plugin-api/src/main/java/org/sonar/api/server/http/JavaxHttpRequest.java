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
package org.sonar.api.server.http;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link HttpRequest} based on a delegate of {@link HttpServletRequest} from the Javax Servlet API.
 */
public class JavaxHttpRequest implements HttpRequest {

  private final HttpServletRequest delegate;

  public JavaxHttpRequest(HttpServletRequest delegate) {
    this.delegate = delegate;
  }

  @Override
  public int getServerPort() {
    return delegate.getServerPort();
  }

  @Override
  public boolean isSecure() {
    return delegate.isSecure();
  }

  @Override
  public String getScheme() {
    return delegate.getScheme();
  }

  @Override
  public String getServerName() {
    return delegate.getServerName();
  }

  @Override
  public String getRequestURL() {
    return delegate.getRequestURL().toString();
  }

  @Override
  public String getRequestURI() {
    return delegate.getRequestURI();
  }

  @Override
  public String getQueryString() {
    return delegate.getQueryString();
  }

  @Override
  public String getContextPath() {
    return delegate.getContextPath();
  }

  @Override
  public String getParameter(String name) {
    return delegate.getParameter(name);
  }

  @Override
  public String[] getParameterValues(String name) {
    return delegate.getParameterValues(name);
  }

  @Override
  public String getHeader(String name) {
    return delegate.getHeader(name);
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    return delegate.getHeaders(name);
  }

  @Override
  public String getMethod() {
    return delegate.getMethod();
  }

  @Override
  public HttpServletRequest getRawRequest() {
    return delegate;
  }
}
