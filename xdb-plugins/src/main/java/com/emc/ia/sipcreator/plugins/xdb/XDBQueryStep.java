/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.xhive.core.interfaces.XhiveDatabaseIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryQueryIf;
import com.xhive.query.interfaces.XhiveXQueryResultIf;

public class XDBQueryStep implements Step {

  private final RuntimeVariable queryTextVariable;
  private final RuntimeVariable libraryVariable;
  private final XDBSessionProvider sessionProvider;
  private final XDBQueryResultProcessor processor;

  public XDBQueryStep(RuntimeVariable queryTextVariable, RuntimeVariable libraryVariable,
      XDBSessionProvider sessionProvider, XDBQueryResultProcessor processor) {
    this.queryTextVariable = queryTextVariable;
    this.libraryVariable = libraryVariable;
    this.sessionProvider = sessionProvider;
    this.processor = processor;
  }

  @Override
  public void run(RuntimeState state) {
    String queryText = queryTextVariable.getValue(state);
    String libraryPath = libraryVariable == null ? null : libraryVariable.getValue(state);
    XhiveSessionIf session = null;
    try {
      session = sessionProvider.getSession();
      session.begin();

      XhiveDatabaseIf database = session.getDatabase();
      XhiveLibraryIf root = database.getRoot();
      // TODO: auto create?
      XhiveLibraryIf library = StringUtils.isBlank(libraryPath) ? root : (XhiveLibraryIf)root.get(libraryPath);

      XhiveXQueryQueryIf xquery = library.createXQuery(queryText);

      XhiveXQueryResultIf result = null;
      try {
        result = xquery.execute();
        processor.process(state, result);
      } finally {
        if (result != null) {
          result.close();
        }
      }
      session.commit();
    } finally {
      if (session != null) {
        session.rollback();
        sessionProvider.releaseSession(session);
      }
    }

  }

}
