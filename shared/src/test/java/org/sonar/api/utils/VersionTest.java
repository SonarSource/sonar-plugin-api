/*
 * Sonar Plugin API
 * Copyright (C) SonarSource Sàrl
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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.sonar.api.utils.Version.parse;

class VersionTest {

  @Test
  void test_parse() {
    assertVersion(parse(""), 0, 0, 0, 0L, "");
    assertVersion(parse("1"), 1, 0, 0, 0L, "");
    assertVersion(parse("1.2"), 1, 2, 0, 0L, "");
    assertVersion(parse("1.2.3"), 1, 2, 3, 0L, "");
    assertVersion(parse("1.2-beta-1"), 1, 2, 0, 0L, "beta-1");
    assertVersion(parse("1.2.3-beta1"), 1, 2, 3, 0L, "beta1");
    assertVersion(parse("1.2.3-beta-1"), 1, 2, 3, 0L, "beta-1");
    assertVersion(parse("1.2.3.4567"), 1, 2, 3, 4567L, "");
    assertVersion(parse("1.2.3.4567-alpha"), 1, 2, 3, 4567L, "alpha");
  }

  @Test
  void parse_throws_IAE_if_more_than_4_fields() {
    assertThatThrownBy(() -> parse("1.2.3.456.7"))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Maximum 4 fields are accepted: 1.2.3.456.7");
  }

  @Test
  void test_create_int() {
    assertVersion(Version.create(1, 2), 1, 2, 0, 0L, "");
    assertVersion(Version.create(1, 2, 3), 1, 2, 3, 0L, "");
  }

  @Test
  void test_fields_of_snapshot_versions() {
    assertVersion(Version.create("1.2.3-SNAPSHOT"), 1, 2, 3, 0L, "SNAPSHOT");
  }

  @Test
  void test_fields_of_releases() {
    assertVersion(Version.create("1.2"), 1, 2, 0, 0L, "");
  }

  @Test
  void test_equals() {
    Version one = parse("1");
    assertThat(one)
      .isEqualTo(one)
      .isEqualTo(parse("1"))
      .isEqualTo(parse("1.0"))
      .isEqualTo(parse("1.0.0"))
      .isNotEqualTo(parse("1.2.3"))
      .isNotEqualTo("1");

    assertThat(parse("1.2.3")).isEqualTo(parse("1.2.3"));
    assertThat(parse("1.2.3")).isNotEqualTo(parse("1.2.4"));
    assertThat(parse("1.2.3")).isNotEqualTo(parse("1.2.3-b1"));
    assertThat(parse("1.2.3-b1")).isNotEqualTo(parse("1.2.3-b2"));
    assertThat(parse("1.2.3-b1")).isEqualTo(parse("1.2.3-b1"));
  }

  @Test
  void equals_should_return_false_for_null() {
    var version = Version.create("1.2.3");
    assertThat(version.equals(null)).isFalse();
  }

  @Test
  void equals_should_return_false_for_different_type() {
    var version = Version.create("1.2.3");
    assertThat(version.equals("1.2.3")).isFalse();
  }

  @Test
  void equals_should_return_true_for_same_instance() {
    var version = Version.create("1.2.3");
    assertThat(version.equals(version)).isTrue();
  }

  @Test
  void test_hashCode() {
    assertThat(parse("1")).hasSameHashCodeAs(parse("1"));
    assertThat(parse("1")).hasSameHashCodeAs(parse("1.0.0"));
    assertThat(parse("1.2.3-beta1")).isNotEqualTo(parse("1.2.3"));
  }

  @Test
  void hashCode_should_be_consistent() {
    var version1 = Version.create("1.2.3-SNAPSHOT");
    var version2 = Version.create("1.2.3-SNAPSHOT");
    assertThat(version1).hasSameHashCodeAs(version2);
  }

  @Test
  void hashCode_should_differ_for_different_versions() {
    var version1 = Version.create("1.2.3");
    var version2 = Version.create("1.2.4");
    assertThat(version1.hashCode()).isNotEqualTo(version2.hashCode());
  }

  @Test
  void test_compareTo() {
    assertThat(parse("1.2")).isEqualByComparingTo(parse("1.2.0"));
    assertThat(parse("1.2.3")).isEqualByComparingTo(parse("1.2.3"));
    assertThat(parse("1.2.3").compareTo(parse("1.2.4"))).isLessThan(0);
    assertThat(parse("1.2.3").compareTo(parse("1.3"))).isLessThan(0);
    assertThat(parse("1.2.3").compareTo(parse("2.1"))).isLessThan(0);
    assertThat(parse("1.2.3").compareTo(parse("2.0.0"))).isLessThan(0);
    assertThat(parse("2.0").compareTo(parse("1.2"))).isGreaterThan(0);
  }

  @Test
  void test_isGreaterThanOrEqual() {
    assertThat(parse("1.2").isGreaterThanOrEqual(parse("1.2.0"))).isTrue();
    assertThat(parse("1.2.3").isGreaterThanOrEqual(parse("1.2.4"))).isFalse();
    assertThat(parse("2.0").isGreaterThanOrEqual(parse("1.2.4"))).isTrue();
  }

  @Test
  void compareTo_handles_build_number() {
    assertThat(parse("1.2")).isEqualByComparingTo(parse("1.2.0.0"));
    assertThat(parse("1.2.3.1234").compareTo(parse("1.2.3.4567"))).isLessThan(0);
    assertThat(parse("1.2.3.1234").compareTo(parse("1.2.3"))).isGreaterThan(0);
    assertThat(parse("1.2.3.1234").compareTo(parse("1.2.4"))).isLessThan(0);
    assertThat(parse("1.2.3.9999").compareTo(parse("1.2.4.1111"))).isLessThan(0);
  }

  @Test
  void compareToIncludingQualifier_is_qualifier_aware() {
    assertThat(parse("1.2.3").compareToIncludingQualifier(parse("1.2.3-build1"))).isPositive();
    assertThat(parse("1.2").compareToIncludingQualifier(parse("1.2-SNAPSHOT"))).isPositive();
    assertThat(parse("1.2-SNAPSHOT").compareToIncludingQualifier(parse("1.2-RC1"))).isPositive();
  }

  @Test
  void compareToIncludingQualifier_snapshots() {
    var version12 = Version.create("1.2");
    var version12Snapshot = Version.create("1.2-SNAPSHOT");
    var version121Snapshot = Version.create("1.2.1-SNAPSHOT");
    var version12RC = Version.create("1.2-RC1");

    assertThat(version12.compareToIncludingQualifier(version12Snapshot)).isPositive();
    assertThat(version12Snapshot.compareToIncludingQualifier(version12Snapshot)).isZero();
    assertThat(version121Snapshot.compareToIncludingQualifier(version12Snapshot)).isPositive();
    assertThat(version12Snapshot.compareToIncludingQualifier(version12RC)).isPositive();
  }

  @Test
  void compareToIncludingQualifier_release_candidates() {
    var version12 = Version.create("1.2");
    var version12Snapshot = Version.create("1.2-SNAPSHOT");
    var version12RC1 = Version.create("1.2-RC1");
    var version12RC2 = Version.create("1.2-RC2");

    assertThat(version12RC1.compareToIncludingQualifier(version12Snapshot)).isNegative();
    assertThat(version12RC1.compareToIncludingQualifier(version12RC1)).isZero();
    assertThat(version12RC1.compareToIncludingQualifier(version12RC2)).isNegative();
    assertThat(version12RC1.compareToIncludingQualifier(version12)).isNegative();
  }

  @Test
  void compare_releases() {
    var version12 = Version.create("1.2");
    var version121 = Version.create("1.2.1");

    assertThat(version12)
      .hasToString("1.2")
      .isEqualByComparingTo(version12);
    assertThat(version121)
      .isEqualByComparingTo(version121)
      .isGreaterThan(version12);
  }

  @Test
  void testTrim() {
    var version12 = Version.create("   1.2  ");

    assertThat(version12)
      .hasToString("1.2")
      .isEqualTo(Version.create("1.2"));
  }

  @Test
  void testDefaultNumberIsZero() {
    var version12 = Version.create("1.2");
    var version120 = Version.create("1.2.0");

    assertThat(version12).isEqualTo(version120);
    assertThat(version120).isEqualTo(version12);
  }

  @Test
  void testCompareOnTwoDigits() {
    var version1dot10 = Version.create("1.10");
    var version1dot1 = Version.create("1.1");
    var version1dot9 = Version.create("1.9");

    assertThat(version1dot10)
      .isGreaterThan(version1dot1)
      .isGreaterThan(version1dot9);
  }

  @Test
  void testFields() {
    var version = Version.create("1.10.2");

    assertThat(version).hasToString("1.10.2");
    assertVersion(version, 1, 10, 2, 0L, "");
  }

  @Test
  void testPatchFieldsEquals() {
    var version = Version.create("1.2.3.4");

    assertThat(version.patch()).isEqualTo(3);
    assertThat(version.buildNumber()).isEqualTo(4L);

    assertThat(version)
      .isEqualTo(version)
      .isEqualTo(Version.create("1.2.3.4"))
      .isNotEqualTo(Version.create("1.2.3.5"));
  }

  @Test
  void test_toString() {
    assertThat(parse("1")).hasToString("1.0");
    assertThat(parse("1.2")).hasToString("1.2");
    assertThat(parse("1.2.3")).hasToString("1.2.3");
    assertThat(parse("1.2-b1")).hasToString("1.2-b1");
    assertThat(parse("1.2.3-b1")).hasToString("1.2.3-b1");
    assertThat(parse("1.2.3.4567")).hasToString("1.2.3.4567");
    assertThat(parse("1.2.3.4567-beta1")).hasToString("1.2.3.4567-beta1");
    assertThat(parse("1.2.0.0")).hasToString("1.2");
    assertThat(parse("1.2.0.1")).hasToString("1.2.0.1");
    assertThat(parse("1.2.1.0")).hasToString("1.2.1");
    assertThat(parse("1.2.1.0-beta")).hasToString("1.2.1-beta");
  }

  @Test
  void removeQualifier() {
    assertVersion(Version.create("1.2.3-SNAPSHOT").removeQualifier(), 1, 2, 3, 0L, "");
  }

  @Test
  void removeQualifier_when_no_qualifier() {
    var version = Version.create("1.2.3").removeQualifier();

    assertThat(version).hasToString("1.2.3");
    assertVersion(version, 1, 2, 3, 0L, "");
  }

  @Test
  void satisfiesMinRequirement_should_return_true_when_equal() {
    var version = Version.create("1.2.3");
    var minRequirement = Version.create("1.2.3");

    assertThat(version.satisfiesMinRequirement(minRequirement)).isTrue();
  }

  @Test
  void satisfiesMinRequirement_should_return_true_when_greater() {
    var version = Version.create("1.3.0");
    var minRequirement = Version.create("1.2.3");

    assertThat(version.satisfiesMinRequirement(minRequirement)).isTrue();
  }

  @Test
  void satisfiesMinRequirement_should_return_false_when_lower() {
    var version = Version.create("1.2.2");
    var minRequirement = Version.create("1.2.3");

    assertThat(version.satisfiesMinRequirement(minRequirement)).isFalse();
  }

  @Test
  void satisfiesMinRequirement_should_ignore_qualifier() {
    var version = Version.create("1.2.3-SNAPSHOT");
    var minRequirement = Version.create("1.2.3-RC1");

    assertThat(version.satisfiesMinRequirement(minRequirement)).isTrue();
  }

  @Test
  void compareToIncludingQualifier_should_return_zero_when_equal() {
    var version1 = Version.create("1.2.3-SNAPSHOT");
    var version2 = Version.create("1.2.3-SNAPSHOT");

    assertThat(version1.compareToIncludingQualifier(version2)).isZero();
  }

  @Test
  void compareToIncludingQualifier_should_return_positive_when_greater() {
    var version1 = Version.create("1.3.0-SNAPSHOT");
    var version2 = Version.create("1.2.3-SNAPSHOT");

    assertThat(version1.compareToIncludingQualifier(version2)).isPositive();
  }

  @Test
  void compareToIncludingQualifier_should_return_negative_when_lower() {
    var version1 = Version.create("1.2.2-SNAPSHOT");
    var version2 = Version.create("1.2.3-SNAPSHOT");

    assertThat(version1.compareToIncludingQualifier(version2)).isNegative();
  }

  private static void assertVersion(Version version,
    int expectedMajor, int expectedMinor, int expectedPatch, long expectedBuildNumber, String expectedQualifier) {
    assertThat(version.major()).isEqualTo(expectedMajor);
    assertThat(version.minor()).isEqualTo(expectedMinor);
    assertThat(version.patch()).isEqualTo(expectedPatch);
    assertThat(version.buildNumber()).isEqualTo(expectedBuildNumber);
    assertThat(version.qualifier()).isEqualTo(expectedQualifier);
  }
}
