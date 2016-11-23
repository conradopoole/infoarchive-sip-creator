/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.impl;

import org.stringtemplate.v4.ST;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.utils.Params;

public class RuntimeVariableImpl implements RuntimeVariable {

  private final ST template;

  public RuntimeVariableImpl(ST template) {
    Params.notNull(template, "RuntimeVariableImpl.template");
    this.template = template;
  }

  @Override
  public String getValue(RuntimeState context) {
    ST st = new ST(template);
    st.add("context", context);
    return st.render();
  }

  @Override
  public String toString() {
    return "RuntimeVariableImpl [template=" + template.impl.getTemplateSource() + "]";
  }

}
