/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.conditions;

import org.codehaus.groovy.control.CompilerConfiguration;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;
import com.emc.ia.sipcreator.plugins.steps.flow.RuntimeCondition;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;

public class GroovyConditionPlugin extends AbstractPluginImpl<RuntimeCondition> {

  private static final String NAME = "groovy";
  private static final String PARAM_SCRIPT = "script";

  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(RuntimeCondition.class)
    .description("A model transform that uses groovy to transform the model.")
    .param(PARAM_SCRIPT, String.class, "The test script.")
    .build();

  public GroovyConditionPlugin() {
    super(NAME, RuntimeCondition.class, DOCUMENTATION);
  }

  @Override
  public RuntimeCondition create(PluginContext context) {
    String text = String.valueOf(context.getConfig()
      .getValue());

    CompilerConfiguration config = new CompilerConfiguration();
    config.setScriptBaseClass(GroovyConditionScript.class.getCanonicalName());
    GroovyShell shell = new GroovyShell(getClass().getClassLoader(), new Binding(), config);
    GroovyConditionScript script = (GroovyConditionScript)shell.parse(text);
    script.run();
    Closure<Boolean> closure = script.getConditionClosure();

    if (closure == null) {
      throw new IllegalArgumentException(
          "The following script does not include the required test {} configuration block. \n" + text);
    }

    return new RuntimeConditionClosure(closure);
  }

}
