/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.flow;

import java.util.function.Predicate;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;
import com.emc.ia.sipcreator.plugins.conditions.GroovyCondition;

public class WhenStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "when";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Performs a set of steps only if a condition is fulfilled.")
    .param("test", String.class, "The condition expression")
    .param("steps", Step.class, "The steps that should be repeated.")
    .build();

  public WhenStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Step create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    String expr = configUtils.getString(context, "test");
    Predicate<RuntimeState> condition = new GroovyCondition(expr);
    Step continuation = configUtils.newObject("sequence", context, "steps", Step.class);
    return new WhenStep(condition, continuation);
  }

}
