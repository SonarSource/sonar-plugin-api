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

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckForNull;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.Duration;

/**
 * @since 3.6
 */
public interface Issue extends Serializable {

  /**
   * Maximum number of characters in the message.
   * In theory it should be 4_000 UTF-8 characters but unfortunately
   * Oracle DB does not support more than 4_000 bytes, even if column
   * issues.message is created with type VARCHAR2(4000 CHAR).
   * In order to have the same behavior on all databases, message
   * is truncated to 4_000 / 3 (maximum bytes per UTF-8 character)
   * = 1_333 characters.
   */
  int MESSAGE_MAX_SIZE = 1_333;

  /**
   * Default status when creating an issue.
   *
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   */
  @Deprecated(since = "10.4")
  String STATUS_OPEN = "OPEN";
  /**
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   */
  @Deprecated(since = "10.4")
  String STATUS_CONFIRMED = "CONFIRMED";
  /**
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   */
  @Deprecated(since = "10.4")
  String STATUS_REOPENED = "REOPENED";
  /**
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   */
  @Deprecated(since = "10.4")
  String STATUS_RESOLVED = "RESOLVED";
  /**
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   */
  @Deprecated(since = "10.4")
  String STATUS_CLOSED = "CLOSED";

  String RESOLUTION_FIXED = "FIXED";

  /**
   * Resolution when issue is flagged as false positive.
   */
  /**
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   */
  @Deprecated(since = "10.4")
  String RESOLUTION_FALSE_POSITIVE = "FALSE-POSITIVE";

  /**
   * Resolution when rule has been uninstalled or disabled in the Quality profile.
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   */
  @Deprecated(since = "10.4")
  String RESOLUTION_REMOVED = "REMOVED";

  /**
   * Issue is irrelevant in the context and was muted by user.
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   * @since 5.1
   */
  @Deprecated(since = "10.4")
  String RESOLUTION_WONT_FIX = "WONTFIX";

  /**
   * Security Hotspot has been reviewed and resolved as safe.
   * @since 8.1
   */
  String RESOLUTION_SAFE = "SAFE";

  /**
   * Security Hotspot has been reviewed and acknowledged that it poses a risk.
   * @since 9.4
   */
  String RESOLUTION_ACKNOWLEDGED = "ACKNOWLEDGED";

  /**
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   */
  @Deprecated(since = "10.4")
  List<String> RESOLUTIONS = List.of(RESOLUTION_FALSE_POSITIVE, RESOLUTION_WONT_FIX, RESOLUTION_FIXED,
    RESOLUTION_REMOVED);

  List<String> SECURITY_HOTSPOT_RESOLUTIONS = List.of(RESOLUTION_FIXED, RESOLUTION_SAFE, RESOLUTION_ACKNOWLEDGED);

  String STATUS_TO_REVIEW = "TO_REVIEW";

  String STATUS_REVIEWED = "REVIEWED";

  /**
   * Return all available statuses
   *
   * @since 4.4
   * @deprecated since 10.4 in favor of {@link IssueStatus}
   */
  @Deprecated(since = "10.4")
  List<String> STATUSES = List.of(STATUS_OPEN, STATUS_CONFIRMED, STATUS_REOPENED, STATUS_RESOLVED, STATUS_CLOSED,
    STATUS_TO_REVIEW, STATUS_REVIEWED);

  /**
   * Unique generated key. It looks like "d2de809c-1512-4ae2-9f34-f5345c9f1a13".
   */
  String key();

  /**
   * Components are modules ("my_project"), directories ("my_project:my/dir") or files ("my_project:my/file.c").
   * Keys of Java packages and classes are currently in a special format: "my_project:com.company" and "my_project:com.company.Foo".
   */
  String componentKey();

  RuleKey ruleKey();

  String language();

  /**
   * See constants in {@link org.sonar.api.rule.Severity}.
   */
  @CheckForNull
  String severity();

  @CheckForNull
  String message();

  /**
   * Optional line number. If set, then it's greater than or equal 1.
   */
  @CheckForNull
  Integer line();

  /**
   * Arbitrary distance to threshold for resolving the issue.
   * <br>
   * For examples:
   * <ul>
   *   <li>for the rule "Avoid too complex methods" : current complexity - max allowed complexity</li>
   *   <li>for the rule "Avoid Duplications" : number of duplicated blocks</li>
   *   <li>for the rule "Insufficient Line Coverage" : number of lines to cover to reach the accepted threshold</li>
   * </ul>
   *
   * @since 5.5
   */
  @CheckForNull
  Double gap();

  /**
   * See constant values in {@link Issue}.
   * @deprecated since 10.4 in favor of {@link IssueStatus}. Not deprecated for hotspots
   */
  @Deprecated(since = "10.4")
  String status();

  /**
   * The type of resolution, or null if the issue is not resolved. See constant values in {@link Issue}.
   * @deprecated since 10.4 in favor of {@link IssueStatus}. Not deprecated for hotspots
   */
  @CheckForNull
  @Deprecated(since = "10.4")
  String resolution();

  /**
   * UUID of the user who is assigned to this issue. Null if the issue is not assigned.
   */
  @CheckForNull
  String assignee();

  Date creationDate();

  Date updateDate();

  /**
   * Date when status was set to {@link Issue#STATUS_CLOSED}, else null.
   */
  @CheckForNull
  Date closeDate();

  /**
   * Login of the SCM account that introduced this issue. Requires the
   * <a href="http://www.sonarsource.com/products/plugins/developer-tools/developer-cockpit/">Developer Cockpit Plugin</a> to be installed.
   */
  @CheckForNull
  String authorLogin();

  /**
   * During a scan return if the current issue is a new one.
   *
   * @return always false on server side
   * @since 4.0
   */
  boolean isNew();

  /**
   * During a scan returns true if the issue is copied from another branch.
   *
   * @since 6.6
   */
  boolean isCopied();

  /**
   * @since 5.5
   */
  @CheckForNull
  Duration effort();

  /**
   * @since 5.0
   */
  String projectKey();

  /**
   * @since 5.0
   */
  String projectUuid();

  /**
   * @since 5.0
   */
  String componentUuid();

  /**
   * @since 5.1
   */
  Collection<String> tags();

  /**
   * @since 9.17
   */
  @CheckForNull
  Collection<String> codeVariants();
}
