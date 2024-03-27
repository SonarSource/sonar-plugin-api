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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;
import static org.sonar.api.utils.Preconditions.checkArgument;

/**
 * Logic of this class should be moved to URLPattern class after deprecation period.
 */
abstract class AbstractUrlPattern {

  private static final String MATCH_ALL = "/*";

  private final List<String> inclusions;
  private final List<String> exclusions;
  private final Predicate<String>[] inclusionPredicates;
  private final Predicate<String>[] exclusionPredicates;

  AbstractUrlPattern(Builder builder) {
    this.inclusions = unmodifiableList(new ArrayList<>(builder.inclusions));
    this.exclusions = unmodifiableList(new ArrayList<>(builder.exclusions));
    if (builder.inclusionPredicates.isEmpty()) {
      // because Stream#anyMatch() returns false if stream is empty
      this.inclusionPredicates = new Predicate[] {s -> true};
    } else {
      this.inclusionPredicates = (Predicate<String>[]) builder.inclusionPredicates.stream().toArray(Predicate[]::new);
    }
    this.exclusionPredicates = (Predicate<String>[]) builder.exclusionPredicates.stream().toArray(Predicate[]::new);
  }

  public boolean matches(String path) {
    return Arrays.stream(exclusionPredicates).noneMatch(pattern -> pattern.test(path)) &&
      Arrays.stream(inclusionPredicates).anyMatch(pattern -> pattern.test(path));
  }

  /**
   * @since 6.0
   */
  public Collection<String> getInclusions() {
    return inclusions;
  }

  /**
   * @since 6.0
   */
  public Collection<String> getExclusions() {
    return exclusions;
  }

  public String label() {
    return "UrlPattern{" +
      "inclusions=[" + convertPatternsToString(inclusions) + "]" +
      ", exclusions=[" + convertPatternsToString(exclusions) + "]" +
      '}';
  }

  private static String convertPatternsToString(List<String> input) {
    StringBuilder output = new StringBuilder();
    if (input.isEmpty()) {
      return "";
    }
    if (input.size() == 1) {
      return output.append(input.get(0)).toString();
    }
    return output.append(input.get(0)).append(", ...").toString();
  }

  /**
   * @since 6.0
   */
  public abstract static class Builder<T extends AbstractUrlPattern, B extends Builder> {
    private static final String WILDCARD_CHAR = "*";
    static final Collection<String> STATIC_RESOURCES = List.of("*.css", "*.css.map", "*.ico", "*.png",
      "*.jpg", "*.jpeg", "*.gif", "*.svg", "*.js", "*.js.map", "*.pdf", "/json/*", "*.woff2", "/static/*",
      "/robots.txt", "/favicon.ico", "/apple-touch-icon*", "/mstile*");

    private final Set<String> inclusions = new LinkedHashSet<>();
    private final Set<String> exclusions = new LinkedHashSet<>();
    private final Set<Predicate<String>> inclusionPredicates = new HashSet<>();
    private final Set<Predicate<String>> exclusionPredicates = new HashSet<>();

    Builder() {
    }

    /**
     * @deprecated since 10.0. Products implementing the API should define this internally.
     */
    @Deprecated(since = "10.0")
    public static Collection<String> staticResourcePatterns() {
      return STATIC_RESOURCES;
    }

    /**
     * Add inclusion patterns. Supported formats are:
     * <ul>
     *   <li>path prefixed by / and ended by * or /*, for example "/api/foo/*", to match all paths "/api/foo" and "api/api/foo/something/else"</li>
     *   <li>path prefixed by / and ended by .*, for example "/api/foo.*", to match exact path "/api/foo" with any suffix like "/api/foo.protobuf"</li>
     *   <li>path prefixed by *, for example "*\/foo", to match all paths "/api/foo" and "something/else/foo"</li>
     *   <li>path with leading slash and no wildcard, for example "/api/foo", to match exact path "/api/foo"</li>
     * </ul>
     */
    public B includes(String... includePatterns) {
      return includes(asList(includePatterns));
    }

    /**
     * Add exclusion patterns. See format described in {@link #includes(String...)}
     */
    public B includes(Collection<String> includePatterns) {
      this.inclusions.addAll(includePatterns);
      this.inclusionPredicates.addAll(includePatterns.stream()
        .filter(pattern -> !MATCH_ALL.equals(pattern))
        .map(Builder::compile)
        .collect(Collectors.toList()));
      return (B) this;
    }

    public B excludes(String... excludePatterns) {
      return excludes(asList(excludePatterns));
    }

    public B excludes(Collection<String> excludePatterns) {
      this.exclusions.addAll(excludePatterns);
      this.exclusionPredicates.addAll(excludePatterns.stream()
        .map(Builder::compile)
        .collect(Collectors.toList()));
      return (B) this;
    }

    public abstract T build();

    private static Predicate<String> compile(String pattern) {
      int countStars = pattern.length() - pattern.replace(WILDCARD_CHAR, "").length();
      if (countStars == 0) {
        checkArgument(pattern.startsWith("/"), "URL pattern must start with slash '/': %s", pattern);
        return url -> url.equals(pattern);
      }
      checkArgument(countStars == 1, "URL pattern accepts only zero or one wildcard character '*': %s", pattern);
      if (pattern.charAt(0) == '/') {
        checkArgument(pattern.endsWith(WILDCARD_CHAR), "URL pattern must end with wildcard character '*': %s", pattern);
        if (pattern.endsWith("/*")) {
          String path = pattern.substring(0, pattern.length() - "/*".length());
          return url -> url.startsWith(path);
        }
        if (pattern.endsWith(".*")) {
          String path = pattern.substring(0, pattern.length() - ".*".length());
          return url -> substringBeforeLast(url, ".").equals(path);
        }
        String path = pattern.substring(0, pattern.length() - "*".length());
        return url -> url.startsWith(path);
      }
      checkArgument(pattern.startsWith(WILDCARD_CHAR), "URL pattern must start with wildcard character '*': %s", pattern);
      // remove the leading *
      String path = pattern.substring(1);
      return url -> url.endsWith(path);
    }
  }
}
