/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.flow;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class NopStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "nop";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Does nothing.")
    .build();

  public NopStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    return new NopStep();

  }

}
