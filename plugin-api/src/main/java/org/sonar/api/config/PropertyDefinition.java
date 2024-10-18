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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.sonar.api.ExtensionPoint;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.ce.ComputeEngineSide;
import org.sonar.api.scanner.ScannerSide;
import org.sonar.api.server.ServerSide;
import org.sonarsource.api.sonarlint.SonarLintSide;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.sonar.api.PropertyType.BOOLEAN;
import static org.sonar.api.PropertyType.EMAIL;
import static org.sonar.api.PropertyType.FLOAT;
import static org.sonar.api.PropertyType.INTEGER;
import static org.sonar.api.PropertyType.JSON;
import static org.sonar.api.PropertyType.PROPERTY_SET;
import static org.sonar.api.PropertyType.REGULAR_EXPRESSION;
import static org.sonar.api.PropertyType.SINGLE_SELECT_LIST;
import static org.sonar.api.utils.Preconditions.checkArgument;

/**
 * Declare a plugin property. Values are available at runtime through the component {@link Configuration}.
 * <br>
 * It's the programmatic alternative to the annotation {@link org.sonar.api.Property}. It is more
 * testable and adds new features like sub-categories and ordering.
 * <br>
 * Example:
 * <pre><code>
 *   public class MyPlugin extends SonarPlugin {
 *     public List getExtensions() {
 *       return Arrays.asList(
 *         PropertyDefinition.builder("sonar.foo").name("Foo").build(),
 *         PropertyDefinition.builder("sonar.bar").name("Bar").defaultValue("123").type(PropertyType.INTEGER).build()
 *       );
 *     }
 *   }
 * </code></pre>
 * <br>
 * Keys in localization bundles are:
 * <ul>
 * <li>{@code property.<key>.name} is the label of the property</li>
 * <li>{@code property.<key>.description} is the optional description of the property</li>
 * <li>{@code property.category.<category>} is the category label</li>
 * <li>{@code property.category.<category>.description} is the category description</li>
 * <li>{@code property.category.<category>.<subcategory>} is the sub-category label</li>
 * <li>{@code property.category.<category>.<subcategory>.description} is the sub-category description</li>
 * </ul>
 *
 * @since 3.6
 */
@ScannerSide
@ServerSide
@ComputeEngineSide
@SonarLintSide
@ExtensionPoint
@SuppressWarnings({"removal"})
public final class PropertyDefinition {

  public enum ConfigScope {
    PROJECT(org.sonar.api.resources.Qualifiers.PROJECT),
    VIEW(org.sonar.api.resources.Qualifiers.VIEW),
    /**
     * @deprecated since 10.13. No more modules on the server side.
     */
    @Deprecated(since = "10.13", forRemoval = true)
    MODULE(org.sonar.api.resources.Qualifiers.MODULE),
    SUB_VIEW(org.sonar.api.resources.Qualifiers.SUBVIEW),
    APP(org.sonar.api.resources.Qualifiers.APP);

    private final String key;

    ConfigScope(String key) {
      this.key = key;
    }

    @CheckForNull
    private static ConfigScope fromKey(String key) {
      return Arrays.stream(values())
        .filter(configScope -> configScope.key.equals(key))
        .findAny()
        .orElse(null);
    }

    private String getKey() {
      return key;
    }
  }

  private final String key;
  private final String defaultValue;
  private final String name;
  private final PropertyType type;
  private final List<String> options;
  private final String description;
  /**
   * @see org.sonar.api.config.PropertyDefinition.Builder#category(String)
   */
  private final String category;
  private final List<ConfigScope> configScopes;
  private final boolean global;
  private final boolean multiValues;
  private final String deprecatedKey;
  private final List<PropertyFieldDefinition> fields;
  /**
   * @see org.sonar.api.config.PropertyDefinition.Builder#subCategory(String)
   */
  private final String subCategory;
  private final int index;

