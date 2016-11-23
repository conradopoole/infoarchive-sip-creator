/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import java.util.function.Function;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.query.interfaces.XhiveXQueryQueryIf;

public class XQueryResultSupplier implements Function<RuntimeState, XQueryResult> {

  private final RuntimeVariable xqueryTextVariable;
  private final XDBSessionProvider sessionProvider;

  public XQueryResultSupplier(XDBSessionProvider sessionProvider, RuntimeVariable xqueryTextVariable) {
    this.sessionProvider = sessionProvider;
    this.xqueryTextVariable = xqueryTextVariable;
  }

  @Override
  public XQueryResult apply(RuntimeState state) {
    String xqueryText = xqueryTextVariable.getValue(state);
    XhiveSessionIf session = sessionProvider.getSession();
    session.begin();
    XhiveXQueryQueryIf xquery = session.getDatabase()
      .getRoot()
      .createXQuery(xqueryText);
    return new XQueryResult(xquery.execute(), sessionProvider, session);
  }

}
