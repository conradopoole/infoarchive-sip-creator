/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.logging;

import java.util.List;

import com.emc.ia.sipcreator.api.Plugin;

public final class SipCreatorInitLog {

  private SipCreatorInitLog() {
    throw new IllegalStateException(
        "SipCreatorInitLog is a static utility class and its constructor should never be called.");
  }

  public static void pluginCreationFailed(Plugin plugin, Throwable t) {
    SipCreatorLoggers.INIT.error(Messages.pluginCreationFailed(plugin, t));
  }

  public static void pluginInstanceIsNull(Plugin plugin) {
    SipCreatorLoggers.INIT.error(Messages.pluginInstanceIsNull(plugin));
  }

  public static void pluginNotUnique(Plugin existingPlugin, Plugin newPlugin) {
    SipCreatorLoggers.INIT.error(Messages.pluginNotUnique(existingPlugin, newPlugin));
  }

  public static void pluginTypeInvalid(Plugin plugin) {
    SipCreatorLoggers.INIT.error(Messages.pluginTypeInvalid(plugin));
  }

  public static void pluginIdInvalid(Plugin plugin) {
    SipCreatorLoggers.INIT.error(Messages.pluginIdInvalid(plugin));
  }

  public static void loadedPlugin(Plugin plugin) {
    SipCreatorLoggers.INIT.info(Messages.loadedPlugin(plugin));
  }

  public static void pluginInstanceTypeInvalid(Plugin plugin, Object object) {
    SipCreatorLoggers.INIT.info(Messages.pluginInstanceTypeInvalid(plugin, object));

  }

  public static void pluginCreated(Plugin plugin, Object object) {
    SipCreatorLoggers.INIT.info(Messages.pluginCreated(plugin, object));

  }

  public static <T> void pluginUnknown(String id, Class<T> type) {
    SipCreatorLoggers.INIT.error(Messages.pluginUnknown(id, type));
  }

  public static void pluginAmbigous(String id, List<Plugin> plugins) {
    SipCreatorLoggers.INIT.error(Messages.pluginAmbigous(id, plugins));

  }

}
