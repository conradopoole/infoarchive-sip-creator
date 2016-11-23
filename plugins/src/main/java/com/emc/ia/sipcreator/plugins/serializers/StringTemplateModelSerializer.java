/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.serializers;

import java.io.IOException;
import java.io.Writer;

import org.stringtemplate.v4.ST;

import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSerializer;

public class StringTemplateModelSerializer implements ModelSerializer {

  private final ST template;

  public StringTemplateModelSerializer(ST template) {
    this.template = template;
  }

  @Override
  public void serialize(Model model, Writer writer) throws IOException {
    ST st = new ST(template);
    st.add("model", model);
    writer.write(st.render());
  }

}
