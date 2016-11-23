/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.testing.plugins;

import java.util.List;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.DelegatingCloseableIterator;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.RuntimeState;

public class ModelSourceFromState implements ModelSource {

  private final String variable;

  public ModelSourceFromState(String variable) {
    this.variable = variable;
  }

  @Override
  public CloseableIterator<Model> getModels(RuntimeState state) {
    return new DelegatingCloseableIterator<Model>(((List<Model>)state.get(variable)).iterator());
  }

  @Override
  public String toString() {
    return "ModelSourceFromState [variable=" + variable + "]";
  }

  @Override
  public void close() {

  }

}
