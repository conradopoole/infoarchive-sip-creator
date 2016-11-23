/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.AIUSource;
import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class AiuFromXQueryPlugin extends AbstractPluginImpl<AIUSource> {

  private static final String NAME = "aiuFromXQuery";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(AIUSource.class)
    .description("Loads the specified files into the specified library in xDB.")
    .param("xdb", String.class, "The name of the xDB session provider to use.", "xdb.default")
    // .param("library", RuntimeVariable.class, "The name of the xDB library to create.")
    .param("query", RuntimeVariable.class, "The query which selects the AIUs.")
    .build();

  public AiuFromXQueryPlugin() {
    super(NAME, AIUSource.class, DOCUMENTATION);
  }

  @Override
  public AIUSource create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    // TODO: add support for content!
    // TODO: add support for optional context library!
    RuntimeVariable xqueryTextVariable = configUtils.getRuntimeVariable(context, "query");
    XDBSessionProvider sessionProvider = context.getObject(XDBSessionProvider.class,
        StringUtils.defaultString(configUtils.getString(context, "xdb"), "xdb.default"));
    XQueryResultSupplier xqueryResult = new XQueryResultSupplier(sessionProvider, xqueryTextVariable);

    return new XMLAIUSource(xqueryResult);
  }

}
