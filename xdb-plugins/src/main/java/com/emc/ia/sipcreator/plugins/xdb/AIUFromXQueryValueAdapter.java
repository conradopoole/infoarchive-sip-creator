/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import com.emc.ia.sdk.sip.assembly.DigitalObject;
import com.emc.ia.sipcreator.api.AIU;
import com.xhive.dom.interfaces.XhiveNodeIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;

public class AIUFromXQueryValueAdapter implements AIU {

  private XhiveXQueryValueIf value;

  public AIUFromXQueryValueAdapter() {
    this(null);
  }

  public AIUFromXQueryValueAdapter(XhiveXQueryValueIf value) {
    this.value = value;
  }

  public XhiveXQueryValueIf getValue() {
    return value;
  }

  public void setValue(XhiveXQueryValueIf value) {
    this.value = value;
  }

  @Override
  public void writeAsXml(Writer writer) throws IOException {
    XhiveNodeIf node = value.asNode();
    node.toXml(writer, false);

  }

  @Override
  public List<DigitalObject> getDigitalObjects() {

    return Collections.emptyList();
  }

}
