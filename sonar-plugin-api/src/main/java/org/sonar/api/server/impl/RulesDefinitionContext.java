package org.sonar.api.server.impl;

import static java.util.Collections.unmodifiableList;
import static org.sonar.api.utils.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.internal.DefaultNewRepository;
import org.sonar.api.server.rule.internal.DefaultRepository;

public class RulesDefinitionContext extends RulesDefinition.Context {

  private final Map<String, RulesDefinition.Repository> repositoriesByKey = new HashMap<>();
  private String currentPluginKey = null;

  @Override
  public RulesDefinition.NewRepository createRepository(String key, String language) {
    return new DefaultNewRepository(this, key, language, false);
  }

  @Override
  public RulesDefinition.NewRepository createExternalRepository(String engineId, String language) {
    return new DefaultNewRepository(this, RuleKey.EXTERNAL_RULE_REPO_PREFIX + engineId, language, true);
  }

  @Override
  @CheckForNull
  public RulesDefinition.Repository repository(String key) {
    return repositoriesByKey.get(key);
  }

  @Override
  public List<RulesDefinition.Repository> repositories() {
    return unmodifiableList(new ArrayList<>(repositoriesByKey.values()));
  }

  @Override
  public void registerRepository(DefaultNewRepository newRepository) {
    RulesDefinition.Repository existing = repositoriesByKey.get(newRepository.key());
    if (existing != null) {
      String existingLanguage = existing.language();
      checkState(existingLanguage.equals(newRepository.language()),
          "The rule repository '%s' must not be defined for two different languages: %s and %s",
          newRepository.key(), existingLanguage, newRepository.language());
    }
    repositoriesByKey.put(newRepository.key(), new DefaultRepository(newRepository, existing));
  }

  @Override
  public String currentPluginKey() {
    return currentPluginKey;
  }

  @Override
  public void setCurrentPluginKey(@Nullable String pluginKey) {
    this.currentPluginKey = pluginKey;
  }
}
