/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.sql.modelsource;

import java.util.ArrayList;
import java.util.List;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;
import com.emc.ia.sipcreator.plugins.sql.jdbc.JdbcSessionProvider;

public class SQLModelSourcePlugin extends AbstractPluginImpl<ModelSource> {

  private static final String NAME = "sql";

  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(ModelSource.class)
    .description("Creates models from an SQL result set.")
    .param("db", String.class, "The name of the JDBC connection to use.")
    .param("query", String.class, "The query definition.")
    .build();

  public SQLModelSourcePlugin() {
    super(NAME, ModelSource.class, DOCUMENTATION);
  }

  @Override
  public ModelSource create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    String jdbcId = configUtils.getString(context, "db");
    JdbcSessionProvider sessionProvider = context.getObject(JdbcSessionProvider.class, jdbcId);
    List<SQLQueryDefinition> subQueries = compileSubQueries(configUtils, context);
    String query = configUtils.getString(context, "query");
    return new SQLModelSource(sessionProvider, query, subQueries);
  }

  private List<SQLQueryDefinition> compileSubQueries(ConfigUtils configUtils, PluginContext context) {
    return compileSubQueries(configUtils, context.getConfig());
  }

  private SQLQueryDefinition compileQueryDefinition(ConfigUtils configUtils, final Config config) {
    final String id = config.getName();
    final String sql = String.valueOf(config.getValue("query"));
    final List<SQLQueryDefinition> subQueries = compileSubQueries(configUtils, config);
    return new SQLQueryDefinitionImpl(id, sql, subQueries);
  }

  private List<SQLQueryDefinition> compileSubQueries(ConfigUtils configUtils, Config config) {
    List<SQLQueryDefinition> subQueries = new ArrayList<>();
    configUtils.forEach(configUtils.getOptionalGroup(config, "subquery"),
        c -> subQueries.add(compileQueryDefinition(configUtils, c)));
    return subQueries;
  }

//  protected List<SQLQueryDefinition> compileSubQueries(ConfigUtils configUtils, final List<Config> children) {
//    final List<SQLQueryDefinition> subQueries = new ArrayList<SQLQueryDefinition>();
//    if (children != null) {
//      for (final Config child : children) {
//        final String name = child.getName();
//        if ("query".equals(name)) {
//          subQueries.add(compileQueryDefinition(configUtils, child));
//        }
//      }
//    }
//    return subQueries;
//  }

}
