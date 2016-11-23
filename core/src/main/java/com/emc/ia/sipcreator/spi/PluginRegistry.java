/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.spi;

import java.util.List;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.api.PluginContext;

public interface PluginRegistry {

  List<Plugin> getAllPlugins();

  <T> T create(Class<T> type, String id, PluginContext context);

  List<Plugin> getPluginsById(String id);
}
