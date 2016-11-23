/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.runtime.plugins;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.runtime.ApplicationState;
import com.emc.ia.sipcreator.spi.PluginRegistry;

public final class PluginUtils {

  private PluginUtils() {
    throw new IllegalStateException(
        "PluginUtils is a static utility class and its constructor should never be called.");
  }

  public static PluginContext context(ApplicationState app, Config cfg) {
    return new PluginContextImpl(app, app.getConfiguration(), cfg);
  }

  public static <T> T create(Class<T> type, Config config, ApplicationState state, PluginRegistry plugins) {
    return plugins.create(type, config.getName(), context(state, config));
  }

}
