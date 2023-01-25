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

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.contentOf;

public class RuleDescriptionSectionBuilderTest {

  private static final String TEST_HTML_DESCRIPTION = "test HTML description";
  private static final String TEST_SECTION_KEY = "test";
  private static final String INVALID_TEST_SECTION_KEY = "test b";
  private static final String RULE_DESCRIPTION_FILE = "RuleDescriptionSectionBuilderTest.html";

  private static final String TEST_CONTEXT_DISPLAY_NAME = "TEST_CONTEXT_DISPLAY_NAME";
  private static final String TEST_CONTEXT_KEY = "context_key";
  private static final Context TEST_CONTEXT = new Context(TEST_CONTEXT_KEY, TEST_CONTEXT_DISPLAY_NAME);

  private final URL RULE_DESCRIPTION_FILE_URL = Objects.requireNonNull(getClass().getResource(RULE_DESCRIPTION_FILE));

  @Test
  public void build_sets_all_fields() {
    RuleDescriptionSection descriptionSection = new RuleDescriptionSectionBuilder()
      .sectionKey(TEST_SECTION_KEY)
      .htmlContent(TEST_HTML_DESCRIPTION)
      .context(TEST_CONTEXT)
      .build();

    assertThat(descriptionSection).isInstanceOf(ContextAwareRuleDescriptionSection.class);
    assertThat(descriptionSection.getKey()).isEqualTo(TEST_SECTION_KEY);
    assertThat(descriptionSection.getHtmlContent()).isEqualTo(TEST_HTML_DESCRIPTION);
    Optional<Context> context = descriptionSection.getContext();
    assertThat(context).isPresent();
    assertThat(context.get().getKey()).isEqualTo(TEST_CONTEXT_KEY);
    assertThat(context.get().getDisplayName()).isEqualTo(TEST_CONTEXT_DISPLAY_NAME);
  }

  @Test
  public void build_withSectionKeyAndHtmlDescription() {
    RuleDescriptionSection descriptionSection = new RuleDescriptionSectionBuilder().sectionKey(TEST_SECTION_KEY).htmlContent(TEST_HTML_DESCRIPTION).build();
    assertThat(descriptionSection).isInstanceOf(RuleDescriptionSection.class);
    assertThat(descriptionSection.getKey()).isEqualTo(TEST_SECTION_KEY);
    assertThat(descriptionSection.getHtmlContent()).isEqualTo(TEST_HTML_DESCRIPTION);
  }

  @Test
  public void build_withSectionKeyAndHtmlResource_shouldLoadContentFromResource() {
    RuleDescriptionSection descriptionSection = new RuleDescriptionSectionBuilder().sectionKey(TEST_SECTION_KEY).htmlClasspathResourceUrl(RULE_DESCRIPTION_FILE_URL).build();
    assertThat(descriptionSection).isInstanceOf(RuleDescriptionSection.class);
    assertThat(descriptionSection.getKey()).isEqualTo(TEST_SECTION_KEY);
    assertThat(descriptionSection.getHtmlContent()).isEqualTo(contentOf(RULE_DESCRIPTION_FILE_URL));
  }

  @Test
  public void build_withInvalidSectionKey_shouldFail() {
    assertThatIllegalArgumentException().isThrownBy(() -> new RuleDescriptionSectionBuilder().sectionKey(INVALID_TEST_SECTION_KEY));
  }

}
