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
package org.sonar.api.rules;

import java.util.Collection;
import javax.annotation.CheckForNull;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.ce.ComputeEngineSide;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.ServerSide;

/**
 * Used in {@link org.sonar.api.profiles.ProfileExporter} and {@link org.sonar.api.profiles.ProfileImporter}
 * Use {@link ActiveRules} on scanner side.
 * @since 2.3
 */
@ServerSide
@ComputeEngineSide
public interface RuleFinder {

  @CheckForNull
  Rule findByKey(String repositoryKey, String key);

  @CheckForNull
  Rule findByKey(RuleKey key);

  /**
   * @throws IllegalArgumentException if more than one result
   */
  @CheckForNull
  Rule find(RuleQuery query);

  Collection<Rule> findAll(RuleQuery query);

}
