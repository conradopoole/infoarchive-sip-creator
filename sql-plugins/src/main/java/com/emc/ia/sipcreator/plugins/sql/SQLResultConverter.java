/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.sql;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

public final class SQLResultConverter {

  private SQLResultConverter() {
    throw new IllegalStateException(
        "SQLResultConverter is a static utility class and its constructor should never be called.");
  }

  public static Object convert(final Object object) throws SQLException, IOException {
    if (object instanceof Clob) {
      final Clob clob = (Clob)object;
      final Reader reader = clob.getCharacterStream();
      try {
        return IOUtils.toString(reader);
      } finally {
        IOUtils.closeQuietly(reader);
      }
    } else {
      return object;
    }
  }
}
