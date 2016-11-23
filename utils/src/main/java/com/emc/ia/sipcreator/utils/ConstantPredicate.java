/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.function.Predicate;

public class ConstantPredicate<T> implements Predicate<T> {

  private final boolean object;

  public ConstantPredicate(boolean object) {
    this.object = object;
  }

  @Override
  public boolean test(T t) {
    return object;
  }

  public static <T> ConstantPredicate<T> of(boolean object) {
    return new ConstantPredicate<T>(object);
  }

  @Override
  public String toString() {
    return "ConstantPredicate [object=" + object + "]";
  }

}
