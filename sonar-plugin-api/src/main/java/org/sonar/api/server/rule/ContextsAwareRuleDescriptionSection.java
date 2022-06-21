package org.sonar.api.server.rule;

import java.util.Set;

public class ContextsAwareRuleDescriptionSection extends DefaultRuleDescriptionSection {

  private final Set<Context> contexts;

  ContextsAwareRuleDescriptionSection(String key, String htmlContent, Set<Context> contexts) {
    super(key, htmlContent);
    this.contexts = contexts;
  }

  @Override
  public Set<Context> getContexts() {
    return contexts;
  }
}
