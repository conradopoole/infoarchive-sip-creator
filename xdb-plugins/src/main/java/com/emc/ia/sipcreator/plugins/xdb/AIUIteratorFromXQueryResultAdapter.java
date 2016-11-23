/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.emc.ia.sipcreator.api.AIU;
import com.emc.ia.sipcreator.api.AIUCollection;
import com.xhive.query.interfaces.XhiveXQueryResultIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;

public class AIUIteratorFromXQueryResultAdapter implements AIUCollection {

  private final XhiveXQueryResultIf result;
  private final AIUFromXQueryValueAdapter adapter = new AIUFromXQueryValueAdapter();
  private final Closeable dependentResource;

  public AIUIteratorFromXQueryResultAdapter(XhiveXQueryResultIf result, Closeable dependentResource) {
    this.result = result;
    this.dependentResource = dependentResource;
  }

  @Override
  public boolean hasNext() {
    return result.hasNext();
  }

  @Override
  public AIU next() {
    XhiveXQueryValueIf value = result.next();
    adapter.setValue(value);
    return adapter;
  }

  @Override
  public void close() throws IOException {
    result.close();
    IOUtils.closeQuietly(dependentResource);
  }

}
