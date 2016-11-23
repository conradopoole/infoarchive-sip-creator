/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import com.emc.ia.sipcreator.api.doc.PluginDocumentation;

public interface Plugin {

  String getId();

  Class<?> getType();

  Object create(PluginContext context);

  PluginDocumentation getDocumentation();
}
