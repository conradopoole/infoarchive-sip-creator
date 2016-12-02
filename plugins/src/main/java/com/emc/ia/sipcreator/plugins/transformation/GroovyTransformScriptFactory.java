/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.transformation;

import java.util.function.Function;

import org.codehaus.groovy.control.CompilerConfiguration;

import com.emc.ia.sipcreator.api.Model;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;

public class GroovyTransformScriptFactory {

  public Function<Model, Model> build(String text) {

    CompilerConfiguration config = new CompilerConfiguration();
    config.setScriptBaseClass(GroovyTransformScript.class.getCanonicalName());
    GroovyShell shell = new GroovyShell(getClass().getClassLoader(), new Binding(), config);
    GroovyTransformScript transform = (GroovyTransformScript)shell.parse(text);
    transform.run();
    Closure<Void> closure = transform.getTransformationClosure();

    if (closure == null) {
      throw new IllegalArgumentException(
          "The following script does not include the required transform {} configuration block. \n" + text);
    }
    return new FunctionWithTransform(closure);

  }
}
