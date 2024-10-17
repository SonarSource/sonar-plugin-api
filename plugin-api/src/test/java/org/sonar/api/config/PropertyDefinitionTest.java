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
package org.sonar.api.config;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.junit.Test;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyField;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.PropertyDefinition.ConfigScope;
import org.sonar.api.utils.AnnotationUtils;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;


@SuppressWarnings({"removal"})
public class PropertyDefinitionTest {

  @Test
  public void should_override_toString() {
    PropertyDefinition def = PropertyDefinition.builder("hello").build();
    assertThat(def.toString()).isEqualTo("hello");
  }

  @Test
  public void should_create_property() {
    PropertyDefinition def = PropertyDefinition.builder("hello")
      .name("Hello")
      .defaultValue("world")
      .category("categ")
      .options("de", "en")
      .description("desc")
      .type(PropertyType.FLOAT)
      .onlyOnQualifiers(org.sonar.api.resources.Qualifiers.MODULE)
      .multiValues(true)
      .build();

    assertThat(def.key()).isEqualTo("hello");
    assertThat(def.name()).isEqualTo("Hello");
    assertThat(def.defaultValue()).isEqualTo("world");
    assertThat(def.category()).isEqualTo("categ");
    assertThat(def.options()).containsOnly("de", "en");
    assertThat(def.description()).isEqualTo("desc");
    assertThat(def.type()).isEqualTo(PropertyType.FLOAT);
    assertThat(def.global()).isFalse();
    assertThat(def.qualifiers()).containsOnly(org.sonar.api.resources.Qualifiers.MODULE);
    assertThat(def.configScopes()).containsOnly(ConfigScope.MODULE);
    assertThat(def.multiValues()).isTrue();
    assertThat(def.fields()).isEmpty();
  }

  @Test
  public void should_create_from_annotation() {
    Properties props = AnnotationUtils.getAnnotation(Init.class, Properties.class);
    Property prop = props.value()[0];

    PropertyDefinition def = PropertyDefinition.create(prop);

    assertThat(def.key()).isEqualTo("hello");
    assertThat(def.name()).isEqualTo("Hello");
    assertThat(def.defaultValue()).isEqualTo("world");
    assertThat(def.category()).isEqualTo("categ");
    assertThat(def.options()).containsOnly("de", "en");
    assertThat(def.description()).isEqualTo("desc");
    assertThat(def.type()).isEqualTo(PropertyType.FLOAT);
    assertThat(def.global()).isFalse();
    assertThat(def.qualifiers()).containsOnly(org.sonar.api.resources.Qualifiers.PROJECT, org.sonar.api.resources.Qualifiers.MODULE);
    assertThat(def.configScopes()).containsOnly(ConfigScope.PROJECT, ConfigScope.MODULE);
    assertThat(def.multiValues()).isTrue();
    assertThat(def.fields()).isEmpty();
  }

  @Test
  public void should_create_hidden_property() {
    PropertyDefinition def = PropertyDefinition.builder("hello")
      .name("Hello")
      .hidden()
      .build();

    assertThat(def.key()).isEqualTo("hello");
    assertThat(def.qualifiers()).isEmpty();
    assertThat(def.configScopes()).isEmpty();
    assertThat(def.global()).isFalse();
  }

  @Test
  public void should_create_property_with_default_values() {
    PropertyDefinition def = PropertyDefinition.builder("hello")
      .name("Hello")
      .build();

    assertThat(def.key()).isEqualTo("hello");
    assertThat(def.name()).isEqualTo("Hello");
    assertThat(def.defaultValue()).isEmpty();
    assertThat(def.category()).isEmpty();
    assertThat(def.options()).isEmpty();
    assertThat(def.description()).isEmpty();
    assertThat(def.type()).isEqualTo(PropertyType.STRING);
    assertThat(def.global()).isTrue();
    assertThat(def.qualifiers()).isEmpty();
    assertThat(def.configScopes()).isEmpty();
    assertThat(def.multiValues()).isFalse();
    assertThat(def.fields()).isEmpty();
  }

