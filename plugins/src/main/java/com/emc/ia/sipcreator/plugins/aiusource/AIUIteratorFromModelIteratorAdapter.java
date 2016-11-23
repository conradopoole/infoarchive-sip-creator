/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.aiusource;

import java.io.IOException;
import java.util.Iterator;

import com.emc.ia.sipcreator.api.AIU;
import com.emc.ia.sipcreator.api.AIUCollection;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSerializer;

public class AIUIteratorFromModelIteratorAdapter implements AIUCollection {

  private final Iterator<Model> result;
  private final AIUFromModelAdapter adapter;

  public AIUIteratorFromModelIteratorAdapter(ModelSerializer converter, DigitalObjectsFromModel extractor,
      Iterator<Model> result) {
    this.adapter = new AIUFromModelAdapter(converter, extractor);
    this.result = result;
  }

  @Override
  public boolean hasNext() {
    return result.hasNext();
  }

  @Override
  public AIU next() {
    adapter.setValue(result.next());
    return adapter;
  }

  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub

  }
}
