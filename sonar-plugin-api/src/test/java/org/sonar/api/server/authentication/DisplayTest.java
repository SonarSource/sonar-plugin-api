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
package org.sonar.api.server.authentication;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DisplayTest {

  @Test
  public void create_display() {
    Display display = Display.builder()
      .setIconPath("/static/authgithub/github.svg")
      .setBackgroundColor("#123456")
      .build();

    assertThat(display.getIconPath()).isEqualTo("/static/authgithub/github.svg");
    assertThat(display.getBackgroundColor()).isEqualTo("#123456");
    assertThat(display.getHelpMessage()).isNull();
  }

  @Test
  public void create_display_with_default_background_color() {
    Display display = Display.builder()
      .setIconPath("/static/authgithub/github.svg")
      .build();

    assertThat(display.getBackgroundColor()).isEqualTo("#236a97");
  }


  @Test
  public void create_display_with_help_message() {
    Display display = Display.builder()
      .setIconPath("/static/authgithub/github.svg")
      .setHelpMessage("Help message")
      .build();

    assertThat(display.getHelpMessage()).isEqualTo("Help message");
  }

  @Test
  public void fail_when_icon_path_is_null() {
    assertThatThrownBy(() -> Display.builder()
      .setIconPath(null)
      .setBackgroundColor("#123456")
      .build())
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Icon path must not be blank");
  }

  @Test
  public void fail_when_icon_path_is_blank() {
    assertThatThrownBy(() -> Display.builder()
      .setIconPath("")
      .setBackgroundColor("#123456")
      .build())
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Icon path must not be blank");
  }

  @Test
  public void fail_when_background_color_is_null() {
    assertThatThrownBy(() -> Display.builder()
      .setIconPath("/static/authgithub/github.svg")
      .setBackgroundColor(null)
      .build())
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Background color must not be blank");
  }

  @Test
  public void fail_when_background_color_is_blank() {
    assertThatThrownBy(() -> Display.builder()
      .setIconPath("/static/authgithub/github.svg")
      .setBackgroundColor("")
      .build())
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Background color must not be blank");
  }

  @Test
  public void fail_when_background_color_has_wrong_size() {
    assertThatThrownBy(() -> Display.builder()
      .setIconPath("/static/authgithub/github.svg")
      .setBackgroundColor("1234")
      .build())
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Background color must begin with a sharp followed by 6 characters");
  }

  @Test
  public void fail_when_background_color_doesnt_begin_with_sharp() {
    assertThatThrownBy(() -> Display.builder()
      .setIconPath("/static/authgithub/github.svg")
      .setBackgroundColor("*123456")
      .build())
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Background color must begin with a sharp followed by 6 characters");
  }
}
