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
package org.sonar.plugin.api.manifest;

import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.utils.Version;

public class RequiredPlugin {

  private static final Pattern PARSER = Pattern.compile("\\w+:.+");

  private final String key;
  private final Version minimalVersion;

  public RequiredPlugin(String key, Version minimalVersion) {
    this.key = key;
    this.minimalVersion = minimalVersion;
  }

  public String getKey() {
    return key;
  }

  public Version getMinimalVersion() {
    return minimalVersion;
  }

  static RequiredPlugin parse(String s) {
    if (!PARSER.matcher(s).matches()) {
      throw new IllegalArgumentException("Manifest field does not have correct format: " + s);
    }
    var fields = StringUtils.split(s, ':');
    return new RequiredPlugin(fields[0], Version.create(fields[1]).removeQualifier());
  }

}
