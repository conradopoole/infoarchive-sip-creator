/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.flow;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.Step;

public class NopStep implements Step {

  @Override
  public void run(RuntimeState context) {
  }

  @Override
  public String toString() {
    return "NopStep []";
  }

}
