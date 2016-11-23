/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.conditions;

import java.util.function.Predicate;

import org.codehaus.groovy.control.CompilerConfiguration;

import com.emc.ia.sipcreator.api.RuntimeState;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyCondition implements Predicate<RuntimeState> {

  private final GroovyConditionScript script;

  public GroovyCondition(String scriptText) {
    CompilerConfiguration config = new CompilerConfiguration();
    config.setScriptBaseClass(GroovyConditionScript.class.getCanonicalName());
    GroovyShell shell = new GroovyShell(this.getClass()
      .getClassLoader(), new Binding(), config);
    script = (GroovyConditionScript)shell.parse(scriptText);

    // Run the script to allow for initialization;
    // script.run();
  }

  @Override
  public boolean test(RuntimeState context) {
    return script.eval(context);
  }

}
