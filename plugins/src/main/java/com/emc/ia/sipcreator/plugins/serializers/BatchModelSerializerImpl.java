/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.serializers;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import com.emc.ia.sdk.support.io.DataBuffer;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelBatchSerializer;
import com.emc.ia.sipcreator.api.ModelSerializer;

public class BatchModelSerializerImpl implements ModelBatchSerializer {

  private final String header;
  private final String footer;
  private final ModelSerializer serializer;
  private Writer writer;

  public BatchModelSerializerImpl(String header, String footer, ModelSerializer serializer) {
    this.header = header;
    this.footer = footer;
    this.serializer = serializer;
  }

  @Override
  public void open(DataBuffer buffer) throws IOException {
    writer = new OutputStreamWriter(new BufferedOutputStream(buffer.openForWriting()), Charset.forName("UTF-8"));
    writer.write(header);
  }

  @Override
  public void serialize(Model model) throws IOException {
    serializer.serialize(model, writer);
  }

  @Override
  public void end() throws IOException {
    writer.write(footer);
    writer.close();
  }

}
