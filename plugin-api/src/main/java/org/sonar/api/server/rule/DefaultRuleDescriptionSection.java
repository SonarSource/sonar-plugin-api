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
package org.sonar.api.server.rule;

import static org.apache.commons.lang3.StringUtils.isEmpty;

class DefaultRuleDescriptionSection implements RuleDescriptionSection {

  private final String key;
  private final String htmlContent;

  DefaultRuleDescriptionSection(String key, String htmlContent) {
    failIfEmpty(key, "The section key must be provided and can't be empty");
    failIfEmpty(htmlContent, "The html content of the section must be provided and can't be empty");
    this.key = key;
    this.htmlContent = htmlContent;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public String getHtmlContent() {
    return htmlContent;
  }

  private static void failIfEmpty(String str, String msg) {
    if (isEmpty(str)) {
      throw new IllegalArgumentException(msg);
    }
  }
}
