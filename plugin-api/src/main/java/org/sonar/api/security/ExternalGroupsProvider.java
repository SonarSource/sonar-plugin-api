/*
 * Sonar Plugin API
 * Copyright (C) 2009-2025 SonarSource SA
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

import java.util.Collection;
import javax.annotation.CheckForNull;
import org.sonar.api.server.http.HttpRequest;

/**
 * Note that prefix "do" for names of methods is reserved for future enhancements, thus should not be used in subclasses.
 *
 * @see SecurityRealm
 * @since 2.14
 */
public abstract class ExternalGroupsProvider {

  /**
   * Override this method in order to load user group information.
   *
   * @return list of groups associated with specified user, or null if such user doesn't exist
   * @throws RuntimeException in case of unexpected error such as connection failure
   * @since 5.2
   */
  @CheckForNull
  public Collection<String> doGetGroups(Context context) {
    return null;
  }

  public static final class Context {
    private String username;
    private HttpRequest httpRequest;

    /**
     * This class is not meant to be instantiated by plugins, except for tests.
     */
    public Context(String username, HttpRequest httpRequest) {
      this.username = username;
      this.httpRequest = httpRequest;
    }

    public String getUsername() {
      return username;
    }

    /**
     * @since 9.16
     */
    public HttpRequest getHttpRequest() {
      return httpRequest;
    }
  }
}
