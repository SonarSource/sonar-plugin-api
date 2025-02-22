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
package org.sonar.api.testfixtures.measure;

import org.junit.jupiter.api.Test;
import org.sonar.api.ce.measure.Component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestComponentTest {

  @Test
  void create_project() {
    TestComponent component = new TestComponent("Project", Component.Type.PROJECT, null);

    assertThat(component.getKey()).isEqualTo("Project");
    assertThat(component.getType()).isEqualTo(Component.Type.PROJECT);
  }

  @Test
  void create_source_file() {
    TestComponent component = new TestComponent("File", Component.Type.FILE, new TestComponent.FileAttributesImpl("xoo", false));

    assertThat(component.getType()).isEqualTo(Component.Type.FILE);
    assertThat(component.getFileAttributes().getLanguageKey()).isEqualTo("xoo");
    assertThat(component.getFileAttributes().isUnitTest()).isFalse();
  }

  @Test
  void create_test_file() {
    TestComponent component = new TestComponent("File", Component.Type.FILE, new TestComponent.FileAttributesImpl(null, true));

    assertThat(component.getType()).isEqualTo(Component.Type.FILE);
    assertThat(component.getFileAttributes().isUnitTest()).isTrue();
    assertThat(component.getFileAttributes().getLanguageKey()).isNull();
  }

  @Test
  void fail_with_ISE_when_calling_get_file_attributes_on_not_file() {
    TestComponent component = new TestComponent("Project", Component.Type.PROJECT, null);

    assertThatThrownBy(component::getFileAttributes)
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Only component of type FILE have a FileAttributes object");
  }

  @Test
  void fail_with_IAE_when_trying_to_create_a_file_without_file_attributes() {
    assertThatThrownBy(() -> new TestComponent("File", Component.Type.FILE, null))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Component of type FILE must have a FileAttributes object");
  }

  @Test
  void fail_with_IAE_when_trying_to_create_not_a_file_with_file_attributes() {
    assertThatThrownBy(() -> new TestComponent("Project", Component.Type.PROJECT, new TestComponent.FileAttributesImpl(null, true)))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Only component of type FILE have a FileAttributes object");
  }

  @Test
  void fail_with_NPE_when_creating_component_without_key() {
    assertThatThrownBy(() -> new TestComponent(null, Component.Type.PROJECT, null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("Key cannot be null");
  }

  @Test
  void fail_with_NPE_when_creating_component_without_type() {
    assertThatThrownBy(() -> new TestComponent("Project", null, null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("Type cannot be null");
  }

  @Test
  void test_equals_and_hashcode() {
    TestComponent component = new TestComponent("Project1", Component.Type.PROJECT, null);
    TestComponent sameComponent = new TestComponent("Project1", Component.Type.PROJECT, null);
    TestComponent anotherComponent = new TestComponent("Project2", Component.Type.PROJECT, null);

    assertThat(component)
      .isEqualTo(component)
      .isEqualTo(sameComponent)
      .isNotEqualTo(anotherComponent)
      .isNotNull()
      .hasSameHashCodeAs(component)
      .hasSameHashCodeAs(sameComponent);
    assertThat(component.hashCode()).isNotEqualTo(anotherComponent.hashCode());
  }

  @Test
  void test_to_string() {
    assertThat(new TestComponent("File", Component.Type.FILE, new TestComponent.FileAttributesImpl("xoo", true)).toString())
      .hasToString("ComponentImpl{key=File, type='FILE', fileAttributes=FileAttributesImpl{languageKey='xoo', unitTest=true}}");
  }

}
