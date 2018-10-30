package com.liveramp.java_support.functional;

public interface ExFunction<T, R> {
  R apply(T t) throws Exception;
}
