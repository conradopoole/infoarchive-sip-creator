/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

public interface PluginContext {

  Config getConfig();

  Config getRootConfig();

  <T> T newObject(Class<T> type, String id, Config cfg);

  <T> T getObject(Class<T> type, String id);

  ConfigUtils configUtils();

}
