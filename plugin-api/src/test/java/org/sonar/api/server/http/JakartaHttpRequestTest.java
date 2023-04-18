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
package org.sonar.api.server.http;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JakartaHttpRequestTest {

  @Test
  public void initRequest() {
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getServerPort()).thenReturn(80);
    when(requestMock.isSecure()).thenReturn(true);
    when(requestMock.getScheme()).thenReturn("https");
    when(requestMock.getServerName()).thenReturn("hostname");
    when(requestMock.getRequestURL()).thenReturn(new StringBuffer("https://hostname:80/path"));
    when(requestMock.getRequestURI()).thenReturn("/path");
    when(requestMock.getQueryString()).thenReturn("param1=value1");
    when(requestMock.getContextPath()).thenReturn("/path");
    when(requestMock.getMethod()).thenReturn("POST");
    when(requestMock.getParameter("param1")).thenReturn("value1");
    when(requestMock.getParameterValues("param1")).thenReturn(new String[]{"value1"});
    when(requestMock.getHeader("header1")).thenReturn("hvalue1");
    Enumeration<String> headers = mock(Enumeration.class);
    when(requestMock.getHeaders("header1")).thenReturn(headers);

    JakartaHttpRequest request = new JakartaHttpRequest(requestMock);

    assertThat(request.getRawRequest()).isSameAs(requestMock);
    assertThat(request.getServerPort()).isEqualTo(80);
    assertThat(request.isSecure()).isTrue();
    assertThat(request.getScheme()).isEqualTo("https");
    assertThat(request.getServerName()).isEqualTo("hostname");
    assertThat(request.getRequestURL()).isEqualTo("https://hostname:80/path");
    assertThat(request.getRequestURI()).isEqualTo("/path");
    assertThat(request.getQueryString()).isEqualTo("param1=value1");
    assertThat(request.getContextPath()).isEqualTo("/path");
    assertThat(request.getMethod()).isEqualTo("POST");
    assertThat(request.getParameter("param1")).isEqualTo("value1");
    assertThat(request.getParameterValues("param1")).containsExactly("value1");
    assertThat(request.getHeader("header1")).isEqualTo("hvalue1");
    assertThat(request.getHeaders("header1")).isEqualTo(headers);
  }
}
