/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class SafeCallable<T> implements Callable<T> {

  private final Callable<T> callable;
  private final Function<Throwable, T> onError;

  public SafeCallable(Callable<T> callable, Function<Throwable, T> onError) {
    Params.notNull(callable, "SafeCallable.callable");
    Params.notNull(onError, "SafeCallable.onError");

    this.callable = callable;
    this.onError = onError;
  }

  @Override
  public T call() {
    try {
      return callable.call();
    } catch (Exception e) {
      return onError.apply(e);
    }
  }

  public static <T> SafeCallable<T> ofConstant(Callable<T> object, T onErrorResult) {
    return SafeCallable.of(object, ConstantFunction.of(onErrorResult));
  }

  public static <T> SafeCallable<T> of(Callable<T> object, Function<Throwable, T> onError) {
    return new SafeCallable<T>(object, onError);
  }

  @Override
  public String toString() {
    return "SafeCallable [callable=" + callable + ", onError=" + onError + "]";
  }
}
