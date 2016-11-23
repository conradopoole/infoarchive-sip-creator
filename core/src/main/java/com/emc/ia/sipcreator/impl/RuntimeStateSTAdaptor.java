/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.impl;

import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import com.emc.ia.sipcreator.api.RuntimeState;

public class RuntimeStateSTAdaptor implements ModelAdaptor {

  @Override
  public Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName)
      throws STNoSuchPropertyException {

    return ((RuntimeState)o).get(propertyName);
  }

}
