/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.function.Function;

public class ConstantFunction<T, R> implements Function<T, R> {

  private final R object;

  public ConstantFunction(R object) {
    this.object = object;
  }

  @Override
  public R apply(T t) {
    return object;
  }

  public static <T, R> ConstantFunction<T, R> of(R object) {
    return new ConstantFunction<T, R>(object);
  }

  @Override
  public String toString() {
    return "ConstantFunction [object=" + object + "]";
  }

}
