/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.testing.plugins;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class ModelSourceFromStatePlugin extends AbstractPluginImpl<ModelSource> {

  private static final String NAME = "fromState";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(ModelSource.class)
    .description("A static set of models. For testing.")
    .param("variable", String.class, "The name of the variable.", "models")
    .build();

  public ModelSourceFromStatePlugin() {
    super(NAME, ModelSource.class, DOCUMENTATION);
  }

  @Override
  public ModelSource create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    String variable = configUtils.getOptionalString(context, "variable", "models");
    return new ModelSourceFromState(variable);
  }
}
