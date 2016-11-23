/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.steps.state;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.URIResolver;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class LoadPropertiesPluginTests extends PluginTestBase {

  private static final String PROPERTIES = "properties";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(PROPERTIES));

    chk.name(PROPERTIES);
    chk.type(Step.class);
    chk.description("Loads a set of properties.");
    chk.param("file", RuntimeVariable.class, "The property file to load.");
    chk.param("uriresolver", String.class, "The name of a URIResolver.", "uriresolver.default");
    chk.param("prefix", RuntimeVariable.class,
        "An optional prefix which will be prepended to the name of each loaded property.", "");
  }

  @Test
  public void runLoadsTheProperties() {
    PluginContext context =
        context("properties.yml", PROPERTIES, x -> x.setObject("uriresolver.default", classPathResolver(getClass())));

    String value = randomString();
    RuntimeState state = state("name", value);

    Step step = newInstance(Step.class, PROPERTIES, context);
    step.run(state);

    assertEquals("1", state.get("first"));
    assertEquals(value, state.get("second"));
    assertEquals("3", state.get("third"));
  }

  private InputStream fail() throws IOException {
    throw new IOException();
  }

  @Test(expected = RuntimeException.class)
  public void runMissingFileThrowsException() {
    URIResolver resolver = x -> {
      return fail();
    };

    PluginContext context = context("properties.yml", "missing", x -> x.setObject("defaulturiresolver", resolver));

    String value = randomString();
    RuntimeState state = state("name", value);

    Step step = newInstance(Step.class, PROPERTIES, context);
    step.run(state);

  }

}
