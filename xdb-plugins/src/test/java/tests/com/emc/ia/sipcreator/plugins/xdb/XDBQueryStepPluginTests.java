/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.xdb;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.xdb.XDBQueryResultProcessor;
import com.emc.ia.sipcreator.plugins.xdb.XDBSessionProvider;

public class XDBQueryStepPluginTests extends XDBTestBase {

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin("xdb.query"));

    chk.name("xdb.query");
    chk.type(Step.class);
    chk.description("Runs the specified xQuery.");
    chk.param("library", RuntimeVariable.class, "The optional name of the xDB library to create.");
    chk.param("xdb", String.class, "The name of the xDB session provider to use.", "xdb.default");
    chk.param("query", RuntimeVariable.class, "The xQuery.");
    chk.param("processor", XDBQueryResultProcessor.class, "The optional query result processor.", "traverse");
  }

  @Test
  public void runLoadsFiles() throws IOException {
    File result = folder.newFile();

    String libraryPath = uuid();
    createLibrary(libraryPath);

    loadDocument(libraryPath, "sample", textOfResource("query.sample.xml"));

    PluginContext providerContext = context("session.yml", "xdb");
    XDBSessionProvider sessionProvider = newInstance(XDBSessionProvider.class, "xdb", providerContext);

    PluginContext context = context("query.yml", "query", x -> x.setObject("xdb.default", sessionProvider));

    Step step = newInstance(Step.class, "xdb.query", context);

    RuntimeState state = state("file", result.getAbsolutePath());
    step.run(state);

    assertEquals(textOfResource("query.result.txt"), textOf(result));
  }

  private String textOf(File result) throws IOException {
    return IOUtils.toString(result.toURI(), StandardCharsets.UTF_8);
  }

  private String textOfResource(String resource) throws IOException {
    return IOUtils.toString(getClass().getResource(resource), StandardCharsets.UTF_8);
  }

}
