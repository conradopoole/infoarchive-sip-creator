/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.aiusource;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.emc.ia.sdk.sip.assembly.DigitalObject;
import com.emc.ia.sipcreator.api.AIU;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSerializer;

public class AIUFromModelAdapter implements AIU {

  private Model value;
  private ModelSerializer converter;
  private DigitalObjectsFromModel extractor;

  public AIUFromModelAdapter(ModelSerializer converter, DigitalObjectsFromModel extractor) {
    this(converter, extractor, null);
  }

  public AIUFromModelAdapter(ModelSerializer converter, DigitalObjectsFromModel extractor, Model value) {
    this.converter = converter;
    this.extractor = extractor;
    this.value = value;
  }

  public Model getValue() {
    return value;
  }

  public void setValue(Model value) {
    this.value = value;
  }

  @Override
  public void writeAsXml(Writer writer) throws IOException {
    converter.serialize(value, writer);
  }

  @Override
  public List<DigitalObject> getDigitalObjects() {
    return extractor.getDigitalObjects(value);
  }

}
