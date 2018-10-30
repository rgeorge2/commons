package com.liveramp.commons.collections;

import java.util.Iterator;

public class Iteratorable<A> implements Iterable<A> {

  private Iterator<A> internal;
  public Iteratorable(Iterator<A> iter){
    if(iter == null){
      throw new RuntimeException("Iteratorable cannot use null iterator!");
    }
    this.internal = iter;
  }

  @Override
  public Iterator<A> iterator() {
    if(internal == null){
      throw new RuntimeException("Iteratorable cannot be iterated over twice!");
    }
    Iterator<A> iter = internal;
    internal = null;
    return iter;
  }

  public static <A> Iteratorable<A> of(Iterator<A> iter){
    return new Iteratorable<A>(iter);
  }
}
