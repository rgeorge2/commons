package com.liveramp.java_support.functional;

import java.io.Serializable;

import com.google.common.base.Function;

public interface Fn<F, T> extends Function<F, T>, Serializable, java.util.function.Function<F, T> {

  public static <T> Fn<T, T> identity() {
    return new Identity<>();
  }

  public static class Identity<T> implements Fn<T, T> {
    @Override
    public T apply(T t) {
      return t;
    }
  }
}
