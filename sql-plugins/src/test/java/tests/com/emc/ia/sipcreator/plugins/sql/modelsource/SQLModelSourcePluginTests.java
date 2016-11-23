/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.sql.modelsource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Test;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

import tests.com.emc.ia.sipcreator.plugins.sql.TemporarySqlResource;

public class SQLModelSourcePluginTests extends PluginTestBase {

  private static final String SQL = "sql";

  @ClassRule
  public static final TemporarySqlResource DB = new TemporarySqlResource(SQLModelSourcePluginTests.class);

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(SQL));

    chk.name(SQL);
    chk.type(ModelSource.class);
    chk.description("Creates models from an SQL result set.");
    chk.param("db", String.class, "The name of the JDBC connection to use.");
    chk.param("query", String.class, "The query definition.");
  }

  @Test
  public void runExecuteStepsForEachRow() throws IOException {
    PluginContext context = context("sqlmodels.yml", SQL, x -> x.setObject("jdbc.sample", DB));

    RuntimeState state = state("foo", "");

    try (ModelSource source = newInstance(ModelSource.class, SQL, context)) {
      try (CloseableIterator<Model> models = source.getModels(state)) {

        check(models, "A", "A1", "A2", "A3");
        check(models, "B", "B1", "B2", "B3");
        assertFalse(models.hasNext());
      }
    }

  }

  @Test(expected = Exception.class)
  public void runInvalidQueryThrowException() throws IOException {
    PluginContext context = context("sqlmodels.yml", "sqlInvalidSubQuery", x -> x.setObject("jdbc.sample", DB));

    try (ModelSource source = newInstance(ModelSource.class, SQL, context)) {
      // NOP
    }

  }

  private void check(Iterator<Model> models, String name, String... subNames) {
    assertTrue(models.hasNext());
    Model model = models.next();
    assertEquals(name, model.get("NAME"));
    List<Model> subs = (List<Model>)model.get("sub");
    int i = 0;
    for (String subName : subNames) {
      assertEquals(subName, subs.get(i++)
        .get("NAME"));
    }

  }
}
