/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class XDBQueryStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "xdb.query";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Runs the specified xQuery.")
    .param("library", RuntimeVariable.class, "The optional name of the xDB library to create.")
    .param("xdb", String.class, "The name of the xDB session provider to use.", "xdb.default")
    .param("query", RuntimeVariable.class, "The xQuery.")
    .param("processor", XDBQueryResultProcessor.class, "The optional query result processor.", "traverse")
    .build();

  public XDBQueryStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    RuntimeVariable libraryVariable = configUtils.getOptionalRuntimeVariable(context, "library", "");
    RuntimeVariable queryVariable = configUtils.getRuntimeVariable(context, "query");
    XDBSessionProvider sessionProvider = context.getObject(XDBSessionProvider.class,
        StringUtils.defaultString(configUtils.getString(context, "xdb"), "xdb.default"));

    XDBQueryResultProcessor processor = createProcessor(configUtils, context);
    return new XDBQueryStep(queryVariable, libraryVariable, sessionProvider, processor);
  }

  private XDBQueryResultProcessor createProcessor(ConfigUtils configUtils, PluginContext context) {
    Config group = configUtils.getOptionalGroup(context.getConfig(), "processor");
    if (group == null) {
      return context.newObject(XDBQueryResultProcessor.class, "traverse", null);
    } else {
      return configUtils.newObject(context, "processor", XDBQueryResultProcessor.class);
    }
  }
}
