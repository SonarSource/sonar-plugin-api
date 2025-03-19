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
package org.sonar.api.issue;

import java.util.List;

/**
 * @since 3.6
 * @deprecated since 11.4 not used by any extension point anymore
 */
@Deprecated(since = "11.4", forRemoval = true)
public interface DefaultTransitions {

  /**
   * @deprecated since 10.4, use {@link #ACCEPT} instead
   */
  @Deprecated(since = "10.4")
  String CONFIRM = "confirm";

  /**
   * @deprecated since 10.4. There is no replacement as `org.sonar.api.issue.DefaultTransitions.CONFIRM` is subject to removal in the future.
   */
  @Deprecated(since = "10.4")
  String UNCONFIRM = "unconfirm";
  String REOPEN = "reopen";
  String RESOLVE = "resolve";
  String FALSE_POSITIVE = "falsepositive";
  String CLOSE = "close";

  /**
   * @since 5.1
   * @deprecated since 10.3, use {@link #ACCEPT} instead
   */
  @Deprecated(since = "10.3")
  String WONT_FIX = "wontfix";

  /**
   * @since 10.3
   */
  String ACCEPT = "accept";

  /**
   * @since 7.8
   */
  String RESOLVE_AS_REVIEWED = "resolveasreviewed";

  /**
   * @since 8.1
   */
  String RESOLVE_AS_SAFE = "resolveassafe";

  /**
   * @since 9.4
   */
  String RESOLVE_AS_ACKNOWLEDGED = "resolveasacknowledged";

  /**
   * @since 7.8
   */
  String RESET_AS_TO_REVIEW = "resetastoreview";

  /**
   * @since 4.4
   */
  List<String> ALL = List.of(CONFIRM, UNCONFIRM, REOPEN, RESOLVE, FALSE_POSITIVE, WONT_FIX, CLOSE,
    RESOLVE_AS_REVIEWED, RESET_AS_TO_REVIEW, ACCEPT);
}
