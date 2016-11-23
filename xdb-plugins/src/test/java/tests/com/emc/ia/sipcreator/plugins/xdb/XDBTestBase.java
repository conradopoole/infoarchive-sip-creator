/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.xdb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import com.emc.ia.sdk.support.xml.XmlBuilder;
import com.emc.ia.sdk.support.xml.XmlUtil;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;
import com.emc.ia.sipcreator.runtime.ApplicationState;
import com.emc.ia.sipcreator.testing.xdb.TemporaryXDBResource;
import com.xhive.core.interfaces.XhiveDatabaseIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveDocumentIf;
import com.xhive.dom.interfaces.XhiveLibraryChildIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;

public class XDBTestBase extends PluginTestBase {

  @ClassRule
  public static final TemporaryXDBResource XDB = new TemporaryXDBResource();

  protected static final Consumer<ApplicationState> WITH_DEFAULT_XDBPROVIDER =
      x -> x.setObject("xdb.default", new TestingXDBSessionProvider(XDB));

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  protected void diff(String expected, String actual) {
    Diff myDiff = DiffBuilder.compare(Input.fromString(expected))
      .withTest(Input.fromString(actual))
      .checkForSimilar()
      .ignoreWhitespace()
      .ignoreComments()
      .build();
    Assert.assertFalse("XML similar " + myDiff.toString(), myDiff.hasDifferences());
  }

  protected File createFile(File root, String uuid, String content) throws IOException {
    File file = new File(root, uuid);
    try (Writer out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
      IOUtils.write(content, out);
    }
    return file;
  }

  protected String uuid() {
    return UUID.randomUUID()
      .toString();
  }

  protected String xml(String root, String name, String value) {
    return XmlUtil.toString(XmlBuilder.newDocument()
      .element(root)
      .element(name, value)
      .end()
      .build());
  }

  protected void createLibrary(String libraryPath) {
    XhiveSessionIf session = null;

    try {
      session = XDB.getSession();

      session.begin();

      XhiveDatabaseIf database = session.getDatabase();
      XhiveLibraryIf root = database.getRoot();
      XhiveLibraryChildIf library = root.createLibrary(XhiveLibraryIf.CONCURRENT_LIBRARY);
      library.setName(libraryPath);
      root.appendChild(library);

      session.commit();

    } finally {
      XDB.releaseSession(session);
    }
  }

  protected void loadDocument(String libraryPath, String name, String xml) {
    XhiveSessionIf session = null;

    try {
      session = XDB.getSession();

      session.begin();

      XhiveDatabaseIf database = session.getDatabase();
      XhiveLibraryIf root = database.getRoot();
      XhiveLibraryIf library = (XhiveLibraryIf)root.getByPath(libraryPath);

      final LSInput lsInput = library.createLSInput();
      final LSParser parser = library.createLSParser();

      lsInput.setStringData(xml);

      final XhiveDocumentIf doc = (XhiveDocumentIf)parser.parse(lsInput);

      doc.setName(name);

      library.appendChild(doc);

      session.commit();

    } finally {
      XDB.releaseSession(session);
    }

  }

}
