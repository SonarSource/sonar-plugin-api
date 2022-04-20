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

import java.net.URL;
import java.util.Objects;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

public class RuleDescriptionSectionBuilderTest {

  private static final String TEST_HTML_DESCRIPTION = "test HTML description";
  private static final String TEST_SECTION_KEY = "Test";
  private static final String RULE_DESCRIPTION_FILE = "RuleDescriptionSectionBuilderTest.html";
  private final URL RULE_DESCRIPTION_FILE_URL = Objects.requireNonNull(getClass().getResource(RULE_DESCRIPTION_FILE));

  @Test
  public void build_withSectionKeyAndHtmlDescription() {
    RuleDescriptionSection descriptionSection = new RuleDescriptionSectionBuilder().sectionKey(TEST_SECTION_KEY).htmlContent(TEST_HTML_DESCRIPTION).build();
    assertThat(descriptionSection.getKey()).isEqualTo(TEST_SECTION_KEY);
    assertThat(descriptionSection.getHtmlContent()).isEqualTo(TEST_HTML_DESCRIPTION);
  }

  @Test
  public void build_withSectionKeyAndHtmlResource_shouldLoadContentFromResource() {
    RuleDescriptionSection descriptionSection = new RuleDescriptionSectionBuilder().sectionKey(TEST_SECTION_KEY).htmlClasspathResourceUrl(RULE_DESCRIPTION_FILE_URL).build();
    assertThat(descriptionSection.getKey()).isEqualTo(TEST_SECTION_KEY);
    assertThat(descriptionSection.getHtmlContent()).isEqualTo(contentOf(RULE_DESCRIPTION_FILE_URL));
  }

}
