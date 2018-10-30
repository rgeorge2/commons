package com.liveramp.java_support.alerts_handler.configs;

import java.util.ArrayList;
import java.util.List;

public class DefaultAlertMessageConfig implements AlertMessageConfig {
  private final boolean isAllowHtml;
  private final List<String> tags;

  public DefaultAlertMessageConfig(boolean isAllowHtml, List<String> tags) {
    this.isAllowHtml = isAllowHtml;
    this.tags = tags;
  }

  @Override
  public boolean isAllowHtml() {
    return isAllowHtml;
  }

  @Override
  public List<String> getTags() {
    return new ArrayList<>(tags);
  }
}
