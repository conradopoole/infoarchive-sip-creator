/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.function.Function;

public class DelegatingFunction<T, R> implements Function<T, R> {

  private Function<T, R> delegate;

  public DelegatingFunction() {
  }

  public DelegatingFunction(Function<T, R> delegate) {
    // Null is ok, since we might set later.
    this.delegate = delegate;
  }

  public Function<T, R> getDelegate() {
    return delegate;
  }

  public void setDelegate(Function<T, R> delegate) {
    // Null is ok, since we might want to clear it out.
    this.delegate = delegate;
  }

  @Override
  public R apply(T t) {
    return delegate.apply(t);
  }

  @Override
  public String toString() {
    return "DelegatingFunction [delegate=" + delegate + "]";
  }
}
