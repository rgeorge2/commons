package com.liveramp.java_support.functional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.liveramp.commons.collections.Iteratorable;
import com.liveramp.commons.collections.nested_map.ThreeNestedMap;
import com.liveramp.commons.collections.nested_map.TwoNestedMap;

public class Fns8 {

  public static <A, B, C> Fn<A, Function<B, C>> curry(BiFunction<A, B, C> bifn) {
    return a -> (b -> bifn.apply(a, b));
  }

  public static <A, B> Fn<Stream<A>, Stream<B>> liftS(Function<? super A, ? extends B> f) {
    return s -> s.map(f);
  }

  public static <A, B> Fn<Optional<A>, Optional<B>> liftO(Function<A, B> f) {
    return s -> s.map(f);
  }

  public static <A, B, C> BiFunction<Optional<A>, Optional<B>, Optional<C>> optionalize(BiFunction<A, B, C> bifn) {
    return (BiFunction<Optional<A>, Optional<B>, Optional<C>> & Serializable)
        (a, b) -> a.flatMap(arg1 -> b.map(arg2 -> bifn.apply(arg1, arg2)));
  }

  public static <A> Optional<A> asNewOpt(com.google.common.base.Optional<A> opt) {
    return Optional.ofNullable(opt.orNull());
  }

  public static <A, B> Fn<Iterator<A>, Iterator<B>> mapItr(Function<A, B> fn) {
    return itr -> Iterators.transform(itr, toG(fn));
  }

  public static <A> Fn<Iterator<A>, Iterator<A>> filterItr(Predicate<A> fn) {
    return itr -> Iterators.filter(itr, toG(fn));
  }

  public static <A, B> Fn<Iterator<A>, Iterator<B>> flatMapItr(Function<A, Iterator<B>> fn) {
    return itr -> Iterators.concat(Iterators.transform(itr, toG(fn)));
  }

  public static <A, B> Fn<Iterator<A>, Iterator<B>> aggItr(Function<Iterator<A>, B> fn) {
    return itr -> Iterators.singletonIterator(fn.apply(itr));
  }

  public static <A, B> Fn<Iterator<A>, B> asAggregator(B identity, ReducerFn<B, A> fn) {
    return new ToAgg(identity, fn);
  }

  public static <A, B> Fn<Iterator<A>, B> asAggregator1(ReducerFn<B, A> fn) {
    return itr -> {
      ToAgg<A, B> toAgg = new ToAgg(itr.next(), fn);
      return toAgg.apply(itr);
    };
  }

  private static <A, B> com.google.common.base.Function<A, B> toG(Function<A, B> fn) {
    return x -> fn.apply(x);
  }

  private static <A, B> com.google.common.base.Predicate<A> toG(Predicate<A> fn) {
    return x -> fn.test(x);
  }


  public static <A> CompositionCollector<A> compositionCollector() {
    return new CompositionCollector<A>();
  }

