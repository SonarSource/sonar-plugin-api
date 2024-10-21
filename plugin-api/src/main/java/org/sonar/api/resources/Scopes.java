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
package org.sonar.api.resources;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Resource scopes are used to group some types of resources. For example Java methods, Flex methods, C functions
 * and Cobol paragraphs are grouped in the scope "block unit".
 * <br>
 * Scopes are generally used in UI to display/hide some services or in web services.
 * <br>
 * Scopes are not extensible by plugins.
 *
 * @since 2.6
 * @deprecated since 10.13. Scopes are not used when developing plugins. This is exposed in some web APIs, but it doesn't make sense to make it part of the plugin API.
 */
@Deprecated(since = "10.13", forRemoval = true)
public final class Scopes {

  /**
   * For example view, subview, project, module or library. Persisted in database.
   */
  public static final String PROJECT = "PRJ";

  /**
   * For example directory or Java package. Persisted in database. A more generic term for this scope could
   * be "namespace"
   */
  public static final String DIRECTORY = "DIR";

  /**
   * For example a Java file. Persisted in database. A more generic term for this scope could
   * be "compilation unit". It's the lowest scope in file system units.
   */
  public static final String FILE = "FIL";

  public static final String[] SORTED_SCOPES = {PROJECT, DIRECTORY, FILE};

  private Scopes() {
    // only static methods
  }

  public static boolean isHigherThan(final String scope, final String than) {
    int index = ArrayUtils.indexOf(SORTED_SCOPES, scope);
    int thanIndex = ArrayUtils.indexOf(SORTED_SCOPES, than);
    return index < thanIndex;
  }


  public static boolean isHigherThanOrEquals(final String scope, final String than) {
    int index = ArrayUtils.indexOf(SORTED_SCOPES, scope);
    int thanIndex = ArrayUtils.indexOf(SORTED_SCOPES, than);
    return index <= thanIndex;
  }
}
