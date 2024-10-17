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
package org.sonar.api.resources;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"removal"})
class ScopesTest {
  @Test
  void it_should_order_scopes() {
    assertThat(Scopes.isHigherThan(Scopes.DIRECTORY, Scopes.FILE)).isTrue();
    assertThat(Scopes.isHigherThan(Scopes.PROJECT, Scopes.FILE)).isTrue();
    assertThat(Scopes.isHigherThan(Scopes.PROJECT, Scopes.DIRECTORY)).isTrue();
    assertThat(Scopes.isHigherThan(Scopes.FILE, Scopes.FILE)).isFalse();
    assertThat(Scopes.isHigherThan(Scopes.DIRECTORY, Scopes.DIRECTORY)).isFalse();
    assertThat(Scopes.isHigherThan(Scopes.PROJECT, Scopes.PROJECT)).isFalse();

    assertThat(Scopes.isHigherThanOrEquals(Scopes.FILE, Scopes.FILE)).isTrue();
    assertThat(Scopes.isHigherThanOrEquals(Scopes.DIRECTORY, Scopes.DIRECTORY)).isTrue();
    assertThat(Scopes.isHigherThanOrEquals(Scopes.PROJECT, Scopes.PROJECT)).isTrue();
  }

}
