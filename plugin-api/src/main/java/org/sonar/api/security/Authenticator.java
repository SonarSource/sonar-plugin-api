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
package org.sonar.api.security;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import org.sonar.api.ExtensionPoint;
import org.sonar.api.server.ServerSide;
import org.sonar.api.server.http.HttpRequest;

import static java.util.Objects.requireNonNull;

/**
 * @see SecurityRealm
 * @since 3.1
 */
@ServerSide
@ExtensionPoint
public abstract class Authenticator {

  /**
   * @return true if user was successfully authenticated with specified credentials, false otherwise
   * @throws RuntimeException in case of unexpected error such as connection failure
   */
  public abstract boolean doAuthenticate(Context context);

  public static final class Context {
    private String username;
    private String password;
    private HttpServletRequest request;
    private HttpRequest httpRequest;

    /**
     * This class is not meant to be instantiated by plugins, except for tests.
     */
    public Context(@Nullable String username, @Nullable String password, HttpRequest httpRequest, HttpServletRequest request) {
      requireNonNull(request);
      this.username = username;
      this.password = password;
      this.httpRequest = httpRequest;
      this.request = request;
    }

    /**
     * Username can be null, for example when using <a href="http://www.jasig.org/cas">CAS</a>.
     */
    public String getUsername() {
      return username;
    }

    /**
     * Password can be null, for example when using <a href="http://www.jasig.org/cas">CAS</a>.
     */
    public String getPassword() {
      return password;
    }

    /**
     * @deprecated since 9.16. Use {@link #getHttpRequest()} instead.
     */
    @Deprecated(since = "9.16", forRemoval = true)
    public HttpServletRequest getRequest() {
      return request;
    }

    /**
     * @since 9.16
     */
    public HttpRequest getHttpRequest() {
      return httpRequest;
    }
  }
}
