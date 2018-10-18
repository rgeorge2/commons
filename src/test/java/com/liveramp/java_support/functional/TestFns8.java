package com.liveramp.java_support.functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

public class TestFns8 {

  @Test
  public void testSplitIterator() {

    ArrayList<Integer> input = Lists.newArrayList(1, 2, 3, 5, 6, 7, 9, 10, 12);

    Iterator<Iterator<Integer>> split = Fns8.split(input.iterator(),
        (i1, i2) -> i2 - i1 > 1);

    List<List<Integer>> transform = Lists.newArrayList(Iterators.transform(split, Lists::newArrayList));

    Assert.assertEquals(Lists.newArrayList(Lists.newArrayList(1, 2, 3), Lists.newArrayList(5, 6, 7), Lists.newArrayList(9, 10), Lists.newArrayList(12)),
        transform);

  }

  @Test
  public void testNonExhaustingSplitIterator() {

    ArrayList<Integer> input = Lists.newArrayList(1, 2, 3, 5, 6, 7, 9, 10, 12);

    Iterator<Iterator<Integer>> split = Fns8.split(input.iterator(),
        (i1, i2) -> i2 - i1 > 1);

    Function<Iterator<Integer>, List<Integer>> takeFirst = itr -> Lists.newArrayList(itr.next());

    List<List<Integer>> transform = Lists.newArrayList(Iterators.transform(split, itr -> takeFirst.apply(itr)));

    Assert.assertEquals(Lists.newArrayList(Lists.newArrayList(1), Lists.newArrayList(5), Lists.newArrayList(9), Lists.newArrayList(12)),
        transform);

  }

  @Test
  public void testTake() {
    ArrayList<Integer> input = Lists.newArrayList(1, 2, 3, 5, 6, 7, 9, 10, 12);

    List<Integer> take = Fns8.take(input.iterator(), 3);
    Assert.assertEquals(Lists.newArrayList(1, 2, 3), take);
  }

  @Test
  public void testTakeDropWhileUntil() {

    ArrayList<Integer> numbers = Lists.newArrayList(1, 2, 3, 4, 5);

    Assert.assertEquals(
        Lists.newArrayList(1, 2),
        Fns8.takeWhile(numbers.stream(), i -> i < 3).collect(Collectors.toList())
    );

    Assert.assertEquals(
        Lists.newArrayList(1, 2, 3),
        Fns8.takeUntil(numbers.stream(), i -> i >= 4).collect(Collectors.toList())
    );

    Assert.assertEquals(
        Lists.newArrayList(5),
        Fns8.dropWhile(numbers.stream(), i -> i < 5).collect(Collectors.toList())
    );

    Assert.assertEquals(
        Lists.newArrayList(4, 5),
        Fns8.dropUntil(numbers.stream(), i -> i >= 4).collect(Collectors.toList())
    );


  }
}