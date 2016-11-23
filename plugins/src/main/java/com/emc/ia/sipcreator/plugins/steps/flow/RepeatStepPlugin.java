/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.flow;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class RepeatStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "repeat";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Repeates a sequence of steps a specified number of times.")
    .param("times", RuntimeVariable.class,
        "The runtime variable representing the number of times the contained steps should be repeated.")
    .param("variable", String.class,
        "The variable that should hold the value indicating which repetition of the contained steps are currently being executed.",
        "i")
    .param("steps", Step.class, "The steps that should be repeated.")
    .build();

  public RepeatStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Step create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    Config conf = context.getConfig();
    RuntimeVariable times = configUtils.getRuntimeVariable(context, "times");
    String variableName = StringUtils.defaultString((String)conf.getValue("variable"), "i");
    Step continuation = context.newObject(Step.class, "sequence", configUtils.getGroup(conf, "steps"));
    return new RepeatStep(times, variableName, continuation);
  }

}
