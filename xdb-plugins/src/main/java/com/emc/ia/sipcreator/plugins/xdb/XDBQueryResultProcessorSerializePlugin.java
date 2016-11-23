/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class XDBQueryResultProcessorSerializePlugin extends AbstractPluginImpl<XDBQueryResultProcessor> {

  private static final String NAME = "serialize";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(XDBQueryResultProcessor.class)
    .description("Serializes the xQuery result set to a file.")
    .param("header", RuntimeVariable.class, "The file header.", "")
    .param("footer", RuntimeVariable.class, "The file footer.", "")
    .param("path", RuntimeVariable.class, "The path to the file to write.", "")
    .param("append", RuntimeVariable.class, "Whether to append (true) or replace (false) the file.", "false")
    .build();

  public XDBQueryResultProcessorSerializePlugin() {
    super(NAME, XDBQueryResultProcessor.class, DOCUMENTATION);
  }

  @Override
  public XDBQueryResultProcessor create(PluginContext context) {
    // TODO: add segmentation support.
    ConfigUtils configUtils = context.configUtils();
    RuntimeVariable headerVariable = configUtils.getOptionalRuntimeVariable(context, "header", "");
    RuntimeVariable footerVariable = configUtils.getOptionalRuntimeVariable(context, "footer", "");
    RuntimeVariable pathVariable = configUtils.getRuntimeVariable(context, "path");
    boolean append = configUtils.getBoolean(context, "append", false);
    return new SerializeQueryResult(headerVariable, footerVariable, pathVariable, append);
  }

}
