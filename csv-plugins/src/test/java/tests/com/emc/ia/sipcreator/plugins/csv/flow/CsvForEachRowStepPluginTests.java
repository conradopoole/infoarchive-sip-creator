/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.csv.flow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class CsvForEachRowStepPluginTests extends PluginTestBase {

  private static final String CSV_FOREACHROW = "csv.foreachrow";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(CSV_FOREACHROW));

    chk.name(CSV_FOREACHROW);
    chk.type(Step.class);
    chk.description(
        "Executes the specified steps once for each row in the specified CSV file after updating the context with the values of the row.");
    chk.param("file", RuntimeVariable.class, "The CSV file to load");
    chk.param("charset", RuntimeVariable.class, "The character set of the CSV file.", "UTF-8");
    chk.param("separator", String.class, "The CSV separator character.", ",");
    chk.param("quote", String.class, "The CSV quote character.", "\"");
    chk.param("escape", String.class, "The CSV escape character.", "\\");
    chk.param("steps", Step.class, "The steps that should be repeated.");
    chk.param("uriresolver", String.class, "The name of a URIResolver.", "uriresolver.default");

  }

  @Test
  public void runExecuteStepsForEachRow() {
    PluginContext context =
        context("csv.yml", CSV_FOREACHROW, x -> x.setObject("uriresolver.default", classPathResolver(getClass())));
    Step step = newInstance(Step.class, CSV_FOREACHROW, context);
    RuntimeState state = state("result", "");
    step.run(state);
    assertEquals("123456", state.get("result"));
  }

}
