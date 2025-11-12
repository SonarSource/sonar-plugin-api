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
package org.sonar.api.web;

public final class UrlPattern extends AbstractUrlPattern {

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
  public static UrlPattern.Builder builder() {
    return new UrlPattern.Builder();
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
