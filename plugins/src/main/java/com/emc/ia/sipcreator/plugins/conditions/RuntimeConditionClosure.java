/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.conditions;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.plugins.steps.flow.RuntimeCondition;
import com.emc.ia.sipcreator.utils.Params;

import groovy.lang.Closure;

public class RuntimeConditionClosure implements RuntimeCondition {

  private final Closure<Boolean> closure;

  public RuntimeConditionClosure(Closure<Boolean> closure) {
    Params.notNull(closure, "RuntimeConditionClosure.closure");
    this.closure = closure;
  }

  @Override
  public boolean test(RuntimeState state) {
    Boolean result = closure.call(state);
    if (result == null) {
      // TODO: log this?
      return false;
    }
    return result;
  }

  @Override
  public String toString() {
    return "RuntimeConditionClosure [closure=" + closure + "]";
  }

}
