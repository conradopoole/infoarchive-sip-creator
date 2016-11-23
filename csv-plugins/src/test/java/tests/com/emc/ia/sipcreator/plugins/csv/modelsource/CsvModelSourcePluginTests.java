/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.csv.modelsource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class CsvModelSourcePluginTests extends PluginTestBase {

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin("csv"));

    chk.name("csv");
    chk.type(ModelSource.class);
    chk.description("Creates models from the specified CSV file.");
    chk.param("file", RuntimeVariable.class, "The CSV file to load");
    chk.param("charset", RuntimeVariable.class, "The character set of the CSV file.", "UTF-8");
    chk.param("separator", String.class, "The CSV separator character.", ",");
    chk.param("quote", String.class, "The CSV quote character.", "\"");
    chk.param("escape", String.class, "The CSV escape character.", "\\");
    chk.param("uriresolver", String.class, "The name of a URIResolver.", "uriresolver.default");
  }

  @Test
  public void runExecuteStepsForEachRow() throws IOException {
    PluginContext context =
        context("csv.yml", "modelsFromCsv", x -> x.setObject("uriresolver.default", classPathResolver(getClass())));
    RuntimeState state = state("result", "");
    try (ModelSource source = newInstance(ModelSource.class, "csv", context)) {
      Iterator<Model> models = source.getModels(state);

      check(models, "1", "2");
      check(models, "3", "4");
      check(models, "5", "6");
      assertFalse(models.hasNext());
    }

  }

  private void check(Iterator<Model> models, String a, String b) {
    assertTrue(models.hasNext());
    Model model = models.next();
    assertEquals(a, model.get("a"));
    assertEquals(b, model.get("b"));
  }

}