  private PropertyDefinition(Builder builder) {
    this.key = builder.key;
    this.name = builder.name;
    this.description = builder.description;
    this.defaultValue = builder.defaultValue;
    this.category = builder.category;
    this.subCategory = builder.subCategory;
    this.global = builder.global;
    this.type = builder.type;
    this.options = builder.options;
    this.multiValues = builder.multiValues;
    this.fields = builder.fields;
    this.deprecatedKey = builder.deprecatedKey;
    this.configScopes = new ArrayList<>(builder.onConfigScopes);
    this.configScopes.addAll(builder.onlyOnConfigScopes);
    this.index = builder.index;
  }

  /**
   * @param key the unique property key. If it ends with ".secured" then users need the
   *            administration permission to access the value.
   */
  public static Builder builder(String key) {
    return new Builder(key);
  }

  static PropertyDefinition create(Property annotation) {
    Builder builder = PropertyDefinition.builder(annotation.key())
      .name(annotation.name())
      .defaultValue(annotation.defaultValue())
      .description(annotation.description())
      .category(annotation.category())
      .type(annotation.type())
      .options(asList(annotation.options()))
      .multiValues(annotation.multiValues())
      .fields(PropertyFieldDefinition.create(annotation.fields()))
      .deprecatedKey(annotation.deprecatedKey());
    List<ConfigScope> configScopes = new ArrayList<>();
    if (annotation.project()) {
      configScopes.add(ConfigScope.PROJECT);
    }
    if (annotation.module()) {
      configScopes.add(ConfigScope.MODULE);
    }
    if (annotation.global()) {
      builder.onConfigScopes(configScopes);
    } else {
      builder.onlyOnConfigScopes(configScopes);
    }
    return builder.build();
  }

  public static Result validate(PropertyType type, @Nullable String value, List<String> options) {
    if (isBlank(value)) {
      return Result.SUCCESS;
    }

    EnumMap<PropertyType, Function<String, Result>> validations = createValidations(options);
    return validations.getOrDefault(type, aValue -> Result.SUCCESS).apply(value);
  }

  private static EnumMap<PropertyType, Function<String, Result>> createValidations(List<String> options) {
    EnumMap<PropertyType, Function<String, Result>> map = new EnumMap<>(PropertyType.class);
    map.put(BOOLEAN, validateBoolean());
    map.put(INTEGER, validateInteger());
    map.put(FLOAT, validateFloat());
    map.put(REGULAR_EXPRESSION, validateRegexp());
    map.put(EMAIL, validateEmail());
    map.put(SINGLE_SELECT_LIST,
      aValue -> options.contains(aValue) ? Result.SUCCESS : Result.newError("notInOptions"));
    return map;
  }

  private static Function<String, Result> validateBoolean() {
    return value -> {
      if (!StringUtils.equalsIgnoreCase(value, "true") && !StringUtils.equalsIgnoreCase(value, "false")) {
        return Result.newError("notBoolean");
      }
      return Result.SUCCESS;
    };
  }

  private static Function<String, Result> validateInteger() {
    return value -> {
      if (!NumberUtils.isDigits(value)) {
        return Result.newError("notInteger");
      }
      return Result.SUCCESS;
    };
  }

  private static Function<String, Result> validateFloat() {
    return value -> {
      try {
        Double.parseDouble(value);
        return Result.SUCCESS;
      } catch (NumberFormatException e) {
        return Result.newError("notFloat");
      }
    };
  }

  private static Function<String, Result> validateRegexp() {
    return value -> {
      try {
        Pattern.compile(value);
        return Result.SUCCESS;
      } catch (PatternSyntaxException e) {
        return Result.newError("notRegexp");
      }
    };
  }

  private static Function<String, Result> validateEmail() {
    return value -> {
      if (!EmailValidator.getInstance(true, true).isValid(value)) {
        return Result.newError("notEmail");
      }
      return Result.SUCCESS;
    };
  }

  public Result validate(@Nullable String value) {
    return validate(type, value, options);
  }

  /**
   * Unique key within all plugins. It's recommended to prefix the key by 'sonar.' and the plugin name. Examples :
   * 'sonar.cobertura.reportPath' and 'sonar.cpd.minimumTokens'.
   */
  public String key() {
    return key;
  }

