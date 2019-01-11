package com.liveramp.commons.collections.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBisectSearch {

  @Test
  public void testElementAtEndCanBeFoundInOddList() {
    final ArrayList<String> xs = Lists.newArrayList("dabc", "abdcd", "def", "abdc", "ex", "ac", "abc");
    assertEquals(Optional.of(6), BisectSearch.findFirst(xs, s -> s.startsWith("abc")));
  }

  @Test
  public void testElementAtEndCanBeFoundInEvenList() {
    final ArrayList<String> xs = Lists.newArrayList("dabc", "abdcd", "def", "abdc", "ex", "abc");
    assertEquals(Optional.of(5), BisectSearch.findFirst(xs, s -> s.startsWith("abc")));
  }


  @Test
  public void testElementAtBeginningCanBeFoundInOddList() {
    final ArrayList<String> xs = Lists.newArrayList("dabc", "dabdcd", "dabef", "dabdc", "dabex", "dabac", "dababc");
    assertEquals(Optional.of(0), BisectSearch.findFirst(xs, s -> s.startsWith("dab")));
  }

  @Test
  public void testElementAtBeginningCanBeFoundInEvenList() {
    final ArrayList<String> xs = Lists.newArrayList("dabc", "dabdcd", "dabef", "dabdc", "dabex", "dabac");
    assertEquals(Optional.of(0), BisectSearch.findFirst(xs, s -> s.startsWith("dab")));
  }

  @Test
  public void testFailureToFindYieldsEmpty() {
    final ArrayList<String> xs = Lists.newArrayList("dabc", "abdcd", "def", "abdc", "ex", "ac", "abc");
    assertEquals(Optional.empty(), BisectSearch.findFirst(xs, s -> s.startsWith("x")));
  }

  @Test
  public void testElementInMiddleCanBeFoundInOddList() {
    final ArrayList<String> xs = Lists.newArrayList("dabc", "abdcd", "def", "abca", "abcb", "abcc", "abcd");
    assertEquals(Optional.of(3), BisectSearch.findFirst(xs, s -> s.startsWith("abc")));
  }

  @Test
  public void testElementInMiddleCanBeFoundInEvenListLeft() {
    final ArrayList<String> xs = Lists.newArrayList("dabc", "abdcd", "abca", "abcb", "abcc", "abcd");
    assertEquals(Optional.of(2), BisectSearch.findFirst(xs, s -> s.startsWith("abc")));
  }

  @Test
  public void testElementInMiddleCanBeFoundInEvenListRight() {
    final ArrayList<String> xs = Lists.newArrayList("dabc", "abdcd", "abda", "abca", "abcb", "abcc");
    assertEquals(Optional.of(3), BisectSearch.findFirst(xs, s -> s.startsWith("abc")));
  }


  @Test
  public void testSearchIsRoughlyLogarithmic() {
    // Expected 'gets' ~ O(ln(searchList.size))
    final int baseFalseSize = 100;
    final int baseTrueSize = 50;
    final int baseMaxGets = 9;
    {
      int factor = 1;
      final IntStream oddStream = IntStream.iterate(1, i -> i + 2).limit(baseFalseSize * factor);
      final IntStream evenStream = IntStream.iterate(2, i -> i + 2).limit(baseTrueSize * factor);
      final List<Integer> nums = IntStream.concat(oddStream, evenStream).boxed().collect(Collectors.toList());
      final AccessCountingList<Integer> searchList = new AccessCountingList<>(nums);
      final Optional<Integer> first = BisectSearch.findFirst(searchList, i -> i % 2 == 0);
      assertEquals(Optional.of(baseFalseSize * factor), first);
      assertTrue(searchList.numGets <= baseMaxGets);
    }
    {
      int factor = 2;
      final IntStream oddStream = IntStream.iterate(1, i -> i + 2).limit(baseFalseSize * factor);
      final IntStream evenStream = IntStream.iterate(2, i -> i + 2).limit(baseTrueSize * factor);
      final List<Integer> nums = IntStream.concat(oddStream, evenStream).boxed().collect(Collectors.toList());
      final AccessCountingList<Integer> searchList = new AccessCountingList<>(nums);
      final Optional<Integer> first = BisectSearch.findFirst(searchList, i -> i % 2 == 0);
      assertEquals(first, Optional.of(baseFalseSize * factor));
      assertTrue(searchList.numGets <= baseMaxGets + 1);
    }
    {
      int factor = 8;
      final IntStream oddStream = IntStream.iterate(1, i -> i + 2).limit(baseFalseSize * factor);
      final IntStream evenStream = IntStream.iterate(2, i -> i + 2).limit(baseTrueSize * factor);
      final List<Integer> nums = IntStream.concat(oddStream, evenStream).boxed().collect(Collectors.toList());
      final AccessCountingList<Integer> searchList = new AccessCountingList<>(nums);
      final Optional<Integer> first = BisectSearch.findFirst(searchList, i -> i % 2 == 0);
      assertEquals(first, Optional.of(baseFalseSize * factor));
      assertTrue(searchList.numGets <= baseMaxGets + 3);
    }
    {
      int factor = 128;
      final IntStream oddStream = IntStream.iterate(1, i -> i + 2).limit(baseFalseSize * factor);
      final IntStream evenStream = IntStream.iterate(2, i -> i + 2).limit(baseTrueSize * factor);
      final List<Integer> nums = IntStream.concat(oddStream, evenStream).boxed().collect(Collectors.toList());
      final AccessCountingList<Integer> searchList = new AccessCountingList<>(nums);
      final Optional<Integer> first = BisectSearch.findFirst(searchList, i -> i % 2 == 0);
      assertEquals(first, Optional.of(baseFalseSize * factor));
      assertTrue(searchList.numGets <= baseMaxGets + 7);
    }
  }

  @Test
  public void testSearchForMissingIsLogarithmic() {
    final int baseFalseSize = 100;
    final int baseMaxGets = 9;
    {
      int factor = 1;
      final List<Integer> nums = IntStream.iterate(1, i -> i + 2)
          .limit(baseFalseSize * factor)
          .boxed()
          .collect(Collectors.toList());
      final AccessCountingList<Integer> searchList = new AccessCountingList<>(nums);
      final Optional<Integer> first = BisectSearch.findFirst(searchList, i -> i % 2 == 0);
      assertEquals(Optional.empty(), first);
      assertTrue(searchList.numGets <= baseMaxGets);
    }
    {
      int factor = 128;
      final List<Integer> nums = IntStream.iterate(1, i -> i + 2)
          .limit(baseFalseSize * factor)
          .boxed()
          .collect(Collectors.toList());
      final AccessCountingList<Integer> searchList = new AccessCountingList<>(nums);
      final Optional<Integer> first = BisectSearch.findFirst(searchList, i -> i % 2 == 0);
      assertEquals(Optional.empty(), first);
      assertTrue(searchList.numGets <= baseMaxGets + 7);
    }
  }

  private static class AccessCountingList<T> extends ArrayList<T> {
    int numGets = 0;

    AccessCountingList(List<T> list) {
      super(list);
    }

    @Override
    public T get(int index) {
      numGets++;
      return super.get(index);
    }
  }

}
