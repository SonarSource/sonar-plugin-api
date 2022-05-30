/*
 * Sonar Plugin API
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
package org.sonar.api;

import static org.junit.Assert.assertEquals;

import java.util.Optional;
import org.junit.Test;
import org.sonar.api.utils.Version;


public class SonarRuntimeTest {

  @Test
  public void simple_interface_impl() {
    SonarRuntimeImpl underTest = new SonarRuntimeImpl();
    assertEquals(underTest.getApiVersion(), Version.create(9, 6, 1));
    assertEquals(underTest.getProductVersion(), Optional.empty());
  }

  private static class SonarRuntimeImpl implements SonarRuntime {

    @Override
    public Version getApiVersion() {
      return Version.create(9, 6, 1);
    }

    @Override
    public SonarProduct getProduct() {
      return SonarProduct.SONARQUBE;
    }

    @Override
    public Optional<Version> getProductVersion() {
      return Optional.empty();
    }

    @Override
    public SonarQubeSide getSonarQubeSide() {
      return SonarQubeSide.SERVER;
    }

    @Override
    public SonarEdition getEdition() {
      return SonarEdition.COMMUNITY;
    }
  }
}
