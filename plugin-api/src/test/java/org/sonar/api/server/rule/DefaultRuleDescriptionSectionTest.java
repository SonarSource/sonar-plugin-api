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
package org.sonar.api.server.rule;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class DefaultRuleDescriptionSectionTest {

  private static final String SECTION_KEY = "key";
  private static final String SECTION_HTML_CONTENT = "description";

  @Test
  public void constructor_instantiateFieldsCorrectly() {
    RuleDescriptionSection section = new DefaultRuleDescriptionSection(SECTION_KEY, SECTION_HTML_CONTENT);

    assertThat(section.getKey()).isEqualTo(SECTION_KEY);
    assertThat(section.getHtmlContent()).isEqualTo(SECTION_HTML_CONTENT);
  }

  @Test
  public void constructor_whenGivenNullKey_shouldFail() {
    assertThatIllegalArgumentException()
      .isThrownBy(() -> new DefaultRuleDescriptionSection(null, SECTION_HTML_CONTENT))
      .withMessage("The section key must be provided and can't be empty");
  }

  @Test
  public void constructor_whenGivenNullHtmlContent_shouldFail() {
    assertThatIllegalArgumentException()
      .isThrownBy(() -> new DefaultRuleDescriptionSection(SECTION_KEY, null))
      .withMessage("The html content of the section must be provided and can't be empty");
  }

  @Test
  public void constructor_whenGivenEmptyKey_shouldFail() {
    assertThatIllegalArgumentException()
      .isThrownBy(() -> new DefaultRuleDescriptionSection("", SECTION_HTML_CONTENT))
      .withMessage("The section key must be provided and can't be empty");
  }

  @Test
  public void constructor_whenGivenEmptyHtmlContent_shouldFail() {
    assertThatIllegalArgumentException()
      .isThrownBy(() -> new DefaultRuleDescriptionSection(SECTION_KEY, ""))
      .withMessage("The html content of the section must be provided and can't be empty");
  }
}
