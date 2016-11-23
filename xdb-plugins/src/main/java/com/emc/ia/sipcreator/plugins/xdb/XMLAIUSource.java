/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import com.emc.ia.sipcreator.api.AIUCollection;
import com.emc.ia.sipcreator.api.AIUSource;
import com.emc.ia.sipcreator.api.RuntimeState;

public class XMLAIUSource implements AIUSource {

  private final XQueryResultSupplier resultSupplier;

  public XMLAIUSource(XQueryResultSupplier resultSupplier) {
    this.resultSupplier = resultSupplier;
  }

  @Override
  public AIUCollection getAIUs(RuntimeState state) {
    final XQueryResult result = resultSupplier.apply(state);
    return new AIUIteratorFromXQueryResultAdapter(result.getResult(), result);
  }

}
