/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.concurrent.Callable;

public class ConstantCallable<T> implements Callable<T> {

  private final T object;

  public ConstantCallable(T object) {
    this.object = object;
  }

  @Override
  public T call() {
    return object;
  }

  public static <T> ConstantCallable<T> of(T object) {
    return new ConstantCallable<T>(object);
  }

  @Override
  public String toString() {
    return "ConstantCallable [object=" + object + "]";
  }

}
