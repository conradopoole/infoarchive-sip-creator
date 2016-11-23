/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.conditions;

import com.emc.ia.sipcreator.api.RuntimeState;

import groovy.lang.Binding;
import groovy.lang.Script;

public abstract class GroovyConditionScript extends Script {

  public boolean eval(RuntimeState context) {
    Binding binding = new Binding();
    binding.setVariable("context", context);
    setBinding(binding);
    return (Boolean)run();
  }
}
