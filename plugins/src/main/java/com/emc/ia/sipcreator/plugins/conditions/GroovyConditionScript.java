/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.conditions;

import groovy.lang.Closure;
import groovy.lang.Script;

public abstract class GroovyConditionScript extends Script {

  private Closure<Boolean> conditionClosure;

  public void test(Closure<Boolean> closure) {
    this.conditionClosure = closure;
  }

  public Closure<Boolean> getConditionClosure() {
    return conditionClosure;
  }

}
