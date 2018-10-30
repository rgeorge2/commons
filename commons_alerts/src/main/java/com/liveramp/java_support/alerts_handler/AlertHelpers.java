package com.liveramp.java_support.alerts_handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

public class AlertHelpers {
  public static String stringifyTags(List<String> tags) {
    final SortedSet<String> sortedAndUniquedTags = new TreeSet<>();
    final StringBuilder sb = new StringBuilder();

    for (String tag : sortedAndUniquedTags) {
      sb.append("[").append(tag).append("] ");
    }

    return sb.toString();
  }

  public static <T> List<T> atLeastOneToList(T first, T... rest) {
    List<T> list = new ArrayList<>(rest.length + 1);
    list.add(first);
    list.addAll(Arrays.asList(rest));
    return list;
  }

  public static String buildEmailAddress(String account, String domain) {
    return String.format("%s@%s", account, domain);
  }

  public static <T> Optional<T> getFirstSetOptional(Optional<T>... optionals) {
    for (Optional<T> optional : optionals) {
      if (optional.isPresent()) {
        return optional;
      }
    }
    return Optional.empty();
  }
}