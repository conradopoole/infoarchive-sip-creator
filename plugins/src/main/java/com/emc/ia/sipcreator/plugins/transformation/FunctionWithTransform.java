/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.transformation;

import java.util.function.Function;

import com.emc.ia.sipcreator.api.Model;

import groovy.lang.Closure;

public class FunctionWithTransform implements Function<Model, Model> {

  private final Closure<Void> closure;

  public FunctionWithTransform(Closure<Void> proc) {
    this.closure = proc;
  }

  @Override
  public Model apply(Model model) {
    closure.call(model);
    return model;
  }

}
