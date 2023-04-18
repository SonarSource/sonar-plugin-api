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
package org.sonar.api.web;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServletFilterTest {

  @Test
  public void filter_should_return_url_pattern() {
    ServletFilter filter = new FakeFilter();
    assertThat(filter.doGetPattern()).isNotNull();
  }

  @Test
  public void filter_should_apply_to_all_urls_by_default() {
    ServletFilter filter = new DefaultFilter();
    assertThat(filter.doGetPattern().matches("/")).isTrue();
    assertThat(filter.doGetPattern().matches("/foo/bar")).isTrue();
  }

  private static class FakeFilter extends ServletFilter {
    @Override
    public UrlPattern doGetPattern() {
      return UrlPattern.create("/fake");
    }

    public void init(FilterConfig filterConfig) {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
    }

    public void destroy() {
    }
  }

  private static class DefaultFilter extends ServletFilter {
    public void init(FilterConfig filterConfig) {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
    }

    public void destroy() {
    }
  }
}
