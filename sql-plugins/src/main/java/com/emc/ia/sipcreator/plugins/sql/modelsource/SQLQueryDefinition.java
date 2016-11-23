/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.sql.modelsource;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.emc.ia.sipcreator.api.Model;

public interface SQLQueryDefinition extends Closeable {

  void prepare(final Connection connection) throws SQLException;

  List<Model> evaluate(final Connection connection, final Model model) throws SQLException, IOException;

  String getName();
}
