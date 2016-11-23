/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.sql.modelsource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.plugins.sql.jdbc.JdbcSessionProvider;
import com.emc.ia.sipcreator.plugins.sql.modelsource.SQLModelSource;
import com.emc.ia.sipcreator.plugins.sql.modelsource.SQLQueryDefinition;

public class SQLModelSourceTests {

  private JdbcSessionProvider sessionProvider;
  private String query;

  private SQLModelSource modelSource;
  private RuntimeState state;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    query = RandomStringUtils.random(32);
    List<SQLQueryDefinition> subQueries = mock(List.class);
    sessionProvider = mock(JdbcSessionProvider.class);
    modelSource = new SQLModelSource(sessionProvider, query, subQueries);
    state = mock(RuntimeState.class);
  }

  @Test(expected = RuntimeException.class)
  public void getModelsConnectionFailsThrowException() throws IOException {
    when(sessionProvider.getConnection()).thenThrow(RuntimeException.class);
    try (CloseableIterator<Model> models = modelSource.getModels(state)) {
      // NOP
    }

  }

  @Test(expected = RuntimeException.class)
  public void createStatementFailsThrowException() throws SQLException, IOException {
    Connection connection = mock(Connection.class); // NOPMD
    when(sessionProvider.getConnection()).thenReturn(connection);
    when(connection.createStatement()).thenThrow(SQLException.class);
    try (CloseableIterator<Model> models = modelSource.getModels(state)) {
      // NOP
    }

  }

  @Test(expected = RuntimeException.class)
  public void executeQueryFailsThrowException() throws SQLException, IOException {
    Connection connection = mock(Connection.class); // NOPMD
    Statement statement = mock(Statement.class); // NOPMD
    when(sessionProvider.getConnection()).thenReturn(connection);
    when(connection.createStatement()).thenReturn(statement);
    when(statement.executeQuery(query)).thenThrow(SQLException.class);
    try (CloseableIterator<Model> models = modelSource.getModels(state)) {
      // NOP
    }

  }

  /*
   * @Test(expected = RuntimeException.class) public void processQueryFailsThrowException() throws SQLException,
   * IOException { Connection connection = mock(Connection.class); Statement statement = mock(Statement.class);
   * ResultSet rset = mock(ResultSet.class); when(sessionProvider.getConnection()).thenReturn(connection);
   * when(connection.createStatement()).thenReturn(statement); when(statement.executeQuery(query)).thenReturn(rset);
   * when(rset.getMetaData()).thenThrow(SQLException.class); try (CloseableIterator<Model> models =
   * modelSource.getModels(state)) { // NOP }
   * 
   * }
   * 
   * @Test(expected = RuntimeException.class) public void closeResultSetFailsThrowException() throws SQLException,
   * IOException { Connection connection = mock(Connection.class); Statement statement = mock(Statement.class);
   * ResultSet rset = mock(ResultSet.class); when(sessionProvider.getConnection()).thenReturn(connection);
   * when(connection.createStatement()).thenReturn(statement); when(statement.executeQuery(query)).thenReturn(rset);
   * doThrow(Exception.class).when(rset) .close(); try (CloseableIterator<Model> models = modelSource.getModels(state))
   * { // NOP }
   * 
   * }
   * 
   * @Test(expected = RuntimeException.class) public void closeStatementFailsThrowException() throws SQLException,
   * IOException { Connection connection = mock(Connection.class); Statement statement = mock(Statement.class);
   * ResultSet rset = mock(ResultSet.class); when(sessionProvider.getConnection()).thenReturn(connection);
   * when(connection.createStatement()).thenReturn(statement); when(statement.executeQuery(query)).thenReturn(rset);
   * doThrow(Exception.class).when(statement) .close(); try (CloseableIterator<Model> models =
   * modelSource.getModels(state)) { // NOP }
   * 
   * }
   * 
   * @Test(expected = RuntimeException.class) public void closeConnectionFailsThrowException() throws SQLException,
   * IOException { Connection connection = mock(Connection.class); Statement statement = mock(Statement.class);
   * ResultSet rset = mock(ResultSet.class); when(sessionProvider.getConnection()).thenReturn(connection);
   * when(connection.createStatement()).thenReturn(statement); when(statement.executeQuery(query)).thenReturn(rset);
   * doThrow(Exception.class).when(connection) .close(); try (CloseableIterator<Model> models =
   * modelSource.getModels(state)) { // NOP }
   * 
   * }
   */
}
