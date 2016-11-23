/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class XDBCreateLibraryStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "xdb.library";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Creates a library, if it doesn't already exist, in the specified xDB instance.")
    .param("library", RuntimeVariable.class, "The name of the xDB library to create.")
    .param("xdb", String.class, "The name of the xDB session provider to use.", "xdb.default")
    .build();

  public XDBCreateLibraryStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    RuntimeVariable libraryPathVariable = configUtils.getRuntimeVariable(context, "library");
    XDBSessionProvider sessionProvider = context.getObject(XDBSessionProvider.class,
        StringUtils.defaultString(configUtils.getString(context, "xdb"), "xdb.default"));
    return new XDBCreateLibraryStep(sessionProvider, libraryPathVariable);
  }

}
