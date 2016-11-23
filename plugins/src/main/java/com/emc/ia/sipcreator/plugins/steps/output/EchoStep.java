/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.utils.Params;

public class EchoStep implements Step {

  private final RuntimeVariable variable;
  private static final Logger LOG = LoggerFactory.getLogger(EchoStep.class);

  public EchoStep(RuntimeVariable variable) {
    Params.notNull(variable, "EchoStep.variable");
    this.variable = variable;
  }

  @Override
  public void run(RuntimeState state) {
    String msg = variable.getValue(state);
    LOG.warn(msg);
    System.out.println(msg); // NOPMD
  }

  @Override
  public String toString() {
    return "EchoStep [variable=" + variable + "]";
  }

}
