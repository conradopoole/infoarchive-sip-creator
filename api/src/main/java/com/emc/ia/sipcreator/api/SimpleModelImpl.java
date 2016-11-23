/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.util.HashMap;
import java.util.Map;

public class SimpleModelImpl implements Model {

  private final Map<String, Object> map = new HashMap<String, Object>();

  @Override
  public Object get(String name) {
    return map.get(name);
  }

  @Override
  public void set(String name, Object value) {
    map.put(name, value);
  }

  @Override
  public String toString() {
    return "SimpleModelImpl [map=" + map + "]";
  }

}
