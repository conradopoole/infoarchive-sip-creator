/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.sql.modelsource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.SimpleModelImpl;
import com.emc.ia.sipcreator.plugins.sql.SQLResultConverter;
import com.emc.ia.sipcreator.plugins.sql.jdbc.JdbcSessionProvider;
import com.google.common.base.Throwables;

public class SQLModelIterator implements CloseableIterator<Model> {

  private ResultSet resultSet;
  private int columnCount;
  private String[] columnNames;
  private Connection connection;
  private JdbcSessionProvider sessionProvider;
  private List<SQLQueryDefinition> subQueries;
  private Statement statement;

  public SQLModelIterator(JdbcSessionProvider sessionProvider, Connection connection, Statement statement,
      ResultSet resultSet, List<SQLQueryDefinition> subQueries) {
    try {
      this.sessionProvider = sessionProvider;
      this.connection = connection;
      this.statement = statement;
      this.resultSet = resultSet;
      this.subQueries = subQueries;
      final ResultSetMetaData metaData = resultSet.getMetaData();
      columnCount = metaData.getColumnCount();
      columnNames = new String[columnCount];
      for (int i = 1; i <= columnCount; ++i) {
        final String name = metaData.getColumnName(i);
        columnNames[i - 1] = name;
      }

      for (final SQLQueryDefinition queryDef : subQueries) {
        queryDef.prepare(connection);
      }
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public boolean hasNext() {
    try {
      return resultSet.next();
    } catch (SQLException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public Model next() {
    try {
      Model model = new SimpleModelImpl();
      // Need to copy explicitly to not be dependent on the order.
      // Some result sets require you to access the columns in the order
      // they are declared.
      for (int i = 0; i < columnCount; ++i) {
        model.set(columnNames[i], convert(resultSet.getObject(i + 1)));
      }

      for (final SQLQueryDefinition queryDef : subQueries) {
        model.set(queryDef.getName(), queryDef.evaluate(connection, model));
      }

      return model;
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  protected Object convert(Object object) throws SQLException, IOException {
    return SQLResultConverter.convert(object);
  }

  @Override
  public void close() throws IOException {
    closeQuietly(resultSet);
    closeQuietly(statement);
    sessionProvider.release(connection);
  }

  private void closeQuietly(AutoCloseable closeable) {
    try {
      closeable.close();
    } catch (Exception e) {
      // Swallow
      // TODO: log me
    }
  }

  // @Override
  // public void close() {
  // try {
  // if (resultSet != null) {
  // resultSet.close();
  // }
  // if (statement != null) {
  // statement.close();
  // }
  // if (dataSource != null) {
  // dataSource.releaseConnection(connection);
  // }
  // } catch (final SQLException e) {
  // throw new IOException(e);
  // }
  // }

}
