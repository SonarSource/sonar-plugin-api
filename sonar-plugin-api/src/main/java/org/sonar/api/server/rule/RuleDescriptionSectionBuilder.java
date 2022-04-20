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
package org.sonar.api.server.rule;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class RuleDescriptionSectionBuilder {
  private String sectionKey;
  private String htmlContent;

  /**
   * Identifier of the section, must be unique across sections of a given rule
   */
  public RuleDescriptionSectionBuilder sectionKey(String sectionKey) {
    this.sectionKey = sectionKey;
    return this;
  }

  /**
   * The content, in HTML format
   */
  public RuleDescriptionSectionBuilder htmlContent(String htmlContent) {
    this.htmlContent = htmlContent;
    return this;
  }

  /**
   * The classpath URL of the resource containing the rule section content in HTML format
   */
  public RuleDescriptionSectionBuilder htmlClasspathResourceUrl(URL htmlClasspathResourceUrl) {
    try {
      htmlContent(IOUtils.toString(htmlClasspathResourceUrl, UTF_8));
    } catch (IOException e) {
      throw new IllegalStateException("Fail to read: " + htmlClasspathResourceUrl, e);
    }
    return this;
  }

  public RuleDescriptionSection build() {
    return new DefaultRuleDescriptionSection(sectionKey, htmlContent);
  }
}