  @Test
  public void should_create_from_annotation_default_values() {
    Properties props = AnnotationUtils.getAnnotation(DefaultValues.class, Properties.class);
    Property prop = props.value()[0];

    PropertyDefinition def = PropertyDefinition.create(prop);

    assertThat(def.key()).isEqualTo("hello");
    assertThat(def.name()).isEqualTo("Hello");
    assertThat(def.defaultValue()).isEmpty();
    assertThat(def.category()).isEmpty();
    assertThat(def.options()).isEmpty();
    assertThat(def.description()).isEmpty();
    assertThat(def.type()).isEqualTo(PropertyType.STRING);
    assertThat(def.global()).isTrue();
    assertThat(def.qualifiers()).isEmpty();
    assertThat(def.configScopes()).isEmpty();
    assertThat(def.multiValues()).isFalse();
    assertThat(def.fields()).isEmpty();
  }

  @Test
  public void should_support_property_sets() {
    PropertyDefinition def = PropertyDefinition.builder("hello")
      .name("Hello")
      .fields(
        PropertyFieldDefinition.build("first").name("First").description("Description").options("A", "B").build(),
        PropertyFieldDefinition.build("second").name("Second").type(PropertyType.INTEGER).build())
      .build();

    assertThat(def.type()).isEqualTo(PropertyType.PROPERTY_SET);
    assertThat(def.fields()).hasSize(2);
    assertThat(def.fields().get(0).key()).isEqualTo("first");
    assertThat(def.fields().get(0).name()).isEqualTo("First");
    assertThat(def.fields().get(0).description()).isEqualTo("Description");
    assertThat(def.fields().get(0).type()).isEqualTo(PropertyType.STRING);
    assertThat(def.fields().get(0).options()).containsOnly("A", "B");
    assertThat(def.fields().get(1).key()).isEqualTo("second");
    assertThat(def.fields().get(1).name()).isEqualTo("Second");
    assertThat(def.fields().get(1).type()).isEqualTo(PropertyType.INTEGER);
    assertThat(def.fields().get(1).options()).isEmpty();
  }

  @Test
  public void should_support_property_sets_from_annotation() {
    Properties props = AnnotationUtils.getAnnotation(WithPropertySet.class, Properties.class);
    Property prop = props.value()[0];

    PropertyDefinition def = PropertyDefinition.create(prop);

    assertThat(def.type()).isEqualTo(PropertyType.PROPERTY_SET);
    assertThat(def.fields()).hasSize(2);
    assertThat(def.fields().get(0).key()).isEqualTo("first");
    assertThat(def.fields().get(0).name()).isEqualTo("First");
    assertThat(def.fields().get(0).description()).isEqualTo("Description");
    assertThat(def.fields().get(0).type()).isEqualTo(PropertyType.STRING);
    assertThat(def.fields().get(0).options()).containsOnly("A", "B");
    assertThat(def.fields().get(1).key()).isEqualTo("second");
    assertThat(def.fields().get(1).name()).isEqualTo("Second");
    assertThat(def.fields().get(1).type()).isEqualTo(PropertyType.INTEGER);
    assertThat(def.fields().get(1).options()).isEmpty();
  }

  @Test
  public void should_validate_string() {
    PropertyDefinition def = PropertyDefinition.builder("foo").name("foo").type(PropertyType.STRING).build();

    assertThat(def.validate(null).isValid()).isTrue();
    assertThat(def.validate("").isValid()).isTrue();
    assertThat(def.validate("   ").isValid()).isTrue();
    assertThat(def.validate("foo").isValid()).isTrue();
  }

  @Test
  public void should_validate_boolean() {
    PropertyDefinition def = PropertyDefinition.builder("foo").name("foo").type(PropertyType.BOOLEAN).build();

    assertThat(def.validate(null).isValid()).isTrue();
    assertThat(def.validate("").isValid()).isTrue();
    assertThat(def.validate("   ").isValid()).isTrue();
    assertThat(def.validate("true").isValid()).isTrue();
    assertThat(def.validate("false").isValid()).isTrue();

    assertThat(def.validate("foo").isValid()).isFalse();
    assertThat(def.validate("foo").getErrorKey()).isEqualTo("notBoolean");
  }

  @Test
  public void should_validate_integer() {
    PropertyDefinition def = PropertyDefinition.builder("foo").name("foo").type(PropertyType.INTEGER).build();

    assertThat(def.validate(null).isValid()).isTrue();
    assertThat(def.validate("").isValid()).isTrue();
    assertThat(def.validate("   ").isValid()).isTrue();
    assertThat(def.validate("123456").isValid()).isTrue();

    assertThat(def.validate("foo").isValid()).isFalse();
    assertThat(def.validate("foo").getErrorKey()).isEqualTo("notInteger");
  }

