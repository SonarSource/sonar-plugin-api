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
package org.sonar.api.config;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.utils.System2;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyDefinitionsTest {

  @Test
  public void should_build_with_predefined_list_of_definitions() {
    List<PropertyDefinition> list = Arrays.asList(
      PropertyDefinition.builder("foo").name("Foo").build(),
      PropertyDefinition.builder("one").name("One").build(),
      PropertyDefinition.builder("two").name("Two").defaultValue("2").build());
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE, list);

    assertProperties(def);
  }

  @Test
  public void should_inspect_plugin_objects() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE,
      PropertyDefinition.builder("foo").name("Foo").build(),
      PropertyDefinition.builder("one").name("One").build(),
      PropertyDefinition.builder("two").name("Two").defaultValue("2").build());

    assertProperties(def);
  }

  @Test
  public void should_inspect_annotation_plugin_objects() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE, new PluginWithProperty(), new PluginWithProperties());

    assertProperties(def);
  }

  @Test
  public void should_inspect_plugin_classes() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE, PluginWithProperty.class, PluginWithProperties.class);

    assertProperties(def);
  }

  @Test
  public void test_categories() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE,
      PropertyDefinition.builder("inCateg").name("In Categ").category("categ").build(),
      PropertyDefinition.builder("noCateg").name("No categ").build());

    assertThat(def.getCategory("inCateg")).isEqualTo("categ");
    assertThat(def.getCategory("noCateg")).isEmpty();
  }

  @Test
  public void test_categories_on_annotation_plugin() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE, Categories.class);

    assertThat(def.getCategory("inCateg")).isEqualTo("categ");
    assertThat(def.getCategory("noCateg")).isEmpty();
  }

  @Test
  public void test_default_category() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE);
    def.addComponent(PropertyDefinition.builder("inCateg").name("In Categ").category("categ").build(), "default");
    def.addComponent(PropertyDefinition.builder("noCateg").name("No categ").build(), "default");

    assertThat(def.getCategory("inCateg")).isEqualTo("categ");
    assertThat(def.getCategory("noCateg")).isEqualTo("default");
  }

  @Test
  public void test_default_category_on_annotation_plugin() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE);
    def.addComponent(Categories.class, "default");
    assertThat(def.getCategory("inCateg")).isEqualTo("categ");
    assertThat(def.getCategory("noCateg")).isEqualTo("default");
  }

  @Test
  public void should_return_special_categories() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE);

    assertThat(def.propertiesByCategory(null).get(new Category("general"))).containsOnlyKeys(new SubCategory("email"));
    assertThat(def.propertiesByCategory(null).get(new Category("general")).keySet().iterator().next().isSpecial()).isTrue();
    assertThat(def.propertiesByCategory(null).get(new Category("security"))).containsOnlyKeys(new SubCategory("encryption"));
    assertThat(def.propertiesByCategory(null).get(new Category("security")).keySet().iterator().next().isSpecial()).isTrue();
    assertThat(def.propertiesByCategory(null).get(new Category("licenses"))).containsOnlyKeys(new SubCategory("server_id"));
    assertThat(def.propertiesByCategory(null).get(new Category("licenses")).keySet().iterator().next().isSpecial()).isTrue();
  }

  @Test
  public void should_group_by_category() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE,
      PropertyDefinition.builder("global1").name("Global1").category("catGlobal1").build(),
      PropertyDefinition.builder("global2").name("Global2").category("catGlobal1").build(),
      PropertyDefinition.builder("global3").name("Global3").category("catGlobal2").build(),
      PropertyDefinition.builder("project").name("Project").category("catProject").onlyOnQualifiers(Qualifiers.PROJECT).build(),
      PropertyDefinition.builder("module").name("Module").category("catModule").onlyOnQualifiers(Qualifiers.MODULE).build(),
      PropertyDefinition.builder("view").name("View").category("catView").onlyOnQualifiers(Qualifiers.VIEW).build(),
      PropertyDefinition.builder("app").name("Application").category("catApp").onlyOnQualifiers(Qualifiers.APP).build());

    assertThat(def.propertiesByCategory(null).keySet()).contains(new Category("catGlobal1"), new Category("catGlobal2"));
    assertThat(def.propertiesByCategory(Qualifiers.PROJECT)).containsOnlyKeys(new Category("catProject"));
    assertThat(def.propertiesByCategory(Qualifiers.MODULE)).containsOnlyKeys(new Category("catModule"));
    assertThat(def.propertiesByCategory(Qualifiers.VIEW)).containsOnlyKeys(new Category("catView"));
    assertThat(def.propertiesByCategory(Qualifiers.APP)).containsOnlyKeys(new Category("catApp"));
    assertThat(def.propertiesByCategory("Unkown").keySet()).isEmpty();
  }

  @Test
  public void should_group_by_subcategory() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE,
      PropertyDefinition.builder("global1").name("Global1").category("catGlobal1").subCategory("sub1").build(),
      PropertyDefinition.builder("global2").name("Global2").category("catGlobal1").subCategory("sub2").build(),
      PropertyDefinition.builder("global3").name("Global3").category("catGlobal1").build(),
      PropertyDefinition.builder("global4").name("Global4").category("catGlobal2").build());

    assertThat(def.propertiesByCategory(null).get(new Category("catGlobal1"))).containsOnlyKeys(new SubCategory("catGlobal1"), new SubCategory("sub1"),
      new SubCategory("sub2"));
    assertThat(def.propertiesByCategory(null).get(new Category("catGlobal2"))).containsOnlyKeys(new SubCategory("catGlobal2"));
  }

  @Test
  public void should_group_by_category_on_annotation_plugin() {
    PropertyDefinitions def = new PropertyDefinitions(System2.INSTANCE, ByCategory.class);

    assertThat(def.propertiesByCategory(null).keySet()).contains(new Category("catglobal1"), new Category("catglobal2"));
    assertThat(def.propertiesByCategory(Qualifiers.PROJECT)).containsOnlyKeys(new Category("catproject"));
    assertThat(def.propertiesByCategory(Qualifiers.MODULE)).containsOnlyKeys(new Category("catmodule"));
  }

  private void assertProperties(PropertyDefinitions definitions) {
    assertThat(definitions.get("foo").name()).isEqualTo("Foo");
    assertThat(definitions.get("one").name()).isEqualTo("One");
    assertThat(definitions.get("two").name()).isEqualTo("Two");
    assertThat(definitions.get("unknown")).isNull();

    assertThat(definitions.getDefaultValue("foo")).isNull();
    assertThat(definitions.getDefaultValue("two")).isEqualTo("2");

    assertThat(definitions.getAll()).hasSize(3);
  }

  @Test
  public void validKey_throws_NPE_if_key_is_null() {
    PropertyDefinitions underTest = new PropertyDefinitions(System2.INSTANCE);

    assertThatThrownBy(() -> underTest.validKey(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("key can't be null");
  }

  @Test
  public void get_throws_NPE_if_key_is_null() {
    PropertyDefinitions underTest = new PropertyDefinitions(System2.INSTANCE);

    assertThatThrownBy(() -> underTest.get(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("key can't be null");
  }

  @Test
  public void get_trims_key_before_looking_for_replacement() {
    Random random = new Random();
    String key = RandomStringUtils.randomAlphanumeric(4);
    String deprecatedKey = RandomStringUtils.randomAlphanumeric(4);
    PropertyDefinitions underTest = new PropertyDefinitions(System2.INSTANCE, singletonList(
      PropertyDefinition.builder(key)
        .deprecatedKey(deprecatedKey)
        .build()));

    String untrimmedKey = blank(random) + deprecatedKey + blank(random);
    assertThat(underTest.get(untrimmedKey).key())
      .describedAs("expecting key %s being returned for get(%s)", key, untrimmedKey)
      .isEqualTo(key);
  }

  private static String blank(Random random) {
    StringBuilder b = new StringBuilder();
    IntStream.range(0, random.nextInt(3)).mapToObj(s -> " ").forEach(b::append);
    return b.toString();
  }

  @Test
  public void get_value_from_env() {
    System2 system = mock(System2.class);
    when(system.envVariable("FOO")).thenReturn("777");
    when(system.envVariable("ONE")).thenReturn("888");
    when(system.envVariable("SOME_COMPLETELY_RANDOM_ENV_VAR")).thenReturn("999");

    PropertyDefinitions underTest = new PropertyDefinitions(system, new PluginWithProperty(), new PluginWithProperties());

    assertThat(underTest.getValueFromEnv("foo")).hasValue("777");
    assertThat(underTest.getValueFromEnv("one")).hasValue("888");
    assertThat(underTest.getValueFromEnv("two")).isEmpty();
    assertThat(underTest.getValueFromEnv("some.unrecognizable.prop")).isEmpty();
  }

  @Test
  public void get_all_properties_set_in_env() {
    System2 system = mock(System2.class);
    when(system.envVariable("FOO")).thenReturn("777");
    when(system.envVariable("ONE")).thenReturn("888");
    when(system.envVariable("SOME_COMPLETELY_RANDOM_ENV_VAR")).thenReturn("999");

    PropertyDefinitions underTest = new PropertyDefinitions(system, new PluginWithProperty(), new PluginWithProperties());

    assertThat(underTest.getAllPropertiesSetInEnv()).containsExactlyInAnyOrderEntriesOf(ImmutableMap.of("foo", "777", "one", "888"));
  }

  @Property(key = "foo", name = "Foo")
  static final class PluginWithProperty {
  }

  @Properties({
    @Property(key = "one", name = "One"),
    @Property(key = "two", name = "Two", defaultValue = "2")
  })
  static final class PluginWithProperties {
  }

  @Properties({
    @Property(key = "inCateg", name = "In Categ", category = "categ"),
    @Property(key = "noCateg", name = "No categ")
  })
  static final class Categories {
  }

  @Properties({
    @Property(key = "global1", name = "Global1", category = "catGlobal1", global = true, project = false, module = false),
    @Property(key = "global2", name = "Global2", category = "catGlobal1", global = true, project = false, module = false),
    @Property(key = "global3", name = "Global3", category = "catGlobal2", global = true, project = false, module = false),
    @Property(key = "project", name = "Project", category = "catProject", global = false, project = true, module = false),
    @Property(key = "module", name = "Module", category = "catModule", global = false, project = false, module = true)
  })
  static final class ByCategory {
  }
}
