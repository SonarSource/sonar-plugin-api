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
package org.sonar.api.batch;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for all the components available in the container of the scanner (code analyzer). Note that
 * injection of dependencies by constructor is used :
 * <pre>
 *   {@literal @}ScannerSide
 *   public class Foo {
 *
 *   }
 *   {@literal @}ScannerSide
 *   public class Bar {
 *     private final Foo foo;
 *     public Bar(Foo f) {
 *       this.foo = f;
 *     }
 *   }
 *
 * </pre>
 *
 * @since 6.0
 * @deprecated since 7.6 use {@link org.sonar.api.scanner.ScannerSide} that will move the component to the project container
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScannerSide {
}