  @Test
  public void should_validate_float() {
    PropertyDefinition def = PropertyDefinition.builder("foo").name("foo").type(PropertyType.FLOAT).build();

    assertThat(def.validate(null).isValid()).isTrue();
    assertThat(def.validate("").isValid()).isTrue();
    assertThat(def.validate("   ").isValid()).isTrue();
    assertThat(def.validate("123456").isValid()).isTrue();
    assertThat(def.validate("3.14").isValid()).isTrue();

    assertThat(def.validate("foo").isValid()).isFalse();
    assertThat(def.validate("foo").getErrorKey()).isEqualTo("notFloat");
  }

  @Test
  public void validate_regular_expression() {
    PropertyDefinition def = PropertyDefinition.builder("foo").name("foo").type(PropertyType.REGULAR_EXPRESSION).build();

    assertThat(def.validate(null).isValid()).isTrue();
    assertThat(def.validate("").isValid()).isTrue();
    assertThat(def.validate("   ").isValid()).isTrue();
    assertThat(def.validate("[a-zA-Z]").isValid()).isTrue();

    assertThat(def.validate("[a-zA-Z").isValid()).isFalse();
    assertThat(def.validate("[a-zA-Z").getErrorKey()).isEqualTo("notRegexp");
  }

  @Test
  public void validate_email() {
    PropertyDefinition def = PropertyDefinition.builder("foo").name("foo").type(PropertyType.EMAIL).build();

    assertThat(def.validate(null).isValid()).isTrue();
    assertThat(def.validate("").isValid()).isTrue();
    assertThat(def.validate("   ").isValid()).isTrue();
    assertThat(def.validate("test@sonarsource.com").isValid()).isTrue();
    assertThat(def.validate("test@localhost").isValid()).isTrue();

    assertThat(def.validate("test@sonarsource.sonar").isValid()).isFalse();
    assertThat(def.validate("test@sonarsource.sonar").getErrorKey()).isEqualTo("notEmail");
    assertThat(def.validate("test@sonarsource.com,test@sonarsource.com").isValid()).isFalse();
    assertThat(def.validate("@sonarsource.com").isValid()).isFalse();
    assertThat(def.validate("test").isValid()).isFalse();
  }

  @Test
  public void should_validate_single_select_list() {
    PropertyDefinition def = PropertyDefinition.builder("foo").name("foo").type(PropertyType.SINGLE_SELECT_LIST).options("de", "en").build();

    assertThat(def.validate(null).isValid()).isTrue();
    assertThat(def.validate("").isValid()).isTrue();
    assertThat(def.validate("   ").isValid()).isTrue();
    assertThat(def.validate("de").isValid()).isTrue();
    assertThat(def.validate("en").isValid()).isTrue();

    assertThat(def.validate("fr").isValid()).isFalse();
    assertThat(def.validate("fr").getErrorKey()).isEqualTo("notInOptions");
  }

  @Test
  public void should_auto_detect_password_type() {
    PropertyDefinition def = PropertyDefinition.builder("scm.password.secured").name("SCM password").build();

    assertThat(def.key()).isEqualTo("scm.password.secured");
    assertThat(def.type()).isEqualTo(PropertyType.PASSWORD);
  }

  @Test
  public void should_create_json_property_type() {
    Builder builder = PropertyDefinition.builder("json-prop").type(PropertyType.JSON).multiValues(false);
    assertThatCode(builder::build)
      .doesNotThrowAnyException();
  }

  @Test
  public void should_not_authorise_empty_key() {
    Builder builder = PropertyDefinition.builder(null);
    assertThatThrownBy(builder::build)
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Key must be set");
  }

  @Test
  public void should_not_create_json_multivalue() {
    Builder builder = PropertyDefinition.builder("json-prop").type(PropertyType.JSON).multiValues(true);
    assertThatThrownBy(builder::build)
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Multivalues are not allowed to be defined for JSON-type property.");
  }

  @Test
  public void should_not_authorize_defining_on_qualifiers_and_hidden() {
    Builder builder = PropertyDefinition.builder("foo").name("foo").onQualifiers(org.sonar.api.resources.Qualifiers.PROJECT).hidden();
    assertThatThrownBy(builder::build)
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Cannot be hidden and defining qualifiers on which to display");
  }

