/*
 * SonarQube
 * Copyright (C) 2009-2022 SonarSource SA
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 1.11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UserRole {
  /**
   * Permissions which are implicitly available for any user, any group and to group "AnyOne" on public components.
   * @since 7.5
   */
  Set<String> PUBLIC_PERMISSIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(UserRole.USER, UserRole.CODEVIEWER)));

  /**
   * @deprecated use the constant USER since 1.12.
   */
  @Deprecated
  String VIEWER = "user";

  String USER = "user";
  String ADMIN = "admin";
  String CODEVIEWER = "codeviewer";
  String ISSUE_ADMIN = "issueadmin";

  /**
   * @since 7.3
   */
  String SECURITYHOTSPOT_ADMIN = "securityhotspotadmin";

  /**
   * @since 7.5
   */
  String SCAN = "scan";

  String[] value() default {};

}
