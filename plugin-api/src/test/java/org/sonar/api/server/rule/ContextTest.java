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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class ContextTest {

  private static final String TEST_CONTEXT_KEY = "context_key";
  private static final String TEST_CONTEXT_DISPLAY_NAME = "CTX Display name";

  @Test
  public void constructor_assign_to_relevant_fields() {
    Context context = new Context(TEST_CONTEXT_KEY, TEST_CONTEXT_DISPLAY_NAME);
    assertThat(context.getKey()).isEqualTo(TEST_CONTEXT_KEY);
    assertThat(context.getDisplayName()).isEqualTo(TEST_CONTEXT_DISPLAY_NAME);
  }

  @Test
  public void constructor_throws_if_context_key_missing() {
    assertThatIllegalArgumentException().isThrownBy(() -> new Context(null, TEST_CONTEXT_DISPLAY_NAME))
      .withMessage("key must be provided and can't be empty");
  }

  @Test
  public void constructor_throws_if_context_key_invalid() {
    assertThatIllegalArgumentException().isThrownBy(() -> new Context("a b", TEST_CONTEXT_DISPLAY_NAME));
  }

  @Test
  public void constructor_throws_if_context_display_name_missing() {
    assertThatIllegalArgumentException().isThrownBy(() -> new Context(TEST_CONTEXT_KEY, null))
      .withMessage("displayName must be provided and can't be empty");
  }

  @Test
  public void constructor_throws_if_context_key_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> new Context("", TEST_CONTEXT_DISPLAY_NAME))
      .withMessage("key must be provided and can't be empty");
  }

  @Test
  public void constructor_throws_if_context_display_name_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> new Context(TEST_CONTEXT_KEY, ""))
      .withMessage("displayName must be provided and can't be empty");
  }

}
