/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.transformation;

import java.util.function.Function;

import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelTransformation;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.utils.Params;

public class GroovyModelTransformation implements ModelTransformation {

  private final RuntimeVariable scriptVariable;
  private final GroovyTransformScriptFactory factory;

  public GroovyModelTransformation(RuntimeVariable scriptVariable) {
    Params.notNull(scriptVariable, "GroovyModelTransformation.scriptVariable");
    this.scriptVariable = scriptVariable;
    factory = new GroovyTransformScriptFactory();
  }

  @Override
  public Function<Model, Model> getTransform(RuntimeState state) {
    String script = scriptVariable.getValue(state);
    return factory.build(script);
  }

}
