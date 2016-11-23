/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.flow;

import java.util.ArrayList;
import java.util.List;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class SequenceStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "sequence";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("A sequence of steps to perform.")
    .build();

  public SequenceStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Step create(PluginContext context) {
    List<Step> steps = new ArrayList<Step>();
    context.configUtils()
      .forEach(context.getConfig(), it -> steps.add(context.newObject(Step.class, it.getName(), it)));
    return new SequentialStep(steps);
  }

}
