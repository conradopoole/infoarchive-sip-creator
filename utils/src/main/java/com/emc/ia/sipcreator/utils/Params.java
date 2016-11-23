/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public final class Params {

  private Params() {
    throw new IllegalStateException("Params is a static utility class and its constructor should never be called.");
  }

  public static void notNull(final Object value, final String name) {
    if (value == null) {
      throw new IllegalArgumentException("Parameter '" + name + "' may not be null.");
    }
  }

  public static void notEmpty(final String value, final String name) {
    if (StringUtils.isEmpty(value)) {
      throw new IllegalArgumentException("Parameter '" + name + "' may not be null or empty.");
    }
  }

  public static void notBlank(final String value, final String name) {
    if (StringUtils.isBlank(value)) {
      throw new IllegalArgumentException("Parameter '" + name + "' may not be null or blank.");
    }
  }

  public static boolean notFalse(final boolean argument, final String msg, final Object... args) {
    if (!argument) {
      throw new IllegalArgumentException(Fmt.format(msg, args));
    }
    return argument;
  }

  public static boolean notTrue(final boolean argument, final String msg, final Object... params) {
    if (argument) {
      throw new IllegalArgumentException(Fmt.format(msg, params));
    }
    return argument;
  }

  public static <T> void notEmpty(final Collection<T> values, final String name) {
    notNull(values, name);
    notTrue(values.isEmpty(), "Parameter '{}' may not be null or empty.", name);
  }

  public static <S, T> void notEmpty(final Map<S, T> map, final String name) {
    notNull(map, name);
    notTrue(map.isEmpty(), "Parameter '{}' may not be null or empty.", name);
  }

  public static <S, T> void contains(final Map<S, T> map, final S key, final String message, final Object... args) {

    notFalse(map != null && map.containsKey(key), message, args);
  }

  public static <T> void notEmpty(final T[] array, final String name) {
    notNull(array, name);
    notTrue(array.length == 0, "Parameter '{}' may not be null or empty.", name);

  }

  public static void atLeast(final Integer count, final int lowerLimit, final String name) {
    notFalse(count >= lowerLimit, "Parameter '{}' must be at least {} but was {}.", name, lowerLimit, count);

  }
}