  public static <T, K, V> Collector<T, Multimap<K, V>, Multimap<K, V>> toMultimap(Function<T, K> keyFn, Function<T, V> valFn) {
    return new Collector<T, Multimap<K, V>, Multimap<K, V>>() {
      @Override
      public Supplier<Multimap<K, V>> supplier() {
        return HashMultimap::create;
      }

      @Override
      public BiConsumer<Multimap<K, V>, T> accumulator() {
        return (m, t) -> m.put(keyFn.apply(t), valFn.apply(t));
      }

      @Override
      public BinaryOperator<Multimap<K, V>> combiner() {
        return (m1, m2) -> {
          m1.putAll(m2);
          return m1;
        };
      }

      @Override
      public Function<Multimap<K, V>, Multimap<K, V>> finisher() {
        return Function.identity();
      }

      @Override
      public Set<Characteristics> characteristics() {
        return Sets.newHashSet(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
      }
    };
  }

  public static <K, V, V2> Map<K, V2> mapValues(Map<K, V> map, Function<V, V2> fn) {
    return map.entrySet().stream().collect(Collectors.toMap(
        e -> e.getKey(),
        e -> fn.apply(e.getValue())
    ));
  }

  public static <K, V, K2> Map<K2, V> mapKeys(Map<K, V> map, Function<K, K2> fn) {
    return map.entrySet().stream().collect(Collectors.toMap(
        e -> fn.apply(e.getKey()),
        e -> e.getValue()
    ));
  }

  public static <K, V> Map<K, V> filterKeys(Map<K, V> map, Predicate<K> pred) {
    return map.entrySet().stream().filter(e -> pred.test(e.getKey()))
        .collect(Collectors.toMap(
            e -> e.getKey(),
            e -> e.getValue()
        ));
  }

  public static <K, V> Map<K, V> filterValues(Map<K, V> map, Predicate<V> pred) {
    return map.entrySet().stream().filter(e -> pred.test(e.getValue()))
        .collect(Collectors.toMap(
            e -> e.getKey(),
            e -> e.getValue()
        ));
  }

  public static <K, V> Collector<Map.Entry<K, V>, HashMap<K, V>, HashMap<K, V>> toMap() {
    return toMap(e -> e.getKey(), e -> e.getValue(), HashMap::new);
  }

  public static <K, V> Collector<Pair<K, V>, HashMap<K, V>, HashMap<K, V>> pairsToMap() {
    return toMap(e -> e.getKey(), e -> e.getValue(), HashMap::new);
  }

  public static <K, V, M extends Map<K, V>, P> Collector<P, M, M> toMap(Function<P, K> keyFunc, Function<P, V> valFunc, Supplier<M> supplier) {
    return new Collector<P, M, M>() {
      @Override
      public Supplier<M> supplier() {
        return supplier;
      }

      @Override
      public BiConsumer<M, P> accumulator() {
        return (m, p) -> m.put(keyFunc.apply(p), valFunc.apply(p));
      }

      @Override
      public BinaryOperator<M> combiner() {
        return (m1, m2) -> {
          m1.putAll(m2);
          return m1;
        };
      }

      @Override
      public Function<M, M> finisher() {
        return Function.identity();
      }

      @Override
      public Set<Characteristics> characteristics() {
        return Sets.newHashSet(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
      }
    };
  }

  private static class ToAgg<A, B> implements Fn<Iterator<A>, B> {
    private B identity;
    private ReducerFn<B, A> fn;

    public ToAgg(B identity, ReducerFn<B, A> fn) {
      this.identity = identity;
      this.fn = fn;
    }

    @Override
    public B apply(Iterator<A> aIterator) {
      B accum = identity;
      while (aIterator.hasNext()) {
        A next = aIterator.next();
        accum = fn.apply(accum, next);
      }
      return accum;
    }
  }

  private static class FnContainer<A, B> {
    private Function<A, B> fn;

    public FnContainer(Function<A, B> fn) {
      this.fn = fn;
    }
  }

  private static class CompositionCollector<A> implements Collector<Function<A, A>, FnContainer<A, A>, Function<A, A>> {

    private Function<A, A> result;

    @Override
    public Supplier<FnContainer<A, A>> supplier() {
      return () -> new FnContainer<A, A>(Function.identity());
    }

    @Override
    public BiConsumer<FnContainer<A, A>, Function<A, A>> accumulator() {
      return (f1, f2) -> f1.fn = f1.fn.andThen(f2);
    }

    @Override
    public BinaryOperator<FnContainer<A, A>> combiner() {
      return (f1, f2) -> new FnContainer<A, A>(f1.fn.andThen(f2.fn));
    }

    @Override
    public Function<FnContainer<A, A>, Function<A, A>> finisher() {
      return fc -> fc.fn;
    }

    @Override
    public Set<Characteristics> characteristics() {
      return Sets.newHashSet();
    }
  }

  public static <T> Iterator<Iterator<T>> split(Iterator<T> itr, Splitter<T> splitter) {
    return new PartitionedIterator<T>(splitter, itr);
  }

  public interface Splitter<T> extends Serializable {

    boolean shouldSplit(T previous, T current);
  }

  private static class PartitionedIterator<T> implements Iterator<Iterator<T>> {

    private final Splitter<T> splitPred;
    private final PeekingIterator<T> internal;
    private SplitSubIterator<T> lastItr = null;

    public PartitionedIterator(Splitter<T> splitPred, Iterator<T> internal) {
      this.splitPred = splitPred;
      this.internal = Iterators.peekingIterator(internal);
    }

    @Override
    public boolean hasNext() {
      exhaustPreviousIfNecessary();
      return internal.hasNext();
    }

    private void exhaustPreviousIfNecessary() {
      if (lastItr != null && lastItr.hasNext()) {
        while (lastItr.hasNext()) {
          lastItr.next();
        }
        lastItr.markInvalid();
        lastItr = null;
      }
    }

    @Override
    public Iterator<T> next() {
      exhaustPreviousIfNecessary();
      SplitSubIterator<T> iterator = new SplitSubIterator<>(internal, splitPred);
      lastItr = iterator;
      return iterator;

    }

    private static class SplitSubIterator<T> implements Iterator<T> {

      T prev = null;
      PeekingIterator<T> internal;
      private Splitter<T> splitPred;
      private boolean invalid = false;

      public SplitSubIterator(PeekingIterator<T> internal, Splitter<T> splitPred) {
        this.internal = internal;
        this.splitPred = splitPred;
      }

      @Override
      public boolean hasNext() {
        throwIfInvalid();
        return internal.hasNext() && (prev == null || !splitPred.shouldSplit(prev, internal.peek()));
      }

      private void throwIfInvalid() {
        if (invalid) {
          throw new IllegalStateException("This iterator was made invalid due to " +
              "retrieval of next iterator in a PartitionedIterator. ");
        }

      }

      @Override
      public T next() {
        throwIfInvalid();
        T next = internal.next();
        prev = next;
        return next;
      }

      public void markInvalid() {
        this.invalid = true;
      }
    }
  }

  public static <T> List<T> take(Iterator<T> itr, int amount) {
    ArrayList<T> result = Lists.newArrayList();
    for (int i = 0; i < amount && itr.hasNext(); i++) {
      result.add(itr.next());
    }
    return result;
  }

  public static <T> Stream<T> asStream(Iterator<T> itr) {
    return StreamSupport.stream(Iteratorable.of(itr).spliterator(), false);
  }

  public static <F, T> Fn<F, T> noThrow(ExFunction<F, T> fn) {
    return new NoThrowFn<>(fn);
  }

  public static class NoThrowFn<A, B> implements Fn<A, B> {

    private ExFunction<A, B> internal;

    public NoThrowFn(ExFunction<A, B> internal) {
      this.internal = internal;
    }

    @Override
    public B apply(A a) {
      try {
        return internal.apply(a);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static <T> Stream<T> dropWhile(Stream<T> stream, final Predicate<T> pred) {
    final AtomicBoolean predicateFailed = new AtomicBoolean(false);
    Predicate<T> statefulPredicate = t -> {
      if (!predicateFailed.get()) {
        if (pred.test(t)) {
          return false;
        } else {
          predicateFailed.set(true);
        }
      }
      return true;
    };
    return stream.filter(statefulPredicate);
  }

  public static <T> Stream<T> dropUntil(Stream<T> stream, final Predicate<T> pred) {
    return dropWhile(stream, pred.negate());
  }

  public static <T> Stream<T> drop(Stream<T> stream, int toDrop) {
    Predicate<T> dropPred = new Predicate<T>() {

      int dropped = 0;

      @Override
      public boolean test(T o) {
        return dropped++ < toDrop;
      }
    };

    return dropWhile(stream, dropPred);
  }

  public static <T> Stream<T> takeWhile(Stream<T> stream, final Predicate<T> pred) {
    final AtomicBoolean predicateFailed = new AtomicBoolean(false);
    Predicate<T> statefulPredicate = t -> {
      if (!predicateFailed.get()) {
        if (pred.test(t)) {
          return true;
        } else {
          predicateFailed.set(true);
        }
      }
      return false;
    };
    return stream.filter(statefulPredicate);
  }

  public static <T> Stream<T> takeUntil(Stream<T> stream, final Predicate<T> pred) {
    return takeWhile(stream, pred.negate());
  }

  public static <T> Supplier<T> memoize(Supplier<T> supplier) {
    return new Supplier<T>() {
      private T cachedValue = null;

      @Override
      public synchronized T get() {
        if (cachedValue == null) {
          cachedValue = supplier.get();
        }
        return cachedValue;
      }
    };
  }

  public static <A, B, C> Function<Pair<A, B>, Pair<A, C>> onSnd(Function<B, C> fn) {
    return p -> Pair.of(p.getKey(), fn.apply(p.getValue()));
  }

  public static <T> Iterator<T> delayedIterator(Supplier<Iterator<T>> supplier) {
    return new DelayedIterator<>(supplier);
  }

  public static <T> Stream<T> delayedStream(Supplier<Stream<T>> supplier) {
    return asStream(delayedIterator(() -> supplier.get().iterator()));
  }

  public static class DelayedIterator<T> implements Iterator<T> {
    private Supplier<Iterator<T>> supplier;
    private Iterator<T> internal;

    public DelayedIterator(Supplier<Iterator<T>> supplier) {
      this.supplier = supplier;
    }

    @Override
    public boolean hasNext() {
      init();
      return internal.hasNext();
    }

    private void init() {
      if (internal == null) {
        internal = supplier.get();
      }
    }

    @Override
    public T next() {
      init();
      return internal.next();
    }
  }

  public static <A, K1, K2, V> Collector<A, TwoNestedMap<K1, K2, V>, TwoNestedMap<K1, K2, V>> toTwoNestedMap(
      Function<A, K1> key1Fn,
      Function<A, K2> key2Fn,
      Function<A, V> valFn) {
    return new TwoNestedMapCollector<>(key1Fn, key2Fn, valFn);

  }

  public static <K1, K2, V> Collector<Triple<K1, K2, V>, TwoNestedMap<K1, K2, V>, TwoNestedMap<K1, K2, V>> toTwoNestedMap() {
    return new TwoNestedMapCollector<>(Triple::getLeft, Triple::getMiddle, Triple::getRight);

  }

  public static <A, K1, K2, K3, V> Collector<A, ThreeNestedMap<K1, K2, K3, V>, ThreeNestedMap<K1, K2, K3, V>> toThreeNestedMap(
      Function<A, K1> key1Fn,
      Function<A, K2> key2Fn,
      Function<A, K3> key3Fn,
      Function<A, V> valFn) {
    return new ThreeNestedMapCollector<>(key1Fn, key2Fn, key3Fn, valFn);
  }

  private static class TwoNestedMapCollector<K1, K2, V, A> implements Collector<A, TwoNestedMap<K1, K2, V>, TwoNestedMap<K1, K2, V>> {

    private final Function<A, K1> key1Fn;
    private final Function<A, K2> key2Fn;
    private final Function<A, V> valFn;

    public TwoNestedMapCollector(Function<A, K1> key1Fn, Function<A, K2> key2Fn, Function<A, V> valFn) {
      this.key1Fn = key1Fn;
      this.key2Fn = key2Fn;
      this.valFn = valFn;
    }

    @Override
    public Supplier<TwoNestedMap<K1, K2, V>> supplier() {
      return TwoNestedMap::new;
    }

    @Override
    public BiConsumer<TwoNestedMap<K1, K2, V>, A> accumulator() {
      return (map, element) -> map.put(key1Fn.apply(element), key2Fn.apply(element), valFn.apply(element));
    }

    @Override
    public BinaryOperator<TwoNestedMap<K1, K2, V>> combiner() {
      return (m1, m2) -> {
        m1.putAll(m2);
        return m1;
      };
    }

    @Override
    public Function<TwoNestedMap<K1, K2, V>, TwoNestedMap<K1, K2, V>> finisher() {
      return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
      return Sets.newHashSet(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }
  }

  private static class ThreeNestedMapCollector<K1, K2, K3, V, A> implements Collector<A, ThreeNestedMap<K1, K2, K3, V>, ThreeNestedMap<K1, K2, K3, V>> {

    private final Function<A, K1> key1Fn;
    private final Function<A, K2> key2Fn;
    private final Function<A, K3> key3Fn;

    private final Function<A, V> valFn;

    public ThreeNestedMapCollector(Function<A, K1> key1Fn, Function<A, K2> key2Fn, Function<A, K3> key3Fn, Function<A, V> valFn) {
      this.key1Fn = key1Fn;
      this.key2Fn = key2Fn;
      this.key3Fn = key3Fn;
      this.valFn = valFn;
    }

    @Override
    public Supplier<ThreeNestedMap<K1, K2, K3, V>> supplier() {
      return ThreeNestedMap::new;
    }

    @Override
    public BiConsumer<ThreeNestedMap<K1, K2, K3, V>, A> accumulator() {
      return (map, element) -> map.put(key1Fn.apply(element), key2Fn.apply(element), key3Fn.apply(element), valFn.apply(element));
    }

    @Override
    public BinaryOperator<ThreeNestedMap<K1, K2, K3, V>> combiner() {
      return (m1, m2) -> {
        m1.putAll(m2);
        return m1;
      };
    }

    @Override
    public Function<ThreeNestedMap<K1, K2, K3, V>, ThreeNestedMap<K1, K2, K3, V>> finisher() {
      return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
      return Sets.newHashSet(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }
  }

  public static <T, A, In, Out> Collector<T, A, Out> map(Collector<T, A, In> coll, Function<In, Out> fn) {
    return new Collector<T, A, Out>() {
      @Override
      public Supplier<A> supplier() {
        return coll.supplier();
      }

      @Override
      public BiConsumer<A, T> accumulator() {
        return coll.accumulator();
      }

      @Override
      public BinaryOperator<A> combiner() {
        return coll.combiner();
      }

      @Override
      public Function<A, Out> finisher() {
        return coll.finisher().andThen(fn);
      }

      @Override
      public Set<Characteristics> characteristics() {
        return coll.characteristics();
      }
    };
  }
}
