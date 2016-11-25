/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ConfigUtils {

  void forEach(Config parent, Consumer<Config> proc);

  boolean getBoolean(PluginContext context, String id, boolean defaultValue);

  String getExternalResource(PluginContext context, String id) throws IOException;

  Config getGroup(Config parent, String id);

  Config getGroup(PluginContext context, String id);

  Config getOptionalGroup(Config parent, String id);

  RuntimeVariable getOptionalRuntimeVariable(PluginContext context, String id, String defaultValue);

  RuntimeVariable getRuntimeVariable(Config config);

  RuntimeVariable getRuntimeVariable(PluginContext context, String id);

  RuntimeVariable getRuntimeVariable(String templateText);

  List<RuntimeVariable> getRuntimeVariables(PluginContext context, String id);

  Config getSingleChildGroup(Config config);

  String getString(Config config);

  String getString(PluginContext context, String id);

  Predicate<Config> groupSelectorFirstNot(String... args);

  <T> T newObject(PluginContext context, Predicate<Config> groupSelector, Class<T> type);

  <T> T newObject(PluginContext context, String groupId, Class<T> type);

  <T> T newObject(String id, PluginContext context, Config config, Class<T> type);

  <T> T newObject(String id, PluginContext context, String groupId, Class<T> type);

  <T> Optional<T> newOptionalObject(PluginContext context, String groupId, Class<T> type);

  String getOptionalString(PluginContext context, String id, String defaultValue);

  String getExternalResource(String resourcePath) throws IOException;

  long getOptionalLong(PluginContext context, String param, long l);
}
