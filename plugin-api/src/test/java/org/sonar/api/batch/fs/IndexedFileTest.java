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
package org.sonar.api.batch.fs;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import javax.annotation.CheckForNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class IndexedFileTest {

  @Test
  void file_should_be_hidden_on_default() {
    IndexedFile file = new MyIndexedFile();

    assertThat(file.isHidden()).isFalse();
  }

  private static class MyIndexedFile implements IndexedFile {
    @Override
    public String key() {
      return "";
    }

    @Override
    public boolean isFile() {
      return false;
    }

    @Override
    public String relativePath() {
      return "";
    }

    @Override
    public String absolutePath() {
      return "";
    }

    @Override
    public File file() {
      return new File("");
    }

    @Override
    public Path path() {
      return file().toPath();
    }

    @Override
    public URI uri() {
      return file().toURI();
    }

    @Override
    public String filename() {
      return "";
    }

    @CheckForNull
    @Override
    public String language() {
      return "";
    }

    @Override
    public InputFile.Type type() {
      return null;
    }

    @Override
    public InputStream inputStream() {
      return null;
    }
  }
}
