/*
 * Sonar Plugin API
 * Copyright (C) 2009-2023 SonarSource SA
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

import org.sonar.api.ExtensionPoint;
import org.sonar.api.scanner.ScannerSide;
import org.sonar.api.ce.ComputeEngineSide;
import org.sonar.api.server.ServerSide;
import org.sonarsource.api.sonarlint.SonarLintSide;

/**
 * The extension point to define a new language. A Language is defined by a key and a name (aka label).
 * <br/><br/>
 * When source files are analyzed on SonarLint or on scanner side, they will be optionally assigned to a language. A given source file
 * (see {@link org.sonar.api.batch.fs.InputFile}) can only have one language (or no language).
 * Implementors can declare file extensions (using {@link #getFileSuffixes()})
 * or filename patterns (using {@link #filenamePatterns()} that will be used to decide which language should be associated to a file.
 * <br/>
 * Since a source file can only have one language, patterns should match disjoint sets of files.
 *
 * @since 1.10
 */
@ScannerSide
@ServerSide
@SonarLintSide
@ComputeEngineSide
@ExtensionPoint
public interface Language {

  /**
   * For example "java". Should not be more than 20 chars.
   */
  String getKey();

  /**
   * For example "Java"
   */
  String getName();

  /**
   * Filename extensions, without the dot. For example <code>["jav", "java"]</code>. Source files having any of those extensions will
   * be associated to this language. This is equivalent to have {@link #filenamePatterns()}
   * returning <code>["&#42;&#42;/&#42;.jav", "&#42;&#42;/&#42;.java"]</code>.<br/>
   * The filename extension matching is case-insensitive, so declaring <code>["java"]</code> will match "src/main/java/Foo.java" and
   * "src/main/java/Foo.JAVA".<br/>
   * If both {@link #getFileSuffixes()} and {@link #filenamePatterns()} are provided, both will be considered. Implementors should be careful
   * to have each language suffix and patterns matching disjoint set of files, since a file can be assigned to only one language.
   */
  String[] getFileSuffixes();

  /**
   * Whether all files identified with this language should be sent to SonarQube, even if no data is reported for them
   * @since 9.3
   */
  default boolean publishAllFiles() {
    return true;
  }

  /**
   * Provide a list of patterns following the {@link org.sonar.api.utils.WildcardPattern} syntax. Source files matching any of those patterns will
   * be associated to this language.
   * Pattern are considered relative: <code>["pom.xml"]</code> is equivalent to <code>["&#42;&#42;/pom.xml"]</code> <br/>
   * The filename extension matching is case-insensitive, so declaring <code>["&#42;&#42;/&#42;Test.java"]</code> will match "FooTest.java" and
   * "FooTest.JAVA" but <strong>not</strong> "FooTEST.java".<br/>
   * If both {@link #getFileSuffixes()} and {@link #filenamePatterns()} are provided, both will be considered. Implementors should be careful
   * to have each language suffix and patterns matching disjoint set of files, since a file can be assigned to only one language.
   * @since 9.16
   */
  default String[] filenamePatterns() {
    return new String[0];
  }
}
