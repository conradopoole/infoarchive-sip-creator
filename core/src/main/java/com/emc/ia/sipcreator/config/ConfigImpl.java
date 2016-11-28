/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.emc.ia.sipcreator.api.Config;

public class ConfigImpl implements Config {

  private final String name;
  private final Object value;
  private final String path;

  public ConfigImpl(String path, String name, Object value) {
    this.path = path;
    this.name = name;
    this.value = value;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Object getValue(String id) {
    return asMap().get(id);
  }

  private Map<?, ?> asMap() {
    return (Map<?, ?>)value;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public List<Config> getChildGroups() {
    if (value instanceof Map) {
      Map<?, ?> map = asMap();

      List<Config> configs = new ArrayList<Config>();
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        configs.add(new ConfigImpl(getPath(), String.valueOf(entry.getKey()), entry.getValue()));
      }
      return configs;
    } else if (value instanceof List) {
      List list = (List)value;
      List<Config> configs = new ArrayList<Config>();

      for (Object object : list) {
        if (object instanceof Map) {
          Map map = (Map)object;
          if (map.size() == 1) {
            String keyName = map.keySet()
              .iterator()
              .next()
              .toString();
            configs.add(new ConfigImpl(getPath(), keyName, map.get(keyName)));
          } else {
            configs.add(new ConfigImpl(getPath(), "", map));
          }
        } else {
          throw new IllegalArgumentException("No child groups. List.");
        }
      }
      return configs;

    } else {
      throw new IllegalArgumentException("No child groups.");

    }
  }

  @Override
  public boolean isCompound() {
    return value instanceof Map;
  }

  @Override
  public boolean hasValue() {
    return value != null;
  }

  public String getPath() {
    return path + "/" + name;
  }

}
