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

import java.io.IOException;
import org.junit.Test;
import org.sonar.api.server.http.HttpRequest;
import org.sonar.api.server.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpFilterTest {

  @Test
  public void filter_should_return_url_pattern() {
    HttpFilter filter = new FakeFilter();
    assertThat(filter.doGetPattern()).isNotNull();
  }

  @Test
  public void filter_should_apply_to_all_urls_by_default() {
    HttpFilter filter = new DefaultFilter();
    assertThat(filter.doGetPattern().matches("/")).isTrue();
    assertThat(filter.doGetPattern().matches("/foo/bar")).isTrue();
  }

  private static class FakeFilter extends HttpFilter {
    @Override
    public UrlPattern doGetPattern() {
      return UrlPattern.create("/fake");
    }

    @Override
    public void init() {

    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, org.sonar.api.web.FilterChain chain) {

    }

    public void destroy() {
    }
  }

  private static class DefaultFilter extends HttpFilter {
    @Override
    public void init() {

    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, org.sonar.api.web.FilterChain chain) throws IOException {

    }

    public void destroy() {
    }
  }
}
