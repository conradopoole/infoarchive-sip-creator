/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.flow;

import java.util.List;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.Step;

public class SequentialStep implements Step {

  private final List<Step> steps;

  public SequentialStep(final List<Step> steps) {
    this.steps = steps;
  }

  @Override
  public void run(RuntimeState context) {
    for (Step step : steps) {
      step.run(context);
    }
  }

}
