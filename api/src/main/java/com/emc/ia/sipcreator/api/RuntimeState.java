/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class RuntimeState {

  private final Map<String, Object> variables;

  public RuntimeState() {
    variables = new HashMap<String, Object>();
  }

  public void set(String name, Object value) {
    variables.put(name, value);
  }

  public Object get(String name) {
    return variables.get(name);
  }

  @Override
  public String toString() {
    return "RuntimeContext [variables=" + variables + "]";
  }

  public void defineCounter(String counter) {
    defineCounter(counter, 0L);
  }

  public void defineCounter(String counter, long value) {
    variables.put(counter, new AtomicLong(value));
  }

  public long inc(String counter) {
    return ((AtomicLong)variables.get(counter)).incrementAndGet();
  }

  public long getCount(String counter) {
    return ((AtomicLong)variables.get(counter)).get();
  }

  public void setCount(String counter, long value) {
    ((AtomicLong)variables.get(counter)).set(value);

  }

}
