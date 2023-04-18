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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JavaxHttpResponseTest {

  @Test
  public void initResponse() throws IOException {
    HttpServletResponse responseMock = mock(HttpServletResponse.class);
    when(responseMock.getHeader("h1")).thenReturn("hvalue1");
    when(responseMock.getHeaders("h1")).thenReturn(List.of("hvalue1"));
    when(responseMock.getStatus()).thenReturn(200);
    PrintWriter writer = mock(PrintWriter.class);
    when(responseMock.getWriter()).thenReturn(writer);

    JavaxHttpResponse response = new JavaxHttpResponse(responseMock);

    assertThat(response.getRawResponse()).isSameAs(responseMock);
    assertThat(response.getHeader("h1")).isEqualTo("hvalue1");
    assertThat(response.getHeaders("h1")).asList().containsExactly("hvalue1");
    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getWriter()).isEqualTo(writer);

    response.addHeader("h2", "hvalue2");
    response.setHeader("h3", "hvalue3");
    response.setStatus(201);
    response.setContentType("text/plain");
    response.sendRedirect("http://redirect");
    verify(responseMock).addHeader("h2", "hvalue2");
    verify(responseMock).setHeader("h3", "hvalue3");
    verify(responseMock).setStatus(201);
    verify(responseMock).setContentType("text/plain");
    verify(responseMock).sendRedirect("http://redirect");
  }
}
