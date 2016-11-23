/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.runtime;

import java.util.HashMap;
import java.util.Map;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.spi.PluginRegistry;
import com.emc.ia.sipcreator.utils.Params;

public class ApplicationState {

  private final Config config;
  private final PluginRegistry plugins;
  private final Map<String, Object> objects;

  public ApplicationState(Config config, PluginRegistry plugins) {
    Params.notNull(config, "ApplicationState.config");
    this.config = config;
    this.plugins = plugins;
    this.objects = new HashMap<String, Object>();
  }

  public Config getConfiguration() {
    return config;
  }

  public PluginRegistry getPlugins() {
    return plugins;
  }

  @SuppressWarnings("unchecked")
  public <T> T getObject(Class<T> type, String id) {
    return (T)objects.get(id);
  }

  public <T> void setObject(String id, T object) {
    if (objects.containsKey(id)) {
      throw new IllegalStateException("Object with id " + id + " already exists.");
    }
    objects.put(id, object);
  }

  @Override
  public String toString() {
    return "ApplicationState [config=" + config + ", plugins=" + plugins + ", objects=" + objects + "]";
  }

}
