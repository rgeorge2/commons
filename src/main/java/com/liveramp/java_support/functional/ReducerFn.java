package com.liveramp.java_support.functional;

import java.io.Serializable;
import java.util.function.BiFunction;

public interface ReducerFn<Accumulated, Value> extends Serializable, BiFunction<Accumulated, Value, Accumulated>, Fn2<Accumulated, Value, Accumulated> {

  Accumulated reduce(Accumulated accumulated, Value value);

  default Accumulated apply(Accumulated accumulated, Value value) {
    return reduce(accumulated, value);
  }

}
