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

/**
 * Framework-agnostic definition of an HTTP request.
 *
 * @since 9.16
 */
public interface HttpRequest {


  /**
   * Returns the port number to which the request was sent.
   */
  int getServerPort();

  /**
   * Returns a boolean indicating whether this request was made using a secure channel, such as HTTPS.
   */
  boolean isSecure();

  /**
   * Returns the name of the scheme used to make this request, for example, http, https, or ftp.
   */
  String getScheme();

  /**
   * Returns the host name of the server to which the request was sent.
   */
  String getServerName();

  /**
   * Returns the URL the client used to make the request. The returned URL contains a protocol, server name, port number, and server path,
   * but it does not include query string parameters.
   */
  String getRequestURL();

  /**
   * Returns the part of this request's URL from the protocol name up to the query string in the first line of the HTTP request.
   */
  String getRequestURI();

  /**
   * Returns the query string that is contained in the request URL after the path. This method returns null if the URL does not have a
   * query string.
   */
  String getQueryString();

  /**
   * Returns the value of a request parameter as a String, or null if the parameter does not exist.
   * You should only use this method when you are sure the parameter has only one value. If the parameter might have more than one value,
   * use {@link #getParameterValues}.
   */
  String getParameter(String name);

  /**
   * Returns an array containing all of the values the given request parameter has, or null if the parameter does not exist.
   */
  String[] getParameterValues(String name);

  /**
   * Returns the value of the specified request header as a String. If the request did not include a header of the specified name, this
   * method returns null. If there are multiple headers with the same name, this method returns the first head in the request.
   */
  String getHeader(String name);

  /**
   * Returns all the values of the specified request header as an Enumeration of String objects.
   */
  Enumeration<String> getHeaders(String name);

  /**
   * Returns the name of the HTTP method with which this request was made, for example, GET, POST, or PUT.
   */
  String getMethod();

  /**
   * Returns the raw request object from the Servlet API that matches this request.
   */
  Object getRawRequest();
}
