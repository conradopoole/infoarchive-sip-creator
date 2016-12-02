/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.transformation;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ModelTransformation;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class GroovyModelTransformationPlugin extends AbstractPluginImpl<ModelTransformation> {

  private static final String NAME = "groovy";
  private static final String PARAM_SCRIPT = "script";

  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(ModelTransformation.class)
    .description("A model transform that uses groovy to transform the model.")
    .param(PARAM_SCRIPT, RuntimeVariable.class, "The transformation script.")
    .build();

  public GroovyModelTransformationPlugin() {
    super(NAME, ModelTransformation.class, DOCUMENTATION);
  }

  @Override
  public ModelTransformation create(PluginContext context) {
    RuntimeVariable scriptVariable = context.configUtils()
      .getRuntimeVariable(context, PARAM_SCRIPT);
    return new GroovyModelTransformation(scriptVariable);
  }

}
