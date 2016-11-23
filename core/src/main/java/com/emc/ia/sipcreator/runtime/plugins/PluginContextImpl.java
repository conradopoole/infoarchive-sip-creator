/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.runtime.plugins;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.runtime.ApplicationState;
import com.emc.ia.sipcreator.utils.Params;

public class PluginContextImpl implements PluginContext {

  private final Config rootConfig;
  private final Config config;
  private final ApplicationState appState;

  public PluginContextImpl(ApplicationState appState, Config rootConfig, Config config) {
    Params.notNull(appState, "PluginContextImpl.appState");
    Params.notNull(rootConfig, "PluginContextImpl.rootConfig");
    // Null config is ok.

    this.appState = appState;
    this.rootConfig = rootConfig;
    this.config = config;
  }

  @Override
  public Config getConfig() {
    return config;
  }

  @Override
  public Config getRootConfig() {
    return rootConfig;
  }

  @Override
  public <T> T newObject(Class<T> type, String id, Config cfg) {
    final PluginContext context = PluginUtils.context(appState, cfg);
    final T plugin = appState.getPlugins()
      .create(type, id, context);
    // appState.setObject(id, object);
    return plugin;
  }

  @Override
  public <T> T getObject(Class<T> type, String id) {
    return appState.getObject(type, id);
  }

  @Override
  public String toString() {
    return "PluginContextImpl [rootConfig=" + rootConfig + ", config=" + config + ", appState=" + appState + "]";
  }

  @Override
  public ConfigUtils configUtils() {
    return getObject(ConfigUtils.class, "configUtils");
  }

}
