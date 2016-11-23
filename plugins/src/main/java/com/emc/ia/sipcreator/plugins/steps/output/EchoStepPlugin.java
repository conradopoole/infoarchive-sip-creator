/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.output;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class EchoStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "echo";
  private static PluginDocumentation documentation = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Outputs the specified message.")
    .param(".", RuntimeVariable.class, "The message to output.")
    .build();

  public EchoStepPlugin() {
    super(NAME, documentation);
  }

  @Override
  public Step create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    RuntimeVariable variable = configUtils.getRuntimeVariable(context.getConfig());
    return new EchoStep(variable);
  }

  @Override
  public String toString() {
    return "EchoStepPlugin [getId()=" + getId() + ", getType()=" + getType() + ", getDocumentation()="
        + getDocumentation() + "]";
  }

}
