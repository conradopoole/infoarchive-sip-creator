/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.sql.jdbc;

import java.io.Closeable;
import java.sql.Connection;

public interface JdbcSessionProvider extends Closeable {

  Connection getConnection();

  void release(Connection connection);

}
