/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.xhive.query.interfaces.XhiveXQueryResultIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;

public class SerializeQueryResult implements XDBQueryResultProcessor {

  private final RuntimeVariable headerVariable;
  private final RuntimeVariable footerVariable;
  private final RuntimeVariable pathVariable;
  private final boolean append;

  public SerializeQueryResult(RuntimeVariable headerVariable, RuntimeVariable footerVariable,
      RuntimeVariable pathVariable, boolean append) {
    this.headerVariable = headerVariable;
    this.footerVariable = footerVariable;
    this.pathVariable = pathVariable;
    this.append = append;
  }

  @Override
  public void process(RuntimeState state, XhiveXQueryResultIf result) {
    String path = pathVariable.getValue(state);
    File file = new File(path);
    try (Writer writer =
        new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file, append)), StandardCharsets.UTF_8)) {

      writer.write(headerVariable.getValue(state));
      while (result.hasNext()) {
        XhiveXQueryValueIf value = result.next();
        writer.write(value.asString());
      }
      writer.write(footerVariable.getValue(state));
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
