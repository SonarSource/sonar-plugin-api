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
package org.sonar.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ZipUtilsTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void zip_directory() throws IOException {
    File foo = FileUtils.toFile(getClass().getResource("/org/sonar/api/utils/ZipUtilsTest/shouldZipDirectory/foo.txt"));
    File dir = foo.getParentFile();
    File zip = temp.newFile();

    ZipUtils.zipDir(dir, zip);

    assertThat(zip).exists().isFile();
    assertThat(zip.length()).isGreaterThan(1L);
    ArrayList<? extends ZipEntry> zipEntries = Collections.list(new ZipFile(zip).entries());
    assertThat(zipEntries).hasSize(4);

    File unzipDir = temp.newFolder();
    ZipUtils.unzip(zip, unzipDir);
    assertThat(new File(unzipDir, "bar.txt")).exists().isFile();
    assertThat(new File(unzipDir, "foo.txt")).exists().isFile();
    assertThat(new File(unzipDir, "dir1/hello.properties")).exists().isFile();
  }

  @Test
  public void unzipping_creates_target_directory_if_it_does_not_exist() throws IOException {
    File zip = FileUtils.toFile(urlToZip());
    File tempDir = temp.newFolder();
    Files.delete(tempDir);

    File subDir = new File(tempDir, "subDir");
    ZipUtils.unzip(zip, subDir);
    assertThat(subDir.list()).hasSize(3);
  }

  @Test
  public void unzip_file() throws IOException {
    File zip = FileUtils.toFile(urlToZip());
    File toDir = temp.newFolder();
    ZipUtils.unzip(zip, toDir);
    assertThat(toDir.list()).hasSize(3);
  }

  @Test
  public void unzip_stream() throws Exception {
    InputStream zip = urlToZip().openStream();
    File toDir = temp.newFolder();
    ZipUtils.unzip(zip, toDir);
    assertThat(toDir.list()).hasSize(3);
  }

  @Test
  public void fail_if_unzipping_file_outside_target_directory() throws Exception {
    File zip = new File(getClass().getResource("ZipUtilsTest/zip-slip.zip").toURI());
    File toDir = temp.newFolder();

    assertThatThrownBy(() -> ZipUtils.unzip(zip, toDir))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Unzipping an entry outside the target directory is not allowed: ../../../../../../../../../../../../../../../../" +
        "../../../../../../../../../../../../../../../../../../../../../../../../tmp/evil.txt");
  }

  @Test
  public void fail_if_unzipping_stream_outside_target_directory() throws Exception {
    File zip = new File(getClass().getResource("ZipUtilsTest/zip-slip.zip").toURI());
    File toDir = temp.newFolder();


    try (InputStream input = new FileInputStream(zip)) {
      assertThatThrownBy(() -> ZipUtils.unzip(input, toDir))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Unzipping an entry outside the target directory is not allowed: ../../../../../../../../../../../../../../../../../.." +
          "/../../../../../../../../../../../../../../../../../../../../../../tmp/evil.txt");
    }
  }

  private URL urlToZip() {
    return getClass().getResource("/org/sonar/api/utils/ZipUtilsTest/shouldUnzipFile.zip");
  }
}
