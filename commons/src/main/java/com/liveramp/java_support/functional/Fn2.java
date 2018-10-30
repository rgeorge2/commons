package com.liveramp.java_support.functional;

import java.io.Serializable;
import java.util.function.BiFunction;

public interface Fn2<A1, A2, R> extends BiFunction<A1, A2, R>, Serializable {
}
