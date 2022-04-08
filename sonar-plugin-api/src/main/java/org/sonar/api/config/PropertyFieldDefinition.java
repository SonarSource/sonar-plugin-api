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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.PropertyField;
import org.sonar.api.PropertyType;

import static java.util.Arrays.asList;
import static org.sonar.api.utils.Preconditions.checkArgument;

/**
 * @since 3.3
 */
public final class PropertyFieldDefinition {
  private final String key;
  private final String name;
  private final String description;
  private final PropertyType type;
  private final List<String> options;

  private PropertyFieldDefinition(Builder builder) {
    this.key = builder.key;
    this.name = builder.name;
    this.description = builder.description;
    this.type = builder.type;
    this.options = builder.options;
  }

  static List<PropertyFieldDefinition> create(PropertyField[] fields) {
    List<PropertyFieldDefinition> definitions = new ArrayList<>();
    for (PropertyField field : fields) {
      definitions.add(PropertyFieldDefinition.build(field.key())
        .name(field.name())
        .description(field.description())
        .type(field.type())
        .options(field.options())
        .build());
    }
    return definitions;
  }

  public static Builder build(String key) {
    return new Builder(key);
  }

  public String key() {
    return key;
  }

  public String name() {
    return name;
  }

  public String description() {
    return description;
  }

  public PropertyType type() {
    return type;
  }

  public List<String> options() {
    return options;
  }

  public PropertyDefinition.Result validate(@Nullable String value) {
    return PropertyDefinition.validate(type, value, options);
  }

  public static class Builder {
    private String key;
    private String name;
    private String description;
    private PropertyType type;
    private List<String> options;

    private Builder(String key) {
      this.key = key;
      this.name = "";
      this.description = "";
      this.type = PropertyType.STRING;
      this.options = new ArrayList<>();
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder type(PropertyType type) {
      this.type = type;
      return this;
    }

    public Builder options(String... options) {
      this.options.addAll(asList(options));
      return this;
    }

    public Builder options(List<String> options) {
      this.options.addAll(options);
      return this;
    }

    public PropertyFieldDefinition build() {
      checkArgument(!StringUtils.isEmpty(key), "Key must be set");
      checkArgument(!StringUtils.isEmpty(name), "Name must be set");
      return new PropertyFieldDefinition(this);
    }
  }
}
