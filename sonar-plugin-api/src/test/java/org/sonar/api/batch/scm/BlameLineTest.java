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
package org.sonar.api.batch.scm;

import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class BlameLineTest {

  @Test
  public void testBlameLine() {
    Date date = new Date();
    BlameLine line1 = new BlameLine().date(date).revision("1").author("foo");

    assertThat(line1.author()).isEqualTo("foo");
    assertThat(line1.date()).isEqualTo(date);
    assertThat(line1.revision()).isEqualTo("1");
    assertThat(line1.toString()).isNotEqualTo("foo");
    assertThat(line1.toString()).contains("revision=1,author=foo");
  }

  @Test
  public void test_equals_and_hashCode() {
    Date date = new Date();
    BlameLine line1 = new BlameLine().date(date).revision("1").author("foo");
    BlameLine line2 = new BlameLine().date(date).revision("1").author("foo");
    BlameLine line3 = new BlameLine().date(null).revision("1").author("bar");
    BlameLine line4 = new BlameLine().date(date).revision("2").author("foo");
    BlameLine line5 = new BlameLine().date(date).revision("1").author("bar");

    assertThat(line1)
      .isEqualTo(line1)
      .isNotNull()
      .isEqualTo(line2)
      .isNotEqualTo(line3)
      .hasSameHashCodeAs(line2);
    assertThat(line1.hashCode()).isNotEqualTo(line3.hashCode());
    assertThat(line1.hashCode()).isNotEqualTo(line4.hashCode());
    assertThat(line1.hashCode()).isNotEqualTo(line5.hashCode());
  }

  @Test
  public void testTrimAuthor() {
    BlameLine line1 = new BlameLine().date(null).revision("2").author("foo1");
    BlameLine line2 = new BlameLine().date(null).revision("2").author("  ");
    BlameLine line3 = new BlameLine().date(null).revision("2").author(" foo3  ");

    assertThat(line1.author()).isEqualTo("foo1");
    assertThat(line2.author()).isNull();
    assertThat(line3.author()).isEqualTo("foo3");
  }
}