  public String defaultValue() {
    return defaultValue;
  }

  public String name() {
    return name;
  }

  public PropertyType type() {
    return type;
  }

  /**
   * Options for *_LIST types
   * <br>
   * Options for property of type {@link PropertyType#SINGLE_SELECT_LIST}.<br>
   * For example {"property_1", "property_3", "property_3"}).
   * <br>
   */
  public List<String> options() {
    return options;
  }

  public String description() {
    return description;
  }

  /**
   * Category where the property appears in settings pages. By default, equal to plugin name.
   */
  public String category() {
    return category;
  }

  /**
   * Sub-category where property appears in settings pages. By default, sub-category is the category.
   */
  public String subCategory() {
    return subCategory;
  }

  /**
   * @deprecated since 10.13. Use {@link #configScopes()} instead.
   */
  @Deprecated(since = "10.13", forRemoval = true)
  public List<String> qualifiers() {
    return configScopes.stream().map(ConfigScope::getKey).collect(Collectors.toList());
  }

  /**
   * Qualifiers that can display this property
   *
   * @since 10.13
   */
  public List<ConfigScope> configScopes() {
    return configScopes;
  }

  /**
   * Is the property displayed in global settings page ?
   */
  public boolean global() {
    return global;
  }

  public boolean multiValues() {
    return multiValues;
  }

  public List<PropertyFieldDefinition> fields() {
    return fields;
  }

  public String deprecatedKey() {
    return deprecatedKey;
  }

  /**
   * Order to display properties in Sonar UI. When two properties have the same index then it is sorted by
   * lexicographic order of property name.
   */
  public int index() {
    return index;
  }

  @Override
  public String toString() {
    return key;
  }

  public static final class Result {
    private static final Result SUCCESS = new Result(null);
    private final String errorKey;

    private Result(@Nullable String errorKey) {
      this.errorKey = errorKey;
    }

    private static Result newError(String key) {
      return new Result(key);
    }

    public boolean isValid() {
      return StringUtils.isBlank(errorKey);
    }

    @Nullable
    public String getErrorKey() {
      return errorKey;
    }
  }

  public static class Builder {
    private final String key;
    private String name = "";
    private String description = "";
    private String defaultValue = "";
    /**
     * @see PropertyDefinition.Builder#category(String)
     */
    private String category = "";
    /**
     * @see PropertyDefinition.Builder#subCategory(String)
     */
    private String subCategory = "";
    private final List<ConfigScope> onConfigScopes = new ArrayList<>();
    private final List<ConfigScope> onlyOnConfigScopes = new ArrayList<>();
    private boolean global = true;
    private PropertyType type = PropertyType.STRING;
    private final List<String> options = new ArrayList<>();
    private boolean multiValues = false;
    private final List<PropertyFieldDefinition> fields = new ArrayList<>();
    private String deprecatedKey = "";
    private boolean hidden = false;
    private int index = 999;

