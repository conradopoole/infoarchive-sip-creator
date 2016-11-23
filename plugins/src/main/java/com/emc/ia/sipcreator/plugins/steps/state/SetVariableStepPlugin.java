/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.state;

import java.util.HashMap;
import java.util.Map;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class SetVariableStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "set";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Sets the specified variables.")
    .param("<name>", RuntimeVariable.class, "Sets the variable <name> to the specified value.")
    .build();

  public SetVariableStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Step create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    Map<String, RuntimeVariable> template = new HashMap<String, RuntimeVariable>();
    configUtils.forEach(context.getConfig(),
        it -> template.put(it.getName(), configUtils.getRuntimeVariable(configUtils.getString(it))));
    // TODO: log
    return new SetVariableStep(template);
  }

}
