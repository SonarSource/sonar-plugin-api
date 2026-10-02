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
package org.sonar.api.scanner;

import java.nio.file.Path;
import org.sonar.api.Beta;

/**
 * Downloads the static resources of a plugin on demand and makes them available on the local filesystem. It lets a
 * plugin ship large or platform-specific resources, such as a native executable or a model file, outside of its own
 * artifact and fetch only the ones it actually needs for a given analysis.
 *
 * <p>
 * It can be injected in the constructor of any scanner-side extension. Use {@link ScannerRuntime} to resolve the
 * operating system and the architecture when the resource to fetch is platform-specific.
 * </p>
 *
 * <p>
 * The returned files must be considered read-only. They may be shared with other analyzers and reused across
 * analyses, and where they are stored locally is an implementation detail that analyzers must not rely on.
 * Implementations may serve a resource from a local cache without contacting the server.
 * </p>
 *
 * This API is experimental and can be changed or dropped at any time.
 *
 * @since 14.2
 */
@Beta
@ScannerSide
public interface ResourceFetcher {

  /**
   * Downloads a single static resource and returns the path to the local copy.
   *
   * @param pluginKey the key of the plugin the resource belongs to
   * @param resourcePath the path of the resource, relative to the resources directory of the plugin, for example
   *          {@code linux-x64/native-tool}
   * @return the absolute path of the local copy of the resource
   * @throws IllegalArgumentException if the plugin key is unknown, or if the plugin declares no resource at this path
   * @throws java.io.UncheckedIOException if the resource cannot be downloaded or cannot be written locally
   * @throws IllegalStateException if the integrity of the downloaded resource cannot be verified, for example
   *           because its checksum does not match the one advertised by the server
   */
  Path fetch(String pluginKey, String resourcePath);

  /**
   * Downloads a static resource that is an archive, extracts it, and returns the path to the directory holding the
   * extracted content. Only the ZIP and gzipped TAR ({@code .tar.gz}) formats are supported.
   *
   * @param pluginKey the key of the plugin the resource belongs to
   * @param resourcePath the path of the resource, relative to the resources directory of the plugin, for example
   *          {@code linux-x64/native-tool.tar.gz}
   * @return the absolute path of the directory holding the extracted content
   * @throws IllegalArgumentException if the plugin key is unknown, if the plugin declares no resource at this path,
   *           or if the resource is neither a ZIP nor a gzipped TAR archive
   * @throws java.io.UncheckedIOException if the archive cannot be downloaded, cannot be written locally, or cannot
   *           be extracted
   * @throws IllegalStateException if the integrity of the downloaded archive cannot be verified, for example
   *           because its checksum does not match the one advertised by the server
   */
  Path fetchAndExtract(String pluginKey, String resourcePath);

}
