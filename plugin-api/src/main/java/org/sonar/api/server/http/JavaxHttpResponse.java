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

import java.io.IOException;
import java.util.Collection;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of {@link HttpResponse} based on a delegate of {@link HttpServletResponse} from the Javax Servlet API.
 */
public class JavaxHttpResponse implements HttpResponse {

  private final HttpServletResponse delegate;

  public JavaxHttpResponse(HttpServletResponse delegate) {
    this.delegate = delegate;
  }

  @Override
  public void addHeader(String name, String value) {
    delegate.addHeader(name, value);
  }

  @Override
  public String getHeader(String name) {
    return delegate.getHeader(name);
  }

  @Override
  public Collection<String> getHeaders(String name) {
    return delegate.getHeaders(name);
  }

  @Override
  public void setStatus(int status) {
    delegate.setStatus(status);
  }

  @Override
  public int getStatus() {
    return delegate.getStatus();
  }

  @Override
  public void sendRedirect(String location) throws IOException {
    delegate.sendRedirect(location);
  }

  @Override
  public HttpServletResponse getRawResponse() {
    return delegate;
  }
}
