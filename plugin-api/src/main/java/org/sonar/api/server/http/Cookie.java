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
package org.sonar.api.server.http;

/**
 * Framework-agnostic definition of a cookie.
 * Creates a cookie, a small amount of information sent by a servlet to
 * a Web browser, saved by the browser, and later sent back to the server.
 * A cookie's value can uniquely
 * identify a client, so cookies are commonly used for session management.
 *
 * @since 9.16
 **/
public interface Cookie {

  /**
   * Returns the name of the cookie. The name cannot be changed after
   * creation.
   */
  String getName();

  /**
   * Gets the current value of this Cookie.
   */
  String getValue();

  /**
   * Returns the path on the server
   * to which the browser returns this cookie. The
   * cookie is visible to all subpaths on the server.
   */
  String getPath();

  /**
   * Returns <code>true</code> if the browser is sending cookies
   * only over a secure protocol, or <code>false</code> if the
   * browser can send cookies using any protocol.
   */
  boolean isSecure();

  /**
   * Checks whether this Cookie has been marked as <i>HttpOnly</i>.
   */
  boolean isHttpOnly();

  /**
   * Gets the maximum age in seconds of this Cookie.
   */
  int getMaxAge();


}