  @Test
  public void should_not_authorize_defining_ony_on_qualifiers_and_hidden() {
    Builder builder = PropertyDefinition.builder("foo").name("foo").onlyOnQualifiers(org.sonar.api.resources.Qualifiers.PROJECT).hidden();
    assertThatThrownBy(builder::build)
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Cannot be hidden and defining qualifiers on which to display");
  }

  @Test
  public void should_not_authorize_defining_on_qualifiers_and_only_on_qualifiers() {
    Builder builder = PropertyDefinition.builder("foo").name("foo").onQualifiers(org.sonar.api.resources.Qualifiers.MODULE)
      .onlyOnQualifiers(org.sonar.api.resources.Qualifiers.PROJECT);
    assertThatThrownBy(builder::build)
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Cannot use both forQualifiers and onlyForQualifiers");
  }

  private static final Set<ConfigScope> ALLOWED_CONFIG_SCOPES = ImmutableSet.copyOf(ConfigScope.values());
  private static final Set<String> OLD_ALLOWED_QUALIFIERS = ImmutableSet.of("TRK", "VW", "BRC", "SVW");
  private static final Set<String> NOT_ALLOWED_QUALIFIERS = ImmutableSet.of("FIL", "DIR", "UTS", "", randomAlphabetic(3));

  @Test
  public void onQualifiers_with_varargs_parameter_fails_with_IAE_when_qualifier_is_not_supported() {
    failsWithIAEForUnsupportedQualifiers((builder, qualifier) -> builder.onQualifiers(qualifier));
    failsWithIAEForUnsupportedQualifiers((builder, qualifier) -> builder.onQualifiers("TRK", qualifier, "BRC"));
  }

  @Test
  public void onQualifiers_with_list_parameter_fails_with_IAE_when_qualifier_is_not_supported() {
    failsWithIAEForUnsupportedQualifiers((builder, qualifier) -> builder.onQualifiers(Collections.singletonList(qualifier)));
    failsWithIAEForUnsupportedQualifiers((builder, qualifier) -> builder.onQualifiers(Arrays.asList("TRK", qualifier, "BRC")));
  }

  @Test
  public void onlyOnQualifiers_with_varargs_parameter_fails_with_IAE_when_qualifier_is_not_supported() {
    failsWithIAEForUnsupportedQualifiers((builder, qualifier) -> builder.onlyOnQualifiers(qualifier));
    failsWithIAEForUnsupportedQualifiers((builder, qualifier) -> builder.onlyOnQualifiers("TRK", qualifier, "BRC"));
  }

  @Test
  public void onlyOnQualifiers_with_list_parameter_fails_with_IAE_when_qualifier_is_not_supported() {
    failsWithIAEForUnsupportedQualifiers((builder, qualifier) -> builder.onlyOnQualifiers(Collections.singletonList(qualifier)));
    failsWithIAEForUnsupportedQualifiers((builder, qualifier) -> builder.onlyOnQualifiers(Arrays.asList("TRK", qualifier, "BRC")));
  }

  @Test
  public void onQualifiers_with_varargs_parameter_fails_with_NPE_when_qualifier_is_null() {
    failsWithNPEForNullDeprecatedQualifiers(builder -> builder.onQualifiers((String) null));
    failsWithNPEForNullDeprecatedQualifiers(builder -> builder.onQualifiers("TRK", null, "BRC"));
  }

  @Test
  public void onQualifiers_with_list_parameter_fails_with_NPE_when_qualifier_is_null() {
    failsWithNPEForNullDeprecatedQualifiers(builder -> builder.onQualifiers(Collections.singletonList(null)));
    failsWithNPEForNullDeprecatedQualifiers(builder -> builder.onlyOnQualifiers("TRK", null, "BRC"));
  }

  @Test
  public void onlyOnQualifiers_with_varargs_parameter_fails_with_NPE_when_qualifier_is_null() {
    failsWithNPEForNullDeprecatedQualifiers(builder -> builder.onlyOnQualifiers((String) null));
    failsWithNPEForNullDeprecatedQualifiers(builder -> builder.onlyOnQualifiers("TRK", null, "BRC"));
  }

