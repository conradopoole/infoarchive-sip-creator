/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.state;

import java.util.Map;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.utils.Params;

public class SetVariableStep implements Step {

  private final Map<String, RuntimeVariable> template;

  public SetVariableStep(Map<String, RuntimeVariable> template) {
    Params.notNull(template, "SetVariableStep.template");
    this.template = template;
  }

  @Override
  public void run(RuntimeState state) {
    for (Map.Entry<String, RuntimeVariable> variable : template.entrySet()) {
      state.set(variable.getKey(), variable.getValue()
        .getValue(state));
    }
  }

  @Override
  public String toString() {
    return "SetVariableStep [template=" + template + "]";
  }

}
