package com.liveramp.java_support.concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParallelUtil {

  public static <T> Future<T> call(Callable<T> callable) {
    ExecutorService executorService = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
    Future<T> submit = executorService.submit(callable);
    return new CloseExecutorFuture<T>(submit, executorService);
  }

  public static <T> List<Future<T>> call(List<? extends Callable<T>> callable) {
    ExecutorService executorService = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
    List<CloseExecutorFuture<T>> result = new ArrayList<>();
    for (Callable<T> tCallable : callable) {
      Future<T> submit = executorService.submit(tCallable);
      result.add(new CloseExecutorFuture<T>(submit, executorService));
    }
    for (CloseExecutorFuture<T> tCloseExecutorFuture : result) {
      tCloseExecutorFuture.setOthers(result);
    }

    return (List<Future<T>>)(List)result; //java is so incredibly stupid
  }

  public static <T> List<Future<T>> call(Callable<T> callable, Callable<T>... callables) {
    ArrayList<Callable<T>> callable1 = new ArrayList<>();
    callable1.add(callable);
    callable1.addAll(Arrays.asList(callables));
    return call(callable1);
  }

  public static <T> List<Future<Long>> time(Runnable runnable, Runnable... runnables) {
    ArrayList<Runnable> runnables1 = new ArrayList<>();
    runnables1.add(runnable);
    runnables1.addAll(Arrays.asList(runnables));
    return time(runnables1);
  }

  public static <T> Future<Long> time(Runnable runnable) {
    return time(Collections.singletonList(runnable)).get(0);
  }

  public static <T> List<Future<Long>> time(List<Runnable> runnables) {
    List<TimedRunnable> collect = runnables.stream().map(TimedRunnable::new).collect(Collectors.toList());
    return call(collect);
  }

  @Deprecated
  public static <T> List<T> await(List<Future<T>> futures) {
    return awaitIgnoringPotentialErrors(futures);
  }

  static <T> List<T> awaitIgnoringPotentialErrors(Collection<Future<T>> futures) {
    return futures.stream().map(f -> {
      try {
        return f.get();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      } catch (ExecutionException e) {
        throw new RuntimeException(e);
      }
    }).collect(Collectors.toList());
  }

  //Wait on a series of future to complete, but check each future with a timeout in a loop to ensure that any
  //errors in the futures are thrown immediately.
  public static void awaitErrorSafe(Collection<Future<?>> futures, long waitBetweenLoops, TimeUnit amount) {
    boolean allComplete = false;

    while (!allComplete) {
      try {
        amount.sleep(waitBetweenLoops);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      allComplete = true;
      for (Future<?> future : futures) {
        try {
          future.get(1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
          allComplete = false;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public static void awaitErrorSafe(Collection<Future<?>> futures) {
    awaitErrorSafe(futures, 1, TimeUnit.SECONDS);
  }

  private static class TimedRunnable implements Callable<Long> {

    Runnable r;

    public TimedRunnable(Runnable r) {
      this.r = r;
    }

    @Override
    public Long call() throws Exception {
      long start = System.currentTimeMillis();
      r.run();
      return System.currentTimeMillis() - start;
    }
  }

  private static class CloseExecutorFuture<T> implements Future<T> {

    Future<T> internal;
    ExecutorService service;
    boolean done;
    List<CloseExecutorFuture<T>> others = new ArrayList<>();

    public CloseExecutorFuture(Future<T> internal, ExecutorService service) {
      this.internal = internal;
      this.service = service;
    }

    public void setOthers(List<CloseExecutorFuture<T>> others) {
      this.others = others;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
      boolean cancel = internal.cancel(mayInterruptIfRunning);
      tryShutdown();
      return cancel;
    }

    private void tryShutdown() {
      this.done = true;
      if (othersDone()) {
        service.shutdown();
      }
    }

    private boolean othersDone() {
      for (CloseExecutorFuture<T> other : others) {
        if (!other.done) {
          return false;
        }
      }
      return true;
    }

    @Override
    public boolean isCancelled() {
      return internal.isCancelled();
    }

    @Override
    public boolean isDone() {
      return internal.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
      T t = internal.get();
      tryShutdown();
      return t;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      T t = internal.get(timeout, unit);
      tryShutdown();
      return t;
    }
  }

  private static Logger LOG = LoggerFactory.getLogger(ParallelUtil.class);

  public static boolean runWithTimeout(Runnable r, long timeout, TimeUnit unit) {
    Optional<Object> result = runWithTimeout(() -> {
      r.run();
      return new Object();
    }, timeout, unit);
    return result.isPresent();
  }

  public static <T> Optional<T> runWithTimeout(Callable<T> r, long timeout, TimeUnit unit) {
    ExecutorService service = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
    Future<T> submit = service.submit(r);
    try {
      T t = submit.get(timeout, unit);
      service.shutdownNow();
      return Optional.ofNullable(t);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    } catch (TimeoutException e) {
      submit.cancel(true);
      service.shutdownNow();
      return Optional.empty();
    }
  }
}