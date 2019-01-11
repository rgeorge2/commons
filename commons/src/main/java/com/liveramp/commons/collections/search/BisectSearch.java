package com.liveramp.commons.collections.search;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BisectSearch {
  /**
   * Find the first index for which the predicate is true in a list where at some point the predicate
   * switches from false to true going from first element to last. For instance, like `git bisect`.
   * Useful if your predicate is slow to test.
   *
   * i.e. If there exists an n such that:
   *
   * * pred.test(list.get(i)) == false for all i < n
   * * pred.test(list.get(i)) == true for all i >= n
   *
   * this search will then yield n
   *
   * If no such n exists, you'll get an {@link Optional#empty()}.
   *
   * @param list An {@link List} to search. It's probably best you use one that implements
   *             {@link java.util.RandomAccess}
   * @param pred The {@link Predicate} to test the values of the list, list elements should go from
   *             all false before some n to all true after that n
   * @param <T> The type of element contained in the list
   * @return the index n in the list before which all elements are false when tested by {@code pred}
   *         and after which all elements are true when tested by {@code pred}, or {@link Optional#empty()}
   *         if no such n exists
   */
  public static <T> Optional<Integer> findFirst(List<T> list, Predicate<T> pred) {
    int low = 0;
    int high = list.size()-1;

    while (low <= high) {
      int mid = (low + high) >>> 1;
      T midVal = list.get(mid);

      if (pred.test(midVal)) {
        if (mid == low) { // we've found the lowest possible true thing
          return Optional.of(mid);
        } else { // we've found some true thing, which means we're possibly above the lowest true thing
          high = mid;
        }
      } else { // our true thing is above where we searched, if it exists
        low = mid + 1;
      }
    }

    return Optional.empty();
  }

}
