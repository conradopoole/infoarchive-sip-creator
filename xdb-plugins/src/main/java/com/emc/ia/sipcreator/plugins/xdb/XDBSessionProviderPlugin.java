/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.HouseKeeper;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class XDBSessionProviderPlugin extends AbstractPluginImpl<XDBSessionProvider> {

  private static final String NAME = "xdb";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(XDBSessionProvider.class)
    .description("Default session provider implementation for xDB.")
    .param("db", String.class, "The name of the xDB database.")
    .param("user", String.class, "The name the user to connect as.")
    .param("password", String.class, "The password of the user to connect as.")
    .param("url", String.class, "The xDB bootstrap URL.")
    .build();

  public XDBSessionProviderPlugin() {
    super(NAME, XDBSessionProvider.class, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    String database = configUtils.getString(context, "db");
    String user = configUtils.getString(context, "user");
    String password = configUtils.getString(context, "password");
    String url = configUtils.getString(context, "url");
    XDBSessionProviderImpl impl = new XDBSessionProviderImpl(url, database, user, password);
    context.getObject(HouseKeeper.class, "housekeeper")
      .add(impl);
    return impl;
  }

}
