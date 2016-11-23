/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import java.io.Closeable;
import java.io.IOException;

import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.query.interfaces.XhiveXQueryResultIf;

public class XQueryResult implements Closeable {

  private final XhiveXQueryResultIf result;
  private final XDBSessionProvider sessionProvider;
  private final XhiveSessionIf session;

  public XQueryResult(XhiveXQueryResultIf result, XDBSessionProvider sessionProvider, XhiveSessionIf session) {
    this.result = result;
    this.sessionProvider = sessionProvider;
    this.session = session;
  }

  @Override
  public void close() throws IOException {
    sessionProvider.releaseSession(session);
  }

  public XhiveXQueryResultIf getResult() {
    return result;
  }

}
