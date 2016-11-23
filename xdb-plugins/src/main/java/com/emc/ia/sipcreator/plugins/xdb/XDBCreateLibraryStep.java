/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.utils.Params;
import com.xhive.core.interfaces.XhiveDatabaseIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;

public class XDBCreateLibraryStep implements Step {

  private final XDBSessionProvider sessionProvider;
  private final RuntimeVariable libraryPathVariable;

  public XDBCreateLibraryStep(XDBSessionProvider sessionProvider, RuntimeVariable libraryPathVariable) {
    Params.notNull(sessionProvider, "XDBCreateLibraryStep.sessionProvider");
    Params.notNull(libraryPathVariable, "XDBCreateLibraryStep.libraryPathVariable");
    this.sessionProvider = sessionProvider;
    this.libraryPathVariable = libraryPathVariable;
  }

  @Override
  public void run(RuntimeState state) {
    String libraryPath = libraryPathVariable.getValue(state);

    XhiveSessionIf session = null;
    try {
      session = sessionProvider.getSession();
      try {
        session.begin();
        XhiveDatabaseIf database = session.getDatabase();
        XhiveLibraryIf root = database.getRoot();
        String[] components = libraryPath.split("/");
        XhiveLibraryIf lib = root;
        for (int i = 0; i < components.length; ++i) {
          XhiveLibraryIf child = (XhiveLibraryIf)lib.get(components[i]);
          if (child == null) {
            child = lib.createLibrary();
            child.setName(components[i]);
            lib.appendChild(child);
          }
          lib = child;
        }
        session.commit();
      } finally {
        session.rollback();
      }
    } finally {
      sessionProvider.releaseSession(session);
    }
  }

  @Override
  public String toString() {
    return "XDBCreateLibraryStep [sessionProvider=" + sessionProvider + ", libraryPathVariable=" + libraryPathVariable
        + "]";
  }

}
