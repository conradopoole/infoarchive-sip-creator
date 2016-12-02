/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.flow;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.Step;

public class WhenStep implements Step {

  private final Step continuation;
  private final RuntimeCondition condition;

  public WhenStep(RuntimeCondition condition, Step continuation) {
    this.condition = condition;
    this.continuation = continuation;
  }

  @Override
  public void run(RuntimeState context) {

    // Not thread safe!

    if (condition.test(context)) {
      continuation.run(context);
    }

  }

}
