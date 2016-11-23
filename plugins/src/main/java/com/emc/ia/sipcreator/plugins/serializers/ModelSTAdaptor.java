/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.serializers;

import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import com.emc.ia.sipcreator.api.Model;

public class ModelSTAdaptor implements ModelAdaptor {

  @Override
  public Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName)
      throws STNoSuchPropertyException {

    return ((Model)o).get(propertyName);
  }

}