  @Test
  public void onlyOnQualifiers_with_list_parameter_fails_with_NPE_when_qualifier_is_null() {
    failsWithNPEForNullDeprecatedQualifiers(builder -> builder.onlyOnQualifiers(Collections.singletonList(null)));
    failsWithNPEForNullDeprecatedQualifiers(builder -> builder.onlyOnQualifiers(Arrays.asList("TRK", null, "BRC")));
  }

  @Test
  public void onQualifiers_with_varargs_parameter_accepts_supported_qualifiers() {
    acceptsSupportedDeprecatedQualifiers(Builder::onQualifiers);
    acceptsSupportedQualifiers(Builder::onConfigScopes);
  }

  @Test
  public void onQualifiers_with_list_parameter_accepts_supported_qualifiers() {
    acceptsSupportedDeprecatedQualifiers((builder, qualifier) -> builder.onQualifiers(Collections.singletonList(qualifier)));
    acceptsSupportedQualifiers((builder, configScope) -> builder.onConfigScopes(Collections.singletonList(configScope)));
  }

  @Test
  public void onlyOnQualifiers_with_varargs_parameter_accepts_supported_qualifiers() {
    acceptsSupportedDeprecatedQualifiers(Builder::onlyOnQualifiers);
    acceptsSupportedQualifiers(Builder::onlyOnConfigScopes);
  }

  @Test
  public void onlyOnQualifiers_with_list_parameter_accepts_supported_qualifiers() {
    acceptsSupportedDeprecatedQualifiers((builder, qualifier) -> builder.onlyOnQualifiers(Collections.singletonList(qualifier)));
    acceptsSupportedQualifiers((builder, configScope) -> builder.onlyOnConfigScopes(Collections.singletonList(configScope)));
  }

  private static void failsWithIAEForUnsupportedQualifiers(BiConsumer<PropertyDefinition.Builder, String> biConsumer) {
    PropertyDefinition.Builder builder = PropertyDefinition.builder(randomAlphabetic(3));
    NOT_ALLOWED_QUALIFIERS.forEach(qualifier -> {
      try {
        biConsumer.accept(builder, qualifier);
        fail("A IllegalArgumentException should have been thrown for qualifier " + qualifier);
      } catch (IllegalArgumentException e) {
        assertThat(e).hasMessage("Qualifier must be one of [TRK, VW, BRC, SVW, APP]");
      }
    });
  }

  private static void acceptsSupportedDeprecatedQualifiers(BiConsumer<PropertyDefinition.Builder, String> biConsumer) {
    PropertyDefinition.Builder builder = PropertyDefinition.builder(randomAlphabetic(3));
    OLD_ALLOWED_QUALIFIERS.forEach(qualifier -> biConsumer.accept(builder, qualifier));
  }

  private static void acceptsSupportedQualifiers(BiConsumer<PropertyDefinition.Builder, ConfigScope> biConsumer) {
    PropertyDefinition.Builder builder = PropertyDefinition.builder(randomAlphabetic(3));
    ALLOWED_CONFIG_SCOPES.forEach(configScope -> biConsumer.accept(builder, configScope));
  }

  private static void failsWithNPEForNullDeprecatedQualifiers(Consumer<PropertyDefinition.Builder> consumer) {
    PropertyDefinition.Builder builder = PropertyDefinition.builder(randomAlphabetic(3));
    NOT_ALLOWED_QUALIFIERS.forEach(qualifier -> {
      try {
        consumer.accept(builder);
        fail("A NullPointerException should have been thrown for null qualifier");
      } catch (NullPointerException e) {
        assertThat(e).hasMessage("Qualifier cannot be null");
      }
    });
  }

  @Properties(@Property(key = "hello", name = "Hello", defaultValue = "world", description = "desc",
    options = {"de", "en"}, category = "categ", type = PropertyType.FLOAT, global = false, project = true, module = true, multiValues = true))
  static class Init {
  }

  @Properties(@Property(key = "hello", name = "Hello", fields = {
    @PropertyField(key = "first", name = "First", description = "Description", options = {"A", "B"}),
    @PropertyField(key = "second", name = "Second", type = PropertyType.INTEGER)}))
  static class WithPropertySet {
  }

  @Properties(@Property(key = "hello", name = "Hello"))
  static class DefaultValues {
  }

}
