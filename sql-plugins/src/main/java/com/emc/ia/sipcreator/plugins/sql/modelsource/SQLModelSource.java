/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.sql.modelsource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.plugins.sql.jdbc.JdbcSessionProvider;
import com.emc.ia.sipcreator.utils.CloseUnlessTransferred;
import com.google.common.base.Throwables;

public class SQLModelSource implements ModelSource {

  private final JdbcSessionProvider sessionProvider;
  private final String query;
  private final List<SQLQueryDefinition> subQueries;

  public SQLModelSource(JdbcSessionProvider sessionProvider, String query, List<SQLQueryDefinition> subQueries) {
    this.sessionProvider = sessionProvider;
    this.query = query;
    this.subQueries = subQueries;
  }

  @Override
  public CloseableIterator<Model> getModels(RuntimeState state) {
    try (CloseUnlessTransferred<Connection> connection = CloseUnlessTransferred.of(sessionProvider.getConnection());
        CloseUnlessTransferred<Statement> statement = CloseUnlessTransferred.of(connection.getResource()
          .createStatement());
        CloseUnlessTransferred<ResultSet> resultSet = CloseUnlessTransferred.of(statement.getResource()
          .executeQuery(query))) {
      SQLModelIterator iterator = new SQLModelIterator(sessionProvider, connection.getResource(),
          statement.getResource(), resultSet.getResource(), subQueries);
      connection.setTransferred(true);
      statement.setTransferred(true);
      resultSet.setTransferred(true);
      return iterator;
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public void close() throws IOException {
    for (SQLQueryDefinition queries : subQueries) {
      queries.close();
    }
  }

}
