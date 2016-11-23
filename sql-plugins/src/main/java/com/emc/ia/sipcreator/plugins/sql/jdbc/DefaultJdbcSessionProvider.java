/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.sql.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.ia.sipcreator.utils.Params;
import com.google.common.base.Throwables;

public class DefaultJdbcSessionProvider implements JdbcSessionProvider {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultJdbcSessionProvider.class);

  private final String connectionString;
  private final String password;
  private final String username;
  private final String driver;
  private boolean initialized;
  private Connection connection;

  public DefaultJdbcSessionProvider(String driver, String connectionString, String password, String username) {
    Params.notBlank(connectionString, "JdbcSessionProviderImpl.connectionString");

    this.driver = driver;
    this.connectionString = connectionString;
    this.password = password;
    this.username = username;

    LOG.debug("JdbcSessionProviderImpl.driver = {}.", driver);
    LOG.debug("JdbcSessionProviderImpl.connection = {}.", connectionString);
    LOG.debug("JdbcSessionProviderImpl.username = {}.", username);
    LOG.debug("JdbcSessionProviderImpl.password = {}.", password);

    init();
  }

  private void init() {
    if (!initialized) {
      try {
        // To allow for pre JDBC 4.0 drivers
        manuallyLoadDriver();

      } catch (final Exception e) {
        throw new IllegalArgumentException("Failed to load the driver " + driver, e);
      }
      initialized = true;
    }

  }

  @Override
  public Connection getConnection() {
    try {
      if (connection == null) {
        connection = DriverManager.getConnection(connectionString, username, password);
      }

      return connection;

    } catch (final SQLException e) {
      throw Throwables.propagate(e);
      // throw new ExecutionException(e, "Failed to connect to JDBC data
      // source (connection = {}, username = {}, password = {}). Reason
      // given \"{}\".", connectionString, username, password,
      // e.getMessage());
    }

  }

  private void manuallyLoadDriver() throws ClassNotFoundException {
    LOG.debug("Loading JDBC driver {}.", driver);
    if (driver != null) {
      Class.forName(driver);
    }
  }

  @Override
  public void close() throws IOException {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (final SQLException e) {
      throw new RuntimeException("Failed to close JDBC connection. Reason given " + e.getMessage(), e);
    }
  }

  @Override
  public String toString() {
    return "DefaultJdbcSessionProvider [connection=" + connection + ", connectionString=" + connectionString
        + ", password=" + password + ", username=" + username + ", driver=" + driver + ", initialized=" + initialized
        + "]";
  }

  @Override
  public void release(Connection aConnection) {
//    try {
//      if (aConnection != null) {
//        aConnection.close();
//      }
//    } catch (Exception e) {
//      // Swallow
//      // TODO: log me
//    }

  }

}
