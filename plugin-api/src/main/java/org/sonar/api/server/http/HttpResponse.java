/*
 * Sonar Plugin API
 * Copyright (C) 2009-2025 SonarSource Sàrl
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
import java.io.OutputStream;
import java.io.PrintWriter;
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
   * Sets the content type of the response being sent to the client, if the response has not been committed yet.
   */
  void setContentType(String contentType);

  /**
   * Returns a <code>PrintWriter</code> object that can send character text to the client. Calling flush()
   * on the <code>PrintWriter</code> commits the response.
   */
  PrintWriter getWriter() throws IOException;

  /**
   * Sets a response header with the given name and value. If the header had already been set, the new value overwrites
   * the previous one.
   */
  void setHeader(String name, String value);

  /**
   * Sends a temporary redirect response to the client using the specified redirect location URL and clears the buffer.
   * The buffer will be replaced with the data set by this method.
   */
  void sendRedirect(String location) throws IOException;

  /**
   * Adds the specified cookie to the response.  This method can be called
   * multiple times to set more than one cookie.
   */
  void addCookie(Cookie cookie);

  /**
   * Returns a {@link java.io.OutputStream} suitable for writing binary
   * data in the response. The servlet container does not encode the
   * binary data.
   * Either this method or {@link #getWriter} may
   * be called to write the body, not both.
   */
  OutputStream getOutputStream() throws IOException;

  /**
   * Sets the character encoding (MIME charset) of the response
   * being sent to the client, for example, to UTF-8.
   */
  void setCharacterEncoding(String charset);
}
