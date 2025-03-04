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
package org.sonar.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Property value can be set in different ways :
 * <ul>
 * <li>System property</li>
 * <li>Batch command-line (-Dfoo=bar in Maven or sonar-runner)</li>
 * <li>Maven pom.xml (element {@literal <properties>})</li>
 * <li>Maven settings.xml</li>
 * <li>SonarQube web administration console</li>
 * </ul>
 *
 * @since 1.10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Property {

  /**
   * Unique key within all plugins. It's recommended to prefix the key by 'sonar.' and the plugin name. Examples :
   * 'sonar.cobertura.reportPath' and 'sonar.cpd.minimumTokens'.
   */
  String key();

  /**
   * The empty string "" is considered as null, so it's not possible to have empty strings for default values.
   */
  String defaultValue() default "";

  String name();

  String description() default "";

  /**
   * @since 2.11
   * @see org.sonar.api.config.PropertyDefinition#category()
   */
  String category() default "";

  /**
   * Is the property displayed in project settings page ?
   */
  boolean project() default false;

  /**
   * Is the property displayed in module settings page ? A module is a maven sub-project.
   * @deprecated since 10.13. Modules are not used anymore.
   */
  @Deprecated(since = "10.13", forRemoval = true)
  boolean module() default false;

  /**
   * Is the property displayed in global settings page ?
   */
  boolean global() default true;

  /**
   * @since 3.0
   */
  PropertyType type() default PropertyType.STRING;

  /**
   * Options for *_LIST types
   *
   * @since 3.0  Options for property of type {@link PropertyType#SINGLE_SELECT_LIST}
   * For example {"property_1", "property_3", "property_3"}).
   */
  String[] options() default {};

  /**
   * Can the property take multiple values. Eg: list of email addresses.
   *
   * @since 3.3
   */
  boolean multiValues() default false;

  /**
   * A Property with fields is considered a property set.
   *
   * @since 3.3
   */
  PropertyField[] fields() default {};

  /**
   * Relocation of key.
   * @since 3.4
   */
  String deprecatedKey() default "";
}
