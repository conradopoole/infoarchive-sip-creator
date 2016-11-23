/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.RuntimeVariableFactory;

public class ConfigUtilsImpl implements ConfigUtils {

  private final RuntimeVariableFactory runtimeVariableFactory;
  private final Function<String, String> loader;

  public ConfigUtilsImpl(RuntimeVariableFactory runtimeVariableFactory, Function<String, String> loader) {
    Params.notNull(runtimeVariableFactory, "ConfigUtilsImpl.runtimeVariableFactory");
    Params.notNull(loader, "ConfigUtilsImpl.loader");
    this.runtimeVariableFactory = runtimeVariableFactory;
    this.loader = loader;
  }

  @Override
  public void forEach(Config parent, Consumer<Config> proc) {
    if (parent != null) {
      for (Config config : parent.getChildGroups()) {
        proc.accept(config);
      }
    }
  }

  @Override
  public Config getGroup(PluginContext context, String id) {
    return getGroup(context.getConfig(), id);

  }

  @Override
  public Config getOptionalGroup(Config parent, String id) {
    for (Config config : parent.getChildGroups()) {
      if (id.equals(config.getName())) {
        return config;
      }
    }
    return null;
  }

  @Override
  public Config getGroup(Config parent, String id) {
    Config config = getOptionalGroup(parent, id);
    if (config == null) {
      throw new IllegalArgumentException("No such group " + id);
    } else {
      return config;
    }
  }

  @Override
  public <T> T newObject(String id, PluginContext context, String groupId, Class<T> type) {
    Config conf = getGroup(context.getConfig(), groupId);
    return newObject(id, context, conf, type);
  }

  @Override
  public <T> T newObject(String id, PluginContext context, Config config, Class<T> type) {
    return context.newObject(type, id, config);
  }

  @Override
  public String getString(PluginContext context, String id) {
    return getString(context.getConfig(), id);
  }

  @Override
  public RuntimeVariable getRuntimeVariable(PluginContext context, String id) {
    String templateText = getString(context, id);
    return getRuntimeVariable(templateText);
  }

  @Override
  public RuntimeVariable getRuntimeVariable(String templateText) {
    return runtimeVariableFactory.create(templateText);
  }

  private String getString(Config config, String id) {
    if (config.isCompound()) {
      return tryCoerce(config.getValue(id), String.class);
    } else {
      return null;
    }
  }

  private <T> T tryCoerce(Object value, Class<T> type) {
    try {
      return type.cast(value);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Expected value of type " + type + " but got " + value, e);
    }
  }

  @Override
  public RuntimeVariable getRuntimeVariable(Config config) {
    String template = getString(config);
    return getRuntimeVariable(template);
  }

  @Override
  public String getString(Config config) {
    return tryCoerce(config.getValue(), String.class);
  }

  @Override
  public boolean getBoolean(PluginContext context, String id, boolean defaultValue) {
    String value = StringUtils.defaultIfBlank(getString(context, id), String.valueOf(defaultValue));
    if ("true".equals(value)) {
      return true;
    } else if ("false".equals(value)) {
      return false;
    } else {
      throw new IllegalArgumentException("The value " + value + " is not a boolean.");
    }
  }

  @Override
  public List<RuntimeVariable> getRuntimeVariables(PluginContext context, String id) {
    Config config = context.getConfig();
    Object value = config.getValue(id);
    if (value instanceof String) {
      List<RuntimeVariable> result = new ArrayList<>(1);
      result.add(getRuntimeVariable((String)value));
      return result;
    } else if (value instanceof List) {
      List<RuntimeVariable> result = new ArrayList<>(1);
      for (Object entry : (List)value) {
        // TODO: check type
        result.add(getRuntimeVariable((String)entry));
      }
      return result;
    } else {
      throw new IllegalArgumentException("Expected string or list of string but got " + value);
    }
  }

  @Override
  public Config getSingleChildGroup(Config config) {
    // TODO: if no or more than 1
    return config.getChildGroups()
      .get(0);
  }

  @Override
  public String getExternalResource(PluginContext context, String id) throws IOException {
    String path = getString(context, id);
    return getExternalResource(path);
  }

  @Override
  public String getExternalResource(String path) throws IOException {
    return loader.apply(path);
  }

  @Override
  public <T> T newObject(PluginContext context, String groupId, Class<T> type) {
    Config conf = getSingleChildGroup(getGroup(context, groupId));
    return newObject(conf.getName(), context, conf, type);
  }

  @Override
  public <T> Optional<T> newOptionalObject(PluginContext context, String groupId, Class<T> type) {
    Config group = getOptionalGroup(context.getConfig(), groupId);
    if (group == null) {
      return Optional.empty();
    } else {
      Config conf = getSingleChildGroup(group);
      return Optional.of(newObject(conf.getName(), context, conf, type));
    }
  }

  @Override
  public <T> T newObject(PluginContext context, Predicate<Config> groupSelector, Class<T> type) {
    Optional<Config> config = context.getConfig()
      .getChildGroups()
      .stream()
      .filter(groupSelector)
      .findFirst();
    if (config.isPresent()) {
      Config conf = config.get();
      return newObject(conf.getName(), context, conf, type);
    } else {
      throw new IllegalArgumentException("Missing config...");
    }
  }

  @Override
  public Predicate<Config> groupSelectorFirstNot(String... args) {
    Set<String> names = new HashSet<String>(Arrays.asList(args));
    return new ConfigNameIsNotOneOf(names);
  }

  @Override
  public RuntimeVariable getOptionalRuntimeVariable(PluginContext context, String id, String defaultValue) {
    return getRuntimeVariable(StringUtils.defaultString(getString(context, id), defaultValue));
  }

  @Override
  public String toString() {
    return "ConfigUtilsImpl [runtimeVariableFactory=" + runtimeVariableFactory + ", loader=" + loader + "]";
  }

  @Override
  public String getOptionalString(PluginContext context, String id, String defaultValue) {
    String value = getString(context, id);
    if (value == null) {
      return defaultValue;
    } else {
      return value;
    }
  }

}
