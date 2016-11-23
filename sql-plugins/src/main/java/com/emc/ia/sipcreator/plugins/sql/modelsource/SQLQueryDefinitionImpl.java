/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.sql.modelsource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.SimpleModelImpl;
import com.emc.ia.sipcreator.plugins.sql.SQLResultConverter;

public class SQLQueryDefinitionImpl implements SQLQueryDefinition {

  private final List<String> variables = new ArrayList<String>();
  private PreparedStatement statement;
  private final String sql;
  private final String name;
  private final List<SQLQueryDefinition> subQueries;

  public SQLQueryDefinitionImpl(final String name, final String sql, final List<SQLQueryDefinition> subQueries) {
    this.name = name;
    this.subQueries = subQueries;

    final Pattern pattern = Pattern.compile("(\\$([^\\$]+)\\$)");
    final Matcher matcher = pattern.matcher(sql);
    final StringBuffer buf = new StringBuffer();
    while (matcher.find()) {
      variables.add(matcher.group(2));
      matcher.appendReplacement(buf, "?");
    }
    matcher.appendTail(buf);

    this.sql = buf.toString();
  }

  @Override
  public void prepare(final Connection connection) throws SQLException {

    statement = connection.prepareStatement(sql);

    if (subQueries != null) {
      for (final SQLQueryDefinition subQuery : subQueries) {
        subQuery.prepare(connection);
      }
    }
  }

  @Override
  public List<Model> evaluate(final Connection connection, final Model parentModel) throws SQLException, IOException {

    final int len = variables.size();
    for (int i = 0; i < len; ++i) {
      statement.setObject(i + 1, parentModel.get(variables.get(i)));
    }

    try (ResultSet resultSet = statement.executeQuery()) {
      final ResultSetMetaData md = resultSet.getMetaData();
      final int columns = md.getColumnCount();
      final List<Model> models = new ArrayList<Model>();
      while (resultSet.next()) {
        final Model model = new SimpleModelImpl();
        model.set("parent", parentModel);
        for (int i = 1; i <= columns; ++i) {
          model.set(md.getColumnName(i), SQLResultConverter.convert(resultSet.getObject(i)));
        }

        for (final SQLQueryDefinition queryDef : subQueries) {
          final List<Model> subModel = queryDef.evaluate(connection, model);
          model.set(queryDef.getName(), subModel);
        }

        models.add(model);
      }
      return models;
    }

  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void close() throws IOException {
    for (SQLQueryDefinition def : subQueries) {
      IOUtils.closeQuietly(def);
    }

    try {
      statement.close();
    } catch (SQLException e) {
      // swallow
      // TODO log me
    }
  }

}
