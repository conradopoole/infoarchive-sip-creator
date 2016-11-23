/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.xdb;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.utils.MutableFilesCollection;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryChildIf;

public class XDBLoadStepPluginTests extends XDBTestBase {

  private static final String XDB_LOAD = "xdb.load";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(XDB_LOAD));

    chk.name(XDB_LOAD);
    chk.type(Step.class);
    chk.description("Loads the specified files into the specified library in xDB.");
    chk.param("library", RuntimeVariable.class, "The name of the xDB library to create.");
    chk.param("xdb", String.class, "The name of the xDB session provider to use.", "xdb.default");
    chk.param("files", String.class, "Name of the variable that holds the files to load.", "files");
  }

  @Test
  public void runLoadsFiles() throws IOException {
    File root = folder.newFolder();
    String xml1 = xml(randomAlphaString(), randomAlphaString(), randomString());
    String xml2 = xml(randomAlphaString(), randomAlphaString(), randomString());
    File file1 = createFile(root, uuid(), xml1);
    File file2 = createFile(root, uuid(), xml2);

    String libraryPath = uuid();

    createLibrary(libraryPath);

    PluginContext context = context("load.yml", XDB_LOAD, WITH_DEFAULT_XDBPROVIDER);
    Step step = newInstance(Step.class, XDB_LOAD, context);
    MutableFilesCollection files = new MutableFilesCollection();
    files.add(file1);
    files.add(file2);
    RuntimeState state = state("library", libraryPath, "files", files);
    step.run(state);

    checkDocument(libraryPath, file1.getName(), xml1);
    checkDocument(libraryPath, file1.getName(), xml1);
  }

  private void checkDocument(String libraryPath, String name, String xml) {
    XhiveSessionIf session = null;

    try {
      session = XDB.getSession();

      session.begin();

      XhiveLibraryChildIf document = session.getDatabase()
        .getRoot()
        .get(libraryPath)
        .getByPath(name);

      diff(xml, document.toXml());

      session.commit();

    } finally {
      XDB.releaseSession(session);
    }

  }

}
