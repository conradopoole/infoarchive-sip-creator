/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */

package com.emc.ia.sipcreator.api;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.emc.ia.sdk.sip.assembly.DigitalObject;

public interface AIU {

  void writeAsXml(Writer writer) throws IOException;

  List<DigitalObject> getDigitalObjects();

}
