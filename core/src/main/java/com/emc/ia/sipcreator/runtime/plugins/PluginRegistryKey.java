/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.runtime.plugins;

import org.apache.commons.lang3.ObjectUtils;

import com.emc.ia.sipcreator.utils.Params;

public final class PluginRegistryKey {

  private final Class<?> type;
  private final String id;

  public PluginRegistryKey(Class<?> type, String id) {
    Params.notNull(type, "PluginRegistryKey.type");
    Params.notBlank(id, "PluginRegistryKey.id");
    this.type = type;
    this.id = id;
  }

  public Class<?> getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PluginRegistryKey other = (PluginRegistryKey)obj;
    if (!ObjectUtils.equals(id, other.id)) {
      return false;
    }

    return ObjectUtils.equals(type, other.type);
  }

  @Override
  public String toString() {
    return "PluginRegistryKey [type=" + type.getCanonicalName() + ", id=" + id + "]";
  }

}
