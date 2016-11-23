/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import java.io.File;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

import com.emc.ia.sipcreator.api.FilesCollection;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.utils.Params;
import com.xhive.core.interfaces.XhiveDatabaseIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveDocumentIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;

public class XDBLoadStep implements Step {

  private final XDBSessionProvider sessionProvider;
  private final RuntimeVariable libraryPathVariable;
  private final String filesVariable;

  public XDBLoadStep(XDBSessionProvider sessionProvider, RuntimeVariable libraryPathVariable, String filesVariable) {
    Params.notNull(sessionProvider, "XDBLoadStep.sessionProvider");
    Params.notNull(sessionProvider, "XDBLoadStep.libraryPathVariable");
    Params.notNull(sessionProvider, "XDBLoadStep.filesVariable");
    this.sessionProvider = sessionProvider;
    this.libraryPathVariable = libraryPathVariable;
    this.filesVariable = filesVariable;
  }

  @Override
  public void run(RuntimeState state) {
    String libraryPath = libraryPathVariable.getValue(state);

    XhiveSessionIf session = null;
    try {
      session = sessionProvider.getSession();
      FilesCollection fileCollection = (FilesCollection)state.get(filesVariable);
      for (File file : fileCollection) {
        importFile(session, libraryPath, file);
      }
    } finally {
      sessionProvider.releaseSession(session);
    }
  }

  private void importFile(XhiveSessionIf session, String libraryPath, File file) {
    try {
      session.begin();

      XhiveDatabaseIf database = session.getDatabase();
      XhiveLibraryIf root = database.getRoot();
      // TODO: auto create?
      // TODO: verify existence
      XhiveLibraryIf library = (XhiveLibraryIf)root.get(libraryPath);

      final LSInput lsInput = library.createLSInput();
      final LSParser parser = library.createLSParser();

      String name = file.getName();

      lsInput.setSystemId("file:///" + file.getAbsolutePath());

      // TODO: Overwrite or fail?
      if (library.get(name) != null) {
        library.removeChild(library.get(name));
      }

      final XhiveDocumentIf doc = (XhiveDocumentIf)parser.parse(lsInput);

      doc.setName(name);

      library.appendChild(doc);

      session.commit();
    } finally {
      session.rollback();
    }
  }

  @Override
  public String toString() {
    return "XDBLoadStep [sessionProvider=" + sessionProvider + ", libraryPathVariable=" + libraryPathVariable
        + ", filesVariable=" + filesVariable + "]";
  }

}
