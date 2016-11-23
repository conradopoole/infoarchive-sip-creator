/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class XDBQueryResultProcessorTraversePlugin extends AbstractPluginImpl<XDBQueryResultProcessor> {

  private static final String NAME = "traverse";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(XDBQueryResultProcessor.class)
    .description("Traverses the xQuery result set.")
    .build();

  public XDBQueryResultProcessorTraversePlugin() {
    super(NAME, XDBQueryResultProcessor.class, DOCUMENTATION);
  }

  @Override
  public XDBQueryResultProcessor create(PluginContext context) {
    return new TraverseQueryResult();
  }

  @Override
  public String toString() {
    return "XDBQueryResultProcessorTraversePlugin []";
  }

}
