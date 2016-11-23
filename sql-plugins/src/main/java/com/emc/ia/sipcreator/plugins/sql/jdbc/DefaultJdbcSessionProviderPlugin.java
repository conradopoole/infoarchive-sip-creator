/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.sql.jdbc;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class DefaultJdbcSessionProviderPlugin extends AbstractPluginImpl<JdbcSessionProvider> {

  private static final String NAME = "jdbc";

  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(JdbcSessionProvider.class)
    .description("Establishes a JDBC connection")

    .param("driver", String.class, "The JDBC driver to use.")
    .param("url", String.class, "The connection string.")
    .param("username", String.class, "The username.")
    .param("password", String.class, "The password")
    .build();

  public DefaultJdbcSessionProviderPlugin() {
    super(NAME, JdbcSessionProvider.class, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    String driver = configUtils.getOptionalString(context, "driver", "");
    String url = configUtils.getString(context, "url");
    String username = configUtils.getOptionalString(context, "username", null);
    String password = configUtils.getOptionalString(context, "password", null);
    return new DefaultJdbcSessionProvider(driver, url, password, username);
  }

}
