/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.xdb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import com.emc.ia.sipcreator.api.AIU;
import com.emc.ia.sipcreator.api.AIUCollection;
import com.emc.ia.sipcreator.api.AIUSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.xhive.util.interfaces.StringWriter;

public class AiuFromXQueryPluginTests extends XDBTestBase {

  private static final String AIU_FROM_X_QUERY = "aiuFromXQuery";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(AIU_FROM_X_QUERY));
    chk.name(AIU_FROM_X_QUERY);
    chk.type(AIUSource.class);
    chk.description("Loads the specified files into the specified library in xDB.");
    chk.param("xdb", String.class, "The name of the xDB session provider to use.", "xdb.default");
    // .param("library", RuntimeVariable.class, "The name of the xDB library to create.")
    chk.param("query", RuntimeVariable.class, "The query which selects the AIUs.");
  }

  @Test
  public void convert() throws IOException {
    PluginContext context = context("aiufromxquery.yml", AIU_FROM_X_QUERY, WITH_DEFAULT_XDBPROVIDER);

    AIUSource aiuSource = newInstance(AIUSource.class, AIU_FROM_X_QUERY, context);

    String library = uuid();
    String root = randomAlphaString();
    String name = randomAlphaString();
    String xml1 = xml(root, name, randomString());
    String xml2 = xml(root, name, randomString());

    createLibrary(library);
    loadDocument(library, uuid(), xml1);
    loadDocument(library, uuid(), xml2);

    RuntimeState state = state("library", library, "root", root);
    try (AIUCollection aius = aiuSource.getAIUs(state)) {
      check(aius, xml1);
      check(aius, xml2);
      assertFalse(aius.hasNext());
    }
  }

  private void check(Iterator<AIU> aius, String expected) throws IOException {
    assertTrue(aius.hasNext());
    AIU aiu = aius.next();
    StringWriter writer = new StringWriter();
    aiu.writeAsXml(writer);
    diff(expected, writer.toString());
  }

}
