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

/**
 * Framework-agnostic definition of an HTTP response.
 *
 * @since 9.16
 */
public interface HttpResponse {

  /**
   * Adds a response header with the given name and value. This method allows response headers to have multiple values.
   */
  void addHeader(String name, String value);

  /**
   * Gets the value of the response header with the given name.
   * If a response header with the given name exists and contains multiple values, the value that was added first will be returned.
   */
  String getHeader(String name);

  /**
   * Gets the values of the response header with the given name.
   */
  Collection<String> getHeaders(String name);

  /**
   * Sets the status code for this response.
   */
  void setStatus(int sc);

  /**
   * Gets the current status code of this response.
   */
  int getStatus();

  /**
   * Sends a temporary redirect response to the client using the specified redirect location URL and clears the buffer.
   * The buffer will be replaced with the data set by this method.
   */
  void sendRedirect(String location) throws IOException;

  /**
   * Returns the raw response object from the Servlet API that matches this response.
   */
  Object getRawResponse();
}
