package com.liveramp.java_support.concurrent;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestParallelUtil {
  @Test
  public void runWithTimeout() throws Exception {

    Runnable r = () -> {
      try {
        TimeUnit.MILLISECONDS.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    };

    boolean b = ParallelUtil.runWithTimeout(r, 10, TimeUnit.MILLISECONDS);
    assertFalse("Computation should have timed out", b);

    boolean b2 = ParallelUtil.runWithTimeout(r, 500, TimeUnit.MILLISECONDS);
    assertTrue("Computation should not have timed out", b2);

    Callable<Integer> c = () -> 1;

    Optional<Integer> i = ParallelUtil.runWithTimeout(c, 10, TimeUnit.MILLISECONDS);
    assertTrue(i.isPresent());

  }

}