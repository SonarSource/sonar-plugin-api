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

import java.util.Collections;
import java.util.List;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

public class RuleTagFormatTest {

  @Test
  public void isValid() {
    assertThat(RuleTagFormat.isValid(null)).isFalse();
    assertThat(RuleTagFormat.isValid("")).isFalse();
    assertThat(RuleTagFormat.isValid(" ")).isFalse();
    assertThat(RuleTagFormat.isValid("coding style")).isFalse();
    assertThat(RuleTagFormat.isValid("Style")).isFalse();
    assertThat(RuleTagFormat.isValid("sTyle")).isFalse();
    assertThat(RuleTagFormat.isValid("@style")).isFalse();

    assertThat(RuleTagFormat.isValid("style")).isTrue();
    assertThat(RuleTagFormat.isValid("c++")).isTrue();
    assertThat(RuleTagFormat.isValid("f#")).isTrue();
    assertThat(RuleTagFormat.isValid("c++11")).isTrue();
    assertThat(RuleTagFormat.isValid("c.d")).isTrue();
  }

  @Test
  public void validate() {
    RuleTagFormat.validate("style");
    // no error
  }

  @Test
  public void validate_and_fail() {
    assertThatIllegalArgumentException().isThrownBy(() -> RuleTagFormat.validate(" "));
  }

  @Test
  public void validate_and_sanitize_collection_of_tags() {
    assertThat(RuleTagFormat.validate(asList("style", "coding-style", ""))).containsExactly("coding-style", "style");
    assertThat(RuleTagFormat.validate(asList("style", "coding-style", null))).containsExactly("coding-style", "style");
    assertThat(RuleTagFormat.validate(asList("style", "style", null))).containsExactly("style");
    assertThat(RuleTagFormat.validate(singletonList("Uppercase"))).containsExactly("uppercase");
    assertThat(RuleTagFormat.validate(Collections.emptyList())).isEmpty();
  }

  @Test
  public void fail_to_validate_collection_of_tags() {
    List<String> tags = asList("coding style", "Stylé", "valid");
    assertThatIllegalArgumentException().isThrownBy(() -> RuleTagFormat.validate(tags));
  }
}
