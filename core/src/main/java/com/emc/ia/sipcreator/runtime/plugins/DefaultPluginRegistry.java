/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.runtime.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.logging.Messages;
import com.emc.ia.sipcreator.logging.SipCreatorInitLog;
import com.emc.ia.sipcreator.spi.PluginRegistry;

public class DefaultPluginRegistry implements PluginRegistry {

  private final Map<PluginRegistryKey, Plugin> plugins;
  private final Map<String, List<Plugin>> pluginsById;

  public DefaultPluginRegistry() {
    plugins = load();

    pluginsById = new HashMap<>();
    for (Entry<PluginRegistryKey, Plugin> entry : plugins.entrySet()) {
      String id = entry.getKey()
        .getId();
      List<Plugin> list = pluginsById.get(id);
      if (list == null) {
        list = new ArrayList<>();
        pluginsById.put(id, list);
      }
      list.add(entry.getValue());
    }
  }

  @Override
  public List<Plugin> getAllPlugins() {
    return new ArrayList<Plugin>(plugins.values());
  }

  @Override
  public <T> T create(Class<T> type, String id, PluginContext context) {
    final Plugin plugin = getPlugin(type, id);

    if (plugin == null) {
      SipCreatorInitLog.pluginUnknown(id, type);
      throw new IllegalArgumentException(Messages.pluginUnknown(id, type));
    }

    final Object object = create(plugin, context);

    if (object == null) {
      SipCreatorInitLog.pluginInstanceIsNull(plugin);
      throw new IllegalArgumentException(Messages.pluginInstanceIsNull(plugin));
    }

    if (type != null && !type.isAssignableFrom(object.getClass())) {
      SipCreatorInitLog.pluginInstanceTypeInvalid(plugin, object);
      throw new IllegalArgumentException(Messages.pluginInstanceTypeInvalid(plugin, object));
    }

    SipCreatorInitLog.pluginCreated(plugin, object);

    @SuppressWarnings("unchecked")
    final T result = (T)object;

    return result;

  }

  private <T> Plugin getPlugin(Class<T> type, String id) {
    if (type == null) {
      final List<Plugin> list = pluginsById.get(id);
      if (list == null || list.isEmpty()) {
        return null;
      } else if (list.size() == 1) {
        return list.get(0);
      } else {
        SipCreatorInitLog.pluginAmbigous(id, list);
        throw new IllegalArgumentException(Messages.pluginAmbigous(id, list));
      }
    } else {
      final PluginRegistryKey key = new PluginRegistryKey(type, id);
      return plugins.get(key);
    }
  }

  private Object create(Plugin plugin, PluginContext context) {
    try {
      return plugin.create(context);
    } catch (final Exception e) {
      SipCreatorInitLog.pluginCreationFailed(plugin, e);
      throw new IllegalArgumentException(Messages.pluginCreationFailed(plugin, e), e);

    }
  }

  private Map<PluginRegistryKey, Plugin> load() {
    final Map<PluginRegistryKey, Plugin> loadedPlugins = new HashMap<PluginRegistryKey, Plugin>();

    final ServiceLoader<Plugin> pluginLoader = ServiceLoader.load(Plugin.class);
    for (final Plugin plugin : pluginLoader) {
      final String id = plugin.getId();

      if (!isIdValid(id)) {
        SipCreatorInitLog.pluginIdInvalid(plugin);
        throw new IllegalArgumentException(Messages.pluginIdInvalid(plugin));
      }

      final Class<?> type = plugin.getType();

      if (!isTypeValid(type)) {
        SipCreatorInitLog.pluginTypeInvalid(plugin);
        throw new IllegalArgumentException(Messages.pluginTypeInvalid(plugin));
      }

      final PluginRegistryKey key = new PluginRegistryKey(type, id);

      if (loadedPlugins.containsKey(key)) {
        SipCreatorInitLog.pluginNotUnique(plugin, loadedPlugins.get(key));
        throw new IllegalArgumentException(Messages.pluginNotUnique(plugin, loadedPlugins.get(key)));
      }
      loadedPlugins.put(key, plugin);

      SipCreatorInitLog.loadedPlugin(plugin);

    }
    return loadedPlugins;
  }

  private boolean isTypeValid(Class<?> type) {
    return type != null;
  }

  private boolean isIdValid(String id) {
    return StringUtils.isNotBlank(id);
  }

  @Override
  public List<Plugin> getPluginsById(String id) {
    List<Plugin> list = pluginsById.get(id);
    if (list == null) {
      return Collections.emptyList();
    } else {
      return list;
    }
  }

  @Override
  public String toString() {
    return "PluginRegistry [plugins=" + plugins + "]";
  }

}
