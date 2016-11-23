/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.logging;

import java.util.List;
import java.util.ResourceBundle;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.utils.BriefExceptionFormatter;
import com.emc.ia.sipcreator.utils.Fmt;

public final class Messages {

  private static final ResourceBundle RES = ResourceBundle.getBundle("LogMessages");

  private static final String MSG_FORMAT = "{} : {}";

  public static final String SIPCREATOR_PLUGIN_CREATION_FAILED = get("SIPCREATOR_PLUGIN_CREATION_FAILED");
  public static final String SIPCREATOR_PLUGIN_INSTANCE_IS_NULL = get("SIPCREATOR_PLUGIN_INSTANCE_IS_NULL");
  public static final String SIPCREATOR_PLUGIN_NOT_UNIQUE = get("SIPCREATOR_PLUGIN_NOT_UNIQUE");
  public static final String SIPCREATOR_PLUGIN_TYPE_INVALID = get("SIPCREATOR_PLUGIN_TYPE_INVALID");
  public static final String SIPCREATOR_PLUGIN_ID_INVALID = get("SIPCREATOR_PLUGIN_ID_INVALID");
  public static final String SIPCREATOR_LOADED_PLUGIN = get("SIPCREATOR_LOADED_PLUGIN");
  public static final String SIPCREATOR_PLUGIN_INSTANCE_INVALID = get("SIPCREATOR_PLUGIN_INSTANCE_INVALID");
  public static final String SIPCREATOR_PLUGIN_UNKNOWN = get("SIPCREATOR_PLUGIN_UNKNOWN");
  public static final String SIPCREATOR_PLUGIN_CREATED = get("SIPCREATOR_PLUGIN_CREATED");
  public static final String SIPCREATOR_PLUGIN_AMBIGUOUS = get("SIPCREATOR_PLUGIN_AMBIGUOUS");

  private static BriefExceptionFormatter exceptionFormatter = new BriefExceptionFormatter();

  private Messages() {
    throw new IllegalStateException("Messages is a static utility class and its constructor should never be called.");
  }

  private static String get(final String id) {
    final String value = RES.getString(id);
    return Fmt.format(MSG_FORMAT, id, value);
  }

  public static String pluginCreationFailed(Plugin plugin, Throwable t) {
    return Fmt.format(SIPCREATOR_PLUGIN_CREATION_FAILED, plugin.getId(), pluginType(plugin), canonicalName(plugin),
        t.getMessage(), briefly(t));
  }

  private static String briefly(Throwable t) {
    return exceptionFormatter.format(t);
  }

  public static String pluginInstanceIsNull(Plugin plugin) {
    return Fmt.format(SIPCREATOR_PLUGIN_INSTANCE_IS_NULL, plugin.getId(), pluginType(plugin), canonicalName(plugin));
  }

  public static String pluginNotUnique(Plugin existingPlugin, Plugin newPlugin) {
    return Fmt.format(SIPCREATOR_PLUGIN_NOT_UNIQUE, newPlugin.getId(), pluginType(newPlugin),
        canonicalName(existingPlugin), canonicalName(newPlugin));
  }

  public static String pluginTypeInvalid(Plugin plugin) {
    return Fmt.format(SIPCREATOR_PLUGIN_TYPE_INVALID, plugin.getId(), canonicalName(plugin));
  }

  public static String pluginIdInvalid(Plugin plugin) {
    return Fmt.format(SIPCREATOR_PLUGIN_ID_INVALID, canonicalName(plugin));
  }

  public static String loadedPlugin(Plugin plugin) {
    return Fmt.format(SIPCREATOR_LOADED_PLUGIN, plugin.getId(), pluginType(plugin), plugin);
  }

  public static String pluginCreated(Plugin plugin, Object object) {
    return Fmt.format(SIPCREATOR_PLUGIN_CREATED, plugin.getId(), pluginType(plugin), plugin, object,
        canonicalName(object));
  }

  public static String pluginInstanceTypeInvalid(Plugin plugin, Object object) {
    return Fmt.format(SIPCREATOR_PLUGIN_INSTANCE_INVALID, plugin.getId(), pluginType(plugin), canonicalName(plugin),
        canonicalName(object));
  }

  public static <T> String pluginUnknown(String id, Class<T> type) {
    return Fmt.format(SIPCREATOR_PLUGIN_UNKNOWN, id, type == null ? "N/A" : type.getCanonicalName());
  }

  public static String pluginAmbigous(String id, List<Plugin> plugins) {
    return Fmt.format(SIPCREATOR_PLUGIN_AMBIGUOUS, id, plugins);
  }

  private static String canonicalName(Object o) {
    if (o == null) {
      return "[nullobject]";
    } else {
      return o.getClass()
        .getCanonicalName();
    }
  }

  private static String pluginType(Plugin p) {
    final Class<?> type = p.getType();
    if (type == null) {
      return "[nulltype]";
    } else {
      return type.getCanonicalName();
    }

  }
}
