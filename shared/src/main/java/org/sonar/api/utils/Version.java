/*
 * Sonar Plugin API
 * Copyright (C) 2009-2025 SonarSource Sàrl
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
package org.sonar.api.utils;

import javax.annotation.concurrent.Immutable;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Objects.requireNonNull;

/**
 * Version composed of maximum four fields (major, minor, patch and build number) and optionally a qualifier.
 * <p>
 * Examples: 1.0, 1.0.0, 1.2.3, 1.2-beta1, 1.2.1-beta-1, 1.2.3.4567
 * <p>
 * <h3>Qualifier ordering</h3>
 * When numeric fields are equal, the qualifier determines ordering:
 * a release (empty qualifier) is greater than any pre-release qualifier,
 * and pre-release qualifiers are compared lexicographically.
 * <p>
 * The default comparison implemented in {@link #compareTo(Version)} ignores the qualifier, so that for example "1.2.3" is considered equal to "1.2.3-beta-1".
 * <p>
 * Use {@link #compareToIncludingQualifier(Version)} to compare including qualifier.
 */
@Immutable
public class Version implements Comparable<Version> {

  private static final long DEFAULT_BUILD_NUMBER = 0L;
  private static final int DEFAULT_PATCH = 0;
  private static final String DEFAULT_QUALIFIER = "";
  private static final String QUALIFIER_SEPARATOR = "-";
  private static final String SEQUENCE_SEPARATOR = ".";

  private final int major;
  private final int minor;
  private final int patch;
  private final long buildNumber;
  private final String qualifier;

  private Version(int major, int minor, int patch, long buildNumber, String qualifier) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
    this.buildNumber = buildNumber;
    this.qualifier = requireNonNull(qualifier, "Version qualifier must not be null");
  }

  public int major() {
    return major;
  }

  public int minor() {
    return minor;
  }

  public int patch() {
    return patch;
  }

  /**
   * Build number if the fourth field, for example {@code 12345} for "6.3.0.12345".
   * If absent, then value is zero.
   */
  public long buildNumber() {
    return buildNumber;
  }

  /**
   * @return non-null suffix. Empty if absent, else the suffix without the first character "-"
   */
  public String qualifier() {
    return qualifier;
  }

  /**
   * Convert a {@link String} to a Version. Supported formats:
   * <ul>
   * <li>1</li>
   * <li>1.2</li>
   * <li>1.2.3</li>
   * <li>1-beta-1</li>
   * <li>1.2-beta-1</li>
   * <li>1.2.3-beta-1</li>
   * <li>1.2.3.4567</li>
   * <li>1.2.3.4567-beta-1</li>
   * </ul>
   * Note that the optional qualifier is the part after the first "-".
   *
   * @throws IllegalArgumentException if parameter is badly formatted, for example
   *                                  if it defines 5 integer-sequences.
   */
  public static Version parse(String text) {
    String s = text.trim();
    String qualifier = DEFAULT_QUALIFIER;
    int dashIdx = s.indexOf(QUALIFIER_SEPARATOR);
    if (dashIdx >= 0) {
      qualifier = s.substring(dashIdx + 1);
      s = s.substring(0, dashIdx);
    }
    String[] fields = s.isEmpty() ? new String[0] : s.split("\\.");
    int major = 0;
    int minor = 0;
    int patch = DEFAULT_PATCH;
    long buildNumber = DEFAULT_BUILD_NUMBER;
    int size = fields.length;
    if (size > 0) {
      major = parseFieldAsInt(fields[0]);
      if (size > 1) {
        minor = parseFieldAsInt(fields[1]);
        if (size > 2) {
          patch = parseFieldAsInt(fields[2]);
          if (size > 3) {
            buildNumber = parseFieldAsLong(fields[3]);
            if (size > 4) {
              throw new IllegalArgumentException("Maximum 4 fields are accepted: " + text);
            }
          }
        }
      }
    }
    return new Version(major, minor, patch, buildNumber, qualifier);
  }

  /**
   * Alias for {@link #parse(String)}.
   */
  public static Version create(String text) {
    return parse(text);
  }

  public static Version create(int major, int minor) {
    return new Version(major, minor, DEFAULT_PATCH, DEFAULT_BUILD_NUMBER, DEFAULT_QUALIFIER);
  }

  public static Version create(int major, int minor, int patch) {
    return new Version(major, minor, patch, DEFAULT_BUILD_NUMBER, DEFAULT_QUALIFIER);
  }

  private static int parseFieldAsInt(String field) {
    if (field.isEmpty()) {
      return 0;
    }
    return parseInt(field);
  }

  private static long parseFieldAsLong(String field) {
    if (field.isEmpty()) {
      return 0L;
    }
    return parseLong(field);
  }

  public boolean isGreaterThanOrEqual(Version than) {
    return this.compareTo(than) >= 0;
  }

  /**
   * Returns a new Version with the same numeric fields but an empty qualifier.
   */
  public Version removeQualifier() {
    return new Version(major, minor, patch, buildNumber, DEFAULT_QUALIFIER);
  }

  /**
   * Returns true if this version is greater than or equal to the given minimum requirement,
   * ignoring qualifier.
   */
  public boolean satisfiesMinRequirement(Version minRequirement) {
    return this.compareToIncludingQualifier(minRequirement) >= 0;
  }

  /**
   * Compares this version to another, including qualifier.
   */
  public int compareToIncludingQualifier(Version other) {
    int c = compareTo(other);
    if (c == 0) {
      if (qualifier.isEmpty()) {
        c = other.qualifier.isEmpty() ? 0 : 1;
      } else if (other.qualifier.isEmpty()) {
        c = -1;
      } else {
        c = qualifier.compareTo(other.qualifier);
      }
    }
    return c;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Version version = (Version) o;
    if (major != version.major) {
      return false;
    }
    if (minor != version.minor) {
      return false;
    }
    if (patch != version.patch) {
      return false;
    }
    if (buildNumber != version.buildNumber) {
      return false;
    }
    return qualifier.equals(version.qualifier);
  }

  @Override
  public int hashCode() {
    int result = major;
    result = 31 * result + minor;
    result = 31 * result + patch;
    result = 31 * result + Long.hashCode(buildNumber);
    result = 31 * result + qualifier.hashCode();
    return result;
  }

  /**
   * Compares this version to another, ignoring qualifier.
   */
  @Override
  public int compareTo(Version other) {
    int c = major - other.major;
    if (c == 0) {
      c = minor - other.minor;
      if (c == 0) {
        c = patch - other.patch;
        if (c == 0) {
          long diff = buildNumber - other.buildNumber;
          c = (diff > 0) ? 1 : ((diff < 0) ? -1 : 0);
        }
      }
    }
    return c;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(major).append(SEQUENCE_SEPARATOR).append(minor);
    if (patch > 0 || buildNumber > 0) {
      sb.append(SEQUENCE_SEPARATOR).append(patch);
      if (buildNumber > 0) {
        sb.append(SEQUENCE_SEPARATOR).append(buildNumber);
      }
    }
    if (!qualifier.isEmpty()) {
      sb.append(QUALIFIER_SEPARATOR).append(qualifier);
    }
    return sb.toString();
  }
}
