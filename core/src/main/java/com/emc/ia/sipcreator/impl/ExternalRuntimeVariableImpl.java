/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.impl;

import java.util.function.Function;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.utils.Params;

public class ExternalRuntimeVariableImpl implements RuntimeVariable {

  private final RuntimeVariable fileVariable;
  private final Function<String, String> loader;

  public ExternalRuntimeVariableImpl(Function<String, String> loader, RuntimeVariable fileVariable) {
    Params.notNull(loader, "ExternalRuntimeVariableImpl.loader");
    Params.notNull(fileVariable, "ExternalRuntimeVariableImpl.fileVariable");
    this.loader = loader;
    this.fileVariable = fileVariable;
  }

  @Override
  public String getValue(RuntimeState state) {
    String path = fileVariable.getValue(state);
    return loader.apply(path);
  }

  @Override
  public String toString() {
    return "ExternalRuntimeVariableImpl [fileVariable=" + fileVariable + ", loader=" + loader + "]";
  }

}
