/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.impl;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.utils.Params;

public class SimpleExecutionPlan implements ExecutionPlan {

  private final Step step;

  public SimpleExecutionPlan(Step step) {
    Params.notNull(step, "SimpleExecutionPlan.step");
    this.step = step;
  }

  @Override
  public void run() {
    RuntimeState context = new RuntimeState();
    step.run(context);
  }

  @Override
  public String toString() {
    return "SimpleExecutionPlan [step=" + step + "]";
  }

}