    private Builder(String key) {
      this.key = key;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * @see PropertyDefinition#name()
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * @see PropertyDefinition#defaultValue()
     */
    public Builder defaultValue(String defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    /**
     * @see PropertyDefinition#category()
     */
    public Builder category(String category) {
      this.category = category;
      return this;
    }

    /**
     * @see PropertyDefinition#subCategory()
     */
    public Builder subCategory(String subCategory) {
      this.subCategory = subCategory;
      return this;
    }

    /**
     * The property will be available in the General Settings AND in the given scopes.
     * <br>
     * For example @{code onConfigScopes(ConfigScope.PROJECT)} allows to configure the
     * property in the General Settings and in the Project Settings.
     * <br>
     * See supported constant values in {@link ConfigScope}. By default, a property is available
     * only in the General Settings.
     *
     * @throws IllegalArgumentException only qualifiers {@link ConfigScope#PROJECT}, {@link ConfigScope#APP},
     *                                  {@link ConfigScope#VIEW} and {@link ConfigScope#SUB_VIEW} are allowed.
     * @since 10.13
     */
    public Builder onConfigScopes(ConfigScope first, ConfigScope... rest) {
      addQualifiers(this.onConfigScopes, first, rest);
      this.global = true;
      return this;
    }

    /**
     * @deprecated since 10.13. Use {@link #onConfigScopes(ConfigScope, ConfigScope...)} instead.
     */
    @Deprecated(since = "10.13", forRemoval = true)
    public Builder onQualifiers(String first, String... rest) {
      addQualifiers(this.onConfigScopes, first, rest);
      this.global = true;
      return this;
    }

    /**
     * The property will be available in the General Settings AND in the given scopes.
     * <br>
     * For example @{code onConfigScopes(Arrays.asList(ConfigScope.PROJECT))} allows to configure the
     * property in the General Settings and in the Project Settings.
     * <br>
     * See supported constant values in {@link ConfigScope}. By default, a property is available
     * only in the General Settings.
     *
     * @throws IllegalArgumentException only qualifiers {@link ConfigScope#PROJECT}, {@link ConfigScope#APP},
     *                                  {@link ConfigScope#VIEW} and {@link ConfigScope#SUB_VIEW} are allowed.
     * @since 10.13
     */
    public Builder onConfigScopes(Collection<ConfigScope> configScopes) {
      addQualifiers(this.onConfigScopes, configScopes);
      this.global = true;
      return this;
    }

    /**
     * @deprecated since 10.13. Use {@link #onConfigScopes(Collection)} instead.
     */
    @Deprecated(since = "10.13", forRemoval = true)
    public Builder onQualifiers(List<String> qualifiers) {
      addQualifiers(this.onConfigScopes, qualifiers);
      this.global = true;
      return this;
    }

    /**
     * The property will be configurable in the given scopes, but NOT in General Settings.
     * <br>
     * For example @{code onlyOnConfigScopes(ConfigScope.PROJECT)} allows to configure the
     * property in Project Settings only.
     * <br>
     * See supported constant values in {@link ConfigScope}. By default, a property is available
     * only in the General Settings.
     *
     * @throws IllegalArgumentException only qualifiers {@link ConfigScope#PROJECT}, {@link ConfigScope#APP},
     *                                  {@link ConfigScope#VIEW} and {@link ConfigScope#SUB_VIEW} are allowed.
     * @since 10.13
     */
    public Builder onlyOnConfigScopes(ConfigScope first, ConfigScope... rest) {
      addQualifiers(this.onlyOnConfigScopes, first, rest);
      this.global = false;
      return this;
    }

    /**
     * @deprecated since 10.13. Use {@link #onlyOnConfigScopes(ConfigScope, ConfigScope...)} instead.
     */
    @Deprecated(since = "10.13", forRemoval = true)
    public Builder onlyOnQualifiers(String first, String... rest) {
      addQualifiers(this.onlyOnConfigScopes, first, rest);
      this.global = false;
      return this;
    }

    /**
     * The property will be configurable in the given scopes, but NOT in General Settings.
     * <br>
     * For example @{code onlyOnConfigScopes(Arrays.asList(ConfigScope.PROJECT))} allows to configure the
     * property in Project Settings only.
     * <br>
     * See supported constant values in {@link ConfigScope}. By default, a property is available
     * only in the General Settings.
     *
     * @throws IllegalArgumentException only qualifiers {@link ConfigScope#PROJECT PROJECT}, {@link ConfigScope#APP APP},
     *                                  {@link ConfigScope#VIEW VIEW} and {@link ConfigScope#SUB_VIEW SVW} are allowed.
     * @since 10.13
     */
    public Builder onlyOnConfigScopes(Collection<ConfigScope> configScopes) {
      addQualifiers(this.onlyOnConfigScopes, configScopes);
      this.global = false;
      return this;
    }

    /**
     * @deprecated since 10.13. Use {@link #onlyOnConfigScopes(Collection)} instead.
     */
    @Deprecated(since = "10.13", forRemoval = true)
    public Builder onlyOnQualifiers(List<String> qualifiers) {
      addQualifiers(this.onlyOnConfigScopes, qualifiers);
      this.global = false;
      return this;
    }

    private static void addQualifiers(List<ConfigScope> target, ConfigScope first, ConfigScope... rest) {
      List<ConfigScope> configScopes = new ArrayList<>();
      configScopes.add(first);
      configScopes.addAll(Arrays.asList(rest));
      addQualifiers(target, configScopes);
    }

    private static void addQualifiers(List<ConfigScope> target, String first, String... rest) {
      List<ConfigScope> configScopes = new ArrayList<>();
      configScopes.add(validateQualifier(first));
      for (String qualifier : rest) {
        configScopes.add(validateQualifier(qualifier));
      }
      addQualifiers(target, configScopes);
    }

    private static void addQualifiers(List<ConfigScope> target, List<String> qualifiers) {
      for (String qualifier : qualifiers) {
        target.add(validateQualifier(qualifier));
      }
    }

    private static void addQualifiers(List<ConfigScope> target, Collection<ConfigScope> configScopes) {
      target.addAll(configScopes);
    }

    private static ConfigScope validateQualifier(@Nullable String qualifierStr) {
      requireNonNull(qualifierStr, "Qualifier cannot be null");
      var qualifier = ConfigScope.fromKey(qualifierStr);
      checkArgument(qualifier != null, "Qualifier must be one of %s", Arrays.stream(ConfigScope.values()).map(ConfigScope::getKey).collect(Collectors.toList()));
      return qualifier;
    }

    /**
     * @see org.sonar.api.config.PropertyDefinition#type()
     */
    public Builder type(PropertyType type) {
      this.type = type;
      return this;
    }

    public Builder options(String first, String... rest) {
      this.options.add(first);
      options.addAll(asList(rest));
      return this;
    }

    public Builder options(List<String> options) {
      this.options.addAll(options);
      return this;
    }

    public Builder multiValues(boolean multiValues) {
      this.multiValues = multiValues;
      return this;
    }

    public Builder fields(PropertyFieldDefinition first, PropertyFieldDefinition... rest) {
      this.fields.add(first);
      this.fields.addAll(asList(rest));
      return this;
    }

    public Builder fields(List<PropertyFieldDefinition> fields) {
      this.fields.addAll(fields);
      return this;
    }

    public Builder deprecatedKey(String deprecatedKey) {
      this.deprecatedKey = deprecatedKey;
      return this;
    }

    /**
     * Flag the property as hidden. Hidden properties are not displayed in Settings pages
     * but allow plugins to benefit from type and default values when calling {@link Configuration}.
     */
    public Builder hidden() {
      this.hidden = true;
      return this;
    }

    /**
     * Set the order index in Settings pages. A property with a lower index is displayed
     * before properties with higher index.
     */
    public Builder index(int index) {
      this.index = index;
      return this;
    }

    public PropertyDefinition build() {
      checkArgument(!isEmpty(key), "Key must be set");
      fixType(key, type);
      checkArgument(onConfigScopes.isEmpty() || onlyOnConfigScopes.isEmpty(), "Cannot use both forQualifiers and onlyForQualifiers");
      checkArgument(!hidden || (onConfigScopes.isEmpty() && onlyOnConfigScopes.isEmpty()), "Cannot be hidden and defining qualifiers on which to display");
      checkArgument(!JSON.equals(type) || !multiValues, "Multivalues are not allowed to be defined for JSON-type property.");
      if (hidden) {
        global = false;
      }
      if (!fields.isEmpty()) {
        type = PROPERTY_SET;
      }
      return new PropertyDefinition(this);
    }

    private void fixType(String key, PropertyType type) {
      // Auto-detect passwords and licenses for old versions of plugins that
      // do not declare property types
      if (type == PropertyType.STRING && StringUtils.endsWith(key, ".password.secured")) {
        this.type = PropertyType.PASSWORD;
      }
    }
  }
}
