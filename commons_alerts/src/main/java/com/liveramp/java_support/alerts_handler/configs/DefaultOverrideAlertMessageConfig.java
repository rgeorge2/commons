package com.liveramp.java_support.alerts_handler.configs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultOverrideAlertMessageConfig implements OverrideAlertMessageConfig {
  private final Optional<Boolean> isAllowHtml;
  private final Optional<List<String>> tagsToSet;
  private final List<String> tagsToAdd;

  DefaultOverrideAlertMessageConfig(Optional<Boolean> isAllowHtml, List<String> tagsToAdd, Optional<List<String>> tagsToSet) {
    this.isAllowHtml = isAllowHtml;
    this.tagsToAdd = tagsToAdd;
    this.tagsToSet = tagsToSet;
  }

  public static class Builder {
    private Optional<Boolean> isAllowHtml = Optional.empty();
    private Optional<List<String>> tagsToSet = Optional.empty();
    private List<String> tagsToAdd = new ArrayList<>();

    public void setIsAllowHtml(boolean isAllowHtml) {
      this.isAllowHtml = Optional.of(isAllowHtml);
    }

    public void addTagsToAdd(List<String> tags) {
      this.tagsToAdd.addAll(tags);
    }

    public void setTagsToSet(List<String> tags) {
      this.tagsToSet = Optional.of(tags);
    }

    public OverrideAlertMessageConfig build() {
      return new DefaultOverrideAlertMessageConfig(isAllowHtml, tagsToAdd, tagsToSet);
    }
  }

  @Override
  public AlertMessageConfig overrideConfig(final AlertMessageConfig defaultConfig) {
    return new AlertMessageConfig() {
      @Override
      public boolean isAllowHtml() {
        if (isAllowHtml.isPresent()) {
          return isAllowHtml.get();
        } else {
          return defaultConfig.isAllowHtml();
        }
      }

      @Override
      public List<String> getTags() {
        final List<String> defaultTags = defaultConfig.getTags();

        if (tagsToSet.isPresent()) {
          return tagsToSet.get();
        } else  {
          final List<String> mergedTags = new ArrayList<>(tagsToAdd);
          if (defaultTags != null) {
            mergedTags.addAll(defaultTags);
          }
          return mergedTags;
        }
      }
    };
  }
}