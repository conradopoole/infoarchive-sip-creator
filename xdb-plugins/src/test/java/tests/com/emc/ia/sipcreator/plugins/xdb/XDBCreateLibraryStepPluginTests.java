/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.xdb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryChildIf;

public class XDBCreateLibraryStepPluginTests extends XDBTestBase {

  private static final String XDB_LIBRARY = "xdb.library";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(XDB_LIBRARY));

    chk.name(XDB_LIBRARY);
    chk.type(Step.class);
    chk.description("Creates a library, if it doesn't already exist, in the specified xDB instance.");
    chk.param("library", RuntimeVariable.class, "The name of the xDB library to create.");
    chk.param("xdb", String.class, "The name of the xDB session provider to use.", "xdb.default");
  }

  @Test
  public void runCreatesLibrary() {
    String libraryPath = UUID.randomUUID()
      .toString();

    assertFalse(existsLibrary(libraryPath));

    PluginContext context = context("library.yml", XDB_LIBRARY, WITH_DEFAULT_XDBPROVIDER);
    Step step = newInstance(Step.class, XDB_LIBRARY, context);
    RuntimeState state = state("library", libraryPath);
    step.run(state);

    assertTrue(existsLibrary(libraryPath));

  }

  private boolean existsLibrary(String libraryPath) {
    XhiveSessionIf session = null;

    try {
      session = XDB.getSession();

      session.begin();

      XhiveLibraryChildIf library = session.getDatabase()
        .getRoot()
        .get(libraryPath);

      session.rollback();

      return library != null;

    } finally {
      XDB.releaseSession(session);
    }
  }

}
