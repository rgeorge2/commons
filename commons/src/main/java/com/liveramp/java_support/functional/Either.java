package com.liveramp.java_support.functional;

import java.io.Serializable;
import java.util.function.Function;

import com.google.common.base.Optional;

public class Either<LEFT, RIGHT> implements Serializable {

  private final Optional<LEFT> optionalLeft;
  private final Optional<RIGHT> optionalRight;

  public static <LEFT, RIGHT> Either<LEFT, RIGHT> left(LEFT l) {
    return new Either<>(Optional.<LEFT>of(l), Optional.<RIGHT>absent());
  }

  public static <LEFT, RIGHT> Either<LEFT, RIGHT> right(RIGHT r) {
    return new Either<>(Optional.<LEFT>absent(), Optional.<RIGHT>of(r));
  }

  private Either(Optional<LEFT> l, Optional<RIGHT> r) {
    optionalLeft = l;
    optionalRight = r;
  }

  public boolean isLeft() {
    return optionalLeft.isPresent();
  }

  public boolean isRight() {
    return optionalRight.isPresent();
  }

  public LEFT getLeft() {
    return optionalLeft.get();
  }

  public RIGHT getRight() {
    return optionalRight.get();
  }

  public <T> Either<T, RIGHT> mapLeft(Function<LEFT, T> fn) {
    if (this.isLeft()) {
      return Either.left(fn.apply(this.getLeft()));
    } else {
      return Either.right(this.getRight());
    }
  }

  public <T> Either<LEFT, T> mapRight(Function<RIGHT, T> fn) {
    if (this.isRight()) {
      return Either.right(fn.apply(this.getRight()));
    } else {
      return Either.left(this.getLeft());
    }
  }

  @Override
  public String toString() {
    return String.format("Either(left:%s, right:%s)", isLeft() ? getLeft() : null, isRight() ? getRight() : null);
  }

  public static <T> T collapse(Either<T, T> input) {
    if (input.isRight()) {
      return input.getRight();
    } else {
      return input.getLeft();
    }
  }

  public static <L, R> Either<L, R> collapseRight(Either<Either<L, R>, R> input) {
    if (input.isRight()) {
      return Either.right(input.getRight());
    } else if (input.getLeft().isRight()) {
      return Either.right(input.getLeft().getRight());
    } else {
      return Either.left(input.getLeft().getLeft());
    }
  }

  public static <L, R> Either<L, R> collapseLeft(Either<L, Either<L, R>> input) {
    if (input.isLeft()) {
      return Either.left(input.getLeft());
    } else if (input.getRight().isLeft()) {
      return Either.left(input.getRight().getLeft());
    } else {
      return Either.right(input.getRight().getRight());
    }
  }

  public static <L, R> Either<R, L> reverse(Either<L, R> input) {
    if (input.isLeft()) {
      return Either.right(input.getLeft());
    } else {
      return Either.left(input.getRight());
    }
  }

}
