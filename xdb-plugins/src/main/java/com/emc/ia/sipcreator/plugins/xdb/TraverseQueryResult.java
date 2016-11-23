/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.xhive.query.interfaces.XhiveXQueryResultIf;

public class TraverseQueryResult implements XDBQueryResultProcessor {

  @Override
  public void process(RuntimeState state, XhiveXQueryResultIf result) {
    while (result.hasNext()) {
      result.next();
    }
  }

}
