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

import org.sonar.api.ExtensionPoint;
import org.sonar.api.server.ServerSide;

/**
 * Plugins can implement filters to serve HTTP requests that would otherwise not be served by the platform. This extension point
 * is not intended to intercept all requests.
 * When the platform intercepts and serves a request (for example with a web service filter), plugin filters do not get invoked,
 * as the filters defined by plugins are at the end of the filter chain.
 * Plugin filters get invoked when no other filters are interrupting the chain and returning a response to the request.
 * {@code @deprecated} since 9.16. Use {@link org.sonar.api.web.HttpFilter} instead.
 *
 * @since 3.1
 * @deprecated since 9.16. Use {@link org.sonar.api.web.HttpFilter} instead.
 */
@ServerSide
@ExtensionPoint
@Deprecated(forRemoval = true, since = "9.16")
public abstract class ServletFilter implements javax.servlet.Filter {

  /**
   * Override to change URL. Default is /*
   */
  public UrlPattern doGetPattern() {
    return UrlPattern.builder().build();
  }

  public static final class UrlPattern extends AbstractUrlPattern {

    private UrlPattern(Builder builder) {
      super(builder);
    }

    /**
     * Defines only a single inclusion pattern. This is a shortcut for {@code builder().includes(inclusionPattern).build()}.
     */
    public static UrlPattern create(String inclusionPattern) {
      return builder().includes(inclusionPattern).build();
    }

    /**
     * @since 6.0
     */
    public static Builder builder() {
      return new Builder();
    }

    /**
     * @since 6.0
     */
    public static class Builder extends AbstractUrlPattern.Builder<UrlPattern, Builder> {

      private Builder() {
        super();
      }

      @Override
      public UrlPattern build() {
        return new UrlPattern(this);
      }
    }
  }
}
