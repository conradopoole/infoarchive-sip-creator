/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.rules.ExternalResource;

import com.emc.ia.sipcreator.plugins.sql.jdbc.JdbcSessionProvider;

public class TemporarySqlResource extends ExternalResource implements JdbcSessionProvider {

  private JdbcDataSource ds;

  public TemporarySqlResource(Class<?> parent) {
    try {
      String path = parent.getResource("sample.sql")
        .toURI()
        .toURL()
        .toString();

      ds = new JdbcDataSource();
      ds.setURL("jdbc:h2:mem:db1;INIT=RUNSCRIPT FROM '" + path + "'");
      ds.setUser("sa");
      ds.setPassword("sa");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public Connection getConnection() {
    try {
      return ds.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void release(Connection connection) {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (Exception e) {
      // Swallow
    }
    // TODO Auto-generated method stub

  }

}
