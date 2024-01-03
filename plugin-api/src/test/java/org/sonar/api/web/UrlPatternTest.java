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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UrlPatternTest {

  @Test
  public void include_all() {
    UrlPattern pattern = UrlPattern.create("/*");
    assertThat(pattern.matches("/")).isTrue();
    assertThat(pattern.matches("/foo/ooo")).isTrue();

    assertThat(pattern.getInclusions()).containsOnly("/*");
    assertThat(pattern.getExclusions()).isEmpty();
  }

  @Test
  public void include_end_of_url() {
    UrlPattern pattern = UrlPattern.create("*foo");
    assertThat(pattern.matches("/")).isFalse();
    assertThat(pattern.matches("/hello/foo")).isTrue();
    assertThat(pattern.matches("/hello/bar")).isFalse();
    assertThat(pattern.matches("/foo")).isTrue();
    assertThat(pattern.matches("/foo2")).isFalse();
  }

  @Test
  public void include_beginning_of_url() {
    UrlPattern pattern = UrlPattern.create("/foo/*");
    assertThat(pattern.matches("/")).isFalse();
    assertThat(pattern.matches("/foo")).isTrue();
    assertThat(pattern.matches("/foo/bar")).isTrue();
    assertThat(pattern.matches("/bar")).isFalse();
  }

  @Test
  public void include_exact_url() {
    UrlPattern pattern = UrlPattern.create("/foo");
    assertThat(pattern.matches("/")).isFalse();
    assertThat(pattern.matches("/foo")).isTrue();
    assertThat(pattern.matches("/foo/")).isFalse();
    assertThat(pattern.matches("/bar")).isFalse();
  }

  @Test
  public void exclude_all() {
    UrlPattern pattern = UrlPattern.builder()
      .excludes("/*")
      .build();
    assertThat(pattern.matches("/")).isFalse();
    assertThat(pattern.matches("/foo/ooo")).isFalse();
  }

  @Test
  public void exclude_end_of_url() {
    UrlPattern pattern = UrlPattern.builder()
      .excludes("*foo")
      .build();

    assertThat(pattern.matches("/")).isTrue();
    assertThat(pattern.matches("/hello/foo")).isFalse();
    assertThat(pattern.matches("/hello/bar")).isTrue();
    assertThat(pattern.matches("/foo")).isFalse();
    assertThat(pattern.matches("/foo2")).isTrue();
  }

  @Test
  public void exclude_beginning_of_url() {
    UrlPattern pattern = UrlPattern.builder()
      .excludes("/foo/*")
      .build();

    assertThat(pattern.matches("/")).isTrue();
    assertThat(pattern.matches("/foo")).isFalse();
    assertThat(pattern.matches("/foo/bar")).isFalse();
    assertThat(pattern.matches("/bar")).isTrue();
  }

  @Test
  public void exclude_exact_url() {
    UrlPattern pattern = UrlPattern.builder()
      .excludes("/foo")
      .build();

    assertThat(pattern.matches("/")).isTrue();
    assertThat(pattern.matches("/foo")).isFalse();
    assertThat(pattern.matches("/foo/")).isTrue();
    assertThat(pattern.matches("/bar")).isTrue();
  }

  @Test
  public void use_multiple_include_patterns() {
    UrlPattern pattern = UrlPattern.builder()
      .includes("/foo", "/foo2")
      .build();
    assertThat(pattern.matches("/")).isFalse();
    assertThat(pattern.matches("/foo")).isTrue();
    assertThat(pattern.matches("/foo2")).isTrue();
    assertThat(pattern.matches("/foo/")).isFalse();
    assertThat(pattern.matches("/bar")).isFalse();
  }

  @Test
  public void use_multiple_exclude_patterns() {
    UrlPattern pattern = UrlPattern.builder()
      .excludes("/foo", "/foo2")
      .build();
    assertThat(pattern.matches("/")).isTrue();
    assertThat(pattern.matches("/foo")).isFalse();
    assertThat(pattern.matches("/foo2")).isFalse();
    assertThat(pattern.matches("/foo/")).isTrue();
    assertThat(pattern.matches("/bar")).isTrue();
  }

  @Test
  public void use_include_and_exclude_patterns() {
    UrlPattern pattern = UrlPattern.builder()
      .includes("/foo/*", "/foo/lo*")
      .excludes("/foo/login", "/foo/logout", "/foo/list")
      .build();
    assertThat(pattern.matches("/")).isFalse();
    assertThat(pattern.matches("/foo")).isTrue();
    assertThat(pattern.matches("/foo/login")).isFalse();
    assertThat(pattern.matches("/foo/logout")).isFalse();
    assertThat(pattern.matches("/foo/list")).isFalse();
    assertThat(pattern.matches("/foo/locale")).isTrue();
    assertThat(pattern.matches("/foo/index")).isTrue();
  }

  @Test
  public void use_include_and_exclude_prefix() {
    UrlPattern pattern = UrlPattern.builder()
      .includes("/foo_2")
      .excludes("/foo")
      .build();
    assertThat(pattern.matches("/")).isFalse();
    assertThat(pattern.matches("/foo_2")).isTrue();
    assertThat(pattern.matches("/foo")).isFalse();
  }

  @Test
  public void exclude_pattern_has_higher_priority_than_include_pattern() {
    UrlPattern pattern = UrlPattern.builder()
      .includes("/foo")
      .excludes("/foo")
      .build();
    assertThat(pattern.matches("/foo")).isFalse();
  }

  @Test
  public void accept_empty_patterns() {
    UrlPattern pattern = UrlPattern.builder()
      .excludes()
      .includes()
      .build();
    assertThat(pattern.matches("/")).isTrue();
    assertThat(pattern.matches("/foo/bar")).isTrue();
  }

  @Test
  public void create_throws_IAE_if_empty_url() {
    assertThatThrownBy(() -> UrlPattern.create(""))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("URL pattern must start with slash '/': ");
  }

  @Test
  public void getUrl_returns_single_inclusion() {
    assertThat(UrlPattern.create("/*").getInclusions()).containsOnly("/*");
    assertThat(UrlPattern.create("/foo/bar").getInclusions()).containsOnly("/foo/bar");
  }

  @Test
  public void test_staticResourcePatterns() {
    assertThat(UrlPattern.Builder.staticResourcePatterns()).containsOnly(
      "*.css",
      "*.css.map",
      "*.ico",
      "*.png",
      "*.jpg",
      "*.jpeg",
      "*.gif",
      "*.svg",
      "*.js",
      "*.js.map",
      "*.pdf",
      "*.woff2",
      "/json/*",
      "/static/*",
      "/robots.txt",
      "/favicon.ico",
      "/apple-touch-icon*",
      "/mstile*");
  }

  @Test
  public void test_label() {
    assertThat(UrlPattern.builder().build().label()).isEqualTo("UrlPattern{inclusions=[], exclusions=[]}");
    assertThat(UrlPattern.builder()
      .includes("/foo/*")
      .excludes("/foo/login")
      .build().label()).isEqualTo("UrlPattern{inclusions=[/foo/*], exclusions=[/foo/login]}");
    assertThat(UrlPattern.builder()
      .includes("/foo/*", "/foo/lo*")
      .excludes("/foo/login", "/foo/logout", "/foo/list")
      .build().label()).isEqualTo("UrlPattern{inclusions=[/foo/*, ...], exclusions=[/foo/login, ...]}");
  }
}
