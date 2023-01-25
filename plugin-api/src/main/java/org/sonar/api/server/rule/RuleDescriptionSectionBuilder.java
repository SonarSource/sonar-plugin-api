/*
 * Sonar Plugin API
 * Copyright (C) 2009-2023 SonarSource SA
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
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.sonar.api.server.rule.StringPatternValidator.validatorWithCommonPatternForKeys;

/**
 * This builder allows to build the right implementation of {@link RuleDescriptionSection}, depending on the provided arguments
 * @since 9.6
 */
public final class RuleDescriptionSectionBuilder {
  private static final StringPatternValidator SECTION_KEY_VALIDATOR = validatorWithCommonPatternForKeys("section keys");

  private String sectionKey;
  private String htmlContent;
  private Context context;

  /**
   * Identifier of the section, must be unique across sections of a given rule
   * Section keys must follow the format defined in {@link StringPatternValidator#COMMON_PATTERN_FOR_KEYS}
   */
  public RuleDescriptionSectionBuilder sectionKey(String sectionKey) {
    SECTION_KEY_VALIDATOR.validate(sectionKey);
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
   * The classpath URL of the resource containing the rule section content in HTML format.
   * Example : {@code htmlClasspathResourceUrl(getClass().getResource("/myrepo/Rule1234_section_intro.html")}
   */
  public RuleDescriptionSectionBuilder htmlClasspathResourceUrl(URL htmlClasspathResourceUrl) {
    try {
      htmlContent(IOUtils.toString(htmlClasspathResourceUrl, UTF_8));
    } catch (IOException e) {
      throw new IllegalStateException("Fail to read: " + htmlClasspathResourceUrl, e);
    }
    return this;
  }

  /**
   * For context specific descriptions, the context key, must be unique across a given section of a rule.
   * @since 9.7
   */
  public RuleDescriptionSectionBuilder context(@Nullable Context context) {
    this.context = context;
    return this;
  }

  public RuleDescriptionSection build() {
    if (context == null) {
      return new DefaultRuleDescriptionSection(sectionKey, htmlContent);
    }
    return new ContextAwareRuleDescriptionSection(sectionKey, context, htmlContent);
  }

}
