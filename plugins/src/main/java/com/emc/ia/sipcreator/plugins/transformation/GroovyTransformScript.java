/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.transformation;

import groovy.lang.Closure;
import groovy.lang.Script;

public abstract class GroovyTransformScript extends Script {

  private Closure<Void> transformationClosure;

  public void transform(Closure<Void> closure) {
    this.transformationClosure = closure;
  }

  public Closure<Void> getTransformationClosure() {
    return transformationClosure;
  }

}
