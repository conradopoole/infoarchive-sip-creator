/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.flow;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;

public class RepeatStep implements Step {

  private final Step continuation;
  private final RuntimeVariable times;
  private final String variableName;

  public RepeatStep(RuntimeVariable times, String variableName, Step continuation) {
    this.times = times;
    this.variableName = variableName;
    this.continuation = continuation;
  }

  @Override
  public void run(RuntimeState context) {
    int num = Integer.parseInt(times.getValue(context));
    for (int i = 1; i <= num; ++i) {
      context.set(variableName, i);
      continuation.run(context);
    }
  }

}
