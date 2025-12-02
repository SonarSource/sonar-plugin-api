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
package org.sonar.api.server.rule.internal;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;

public class DefaultRepositoryTest {
  @Test
  public void create_simple_repo() {
    RulesDefinition.Context ctx = mock(RulesDefinition.Context.class);
    DefaultNewRepository newRepo = new DefaultNewRepository(ctx, "key", "lang", false);
    newRepo.createRule("rule1")
      .setName("rule1")
      .setHtmlDescription("desc");
    newRepo.setName("name");
    DefaultRepository repo = new DefaultRepository(newRepo, null);

    assertThat(repo.isExternal()).isFalse();
    assertThat(repo.key()).isEqualTo("key");
    assertThat(repo.language()).isEqualTo("lang");
    assertThat(repo.isExternal()).isFalse();
    assertThat(repo.name()).isEqualTo("name");
    assertThat(repo.rules()).extracting(RulesDefinition.Rule::key).containsOnly("rule1");
  }

  @Test
  public void create_with_merge_repo_throws_when_languages_do_not_match() {
    RulesDefinition.Context ctx = mock(RulesDefinition.Context.class);
    DefaultNewRepository newRepository = new DefaultNewRepository(ctx, "key", "lang", false);
    DefaultRepository mergeIntoRepository = new DefaultRepository(new DefaultNewRepository(ctx, "key1", "lang", false), null);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new DefaultRepository(newRepository, mergeIntoRepository))
      .withMessage("Bug - language and key of the repositories to be merged should be the same: NewRepository{key='key', language='lang'} and Repository{key='key1', " +
        "language='lang'}");
  }
}
