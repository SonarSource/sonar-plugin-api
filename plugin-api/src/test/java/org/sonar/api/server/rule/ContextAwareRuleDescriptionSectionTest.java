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
package org.sonar.api.server.rule;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class ContextAwareRuleDescriptionSectionTest {

  private static final String SECTION_KEY = "key";
  private static final String SECTION_HTML_CONTENT = "description";

  @Test
  public void constructor_instantiateFieldsCorrectly() {
    Context context = new Context("ctx_key", "ctxDisplayName");
    RuleDescriptionSection section = new ContextAwareRuleDescriptionSection(SECTION_KEY, context, SECTION_HTML_CONTENT);

    assertThat(section.getKey()).isEqualTo(SECTION_KEY);
    assertThat(section.getHtmlContent()).isEqualTo(SECTION_HTML_CONTENT);
    assertThat(section.getContext()).contains(context);
  }

  @Test
  public void constructor_whenGivenNullContext_shouldFail() {
    assertThatNullPointerException()
      .isThrownBy(() -> new ContextAwareRuleDescriptionSection(SECTION_KEY, null, SECTION_HTML_CONTENT))
      .withMessage("context must be provided");
  }

}
