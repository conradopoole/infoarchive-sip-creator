/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.sql.modelsource;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.ClassRule;
import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.plugins.sql.jdbc.JdbcSessionProvider;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

import tests.com.emc.ia.sipcreator.plugins.sql.TemporarySqlResource;

public class DefaultJdbcSessionProviderPluginTests extends PluginTestBase {

  private static final String JDBC = "jdbc";
  @ClassRule
  public static final TemporarySqlResource DB = new TemporarySqlResource(DefaultJdbcSessionProviderPluginTests.class);

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(JDBC));

    chk.name(JDBC);
    chk.param("driver", String.class, "The JDBC driver to use.");
    chk.param("url", String.class, "The connection string.");
    chk.param("username", String.class, "The username.");
    chk.param("password", String.class, "The password");
  }

  @Test
  public void runExecuteStepsForEachRow() throws IOException, SQLException {
    PluginContext context = context("jdbc.yml", JDBC);

    try (JdbcSessionProvider provider = newInstance(JdbcSessionProvider.class, JDBC, context)) {

      try (Connection connection = provider.getConnection()) {
        assertTrue(connection.isValid(10));
      }
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void runUnknownDriverThrowException() throws IOException, SQLException {
    PluginContext context = context("jdbc.yml", "jdbcUnknown");

    try (JdbcSessionProvider provider = newInstance(JdbcSessionProvider.class, JDBC, context)) {
      // NOP
    }

  }

}
