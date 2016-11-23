/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.digitalobjects;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.emc.ia.sdk.sip.assembly.DigitalObject;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.Model;

public class SingleContent implements DigitalObjectsFromModel {

  private final String pathProperty;
  private final String referenceProperty;

  public SingleContent(String pathProperty, String referenceProperty) {
    this.pathProperty = pathProperty;
    this.referenceProperty = referenceProperty;
  }

  @Override
  public List<DigitalObject> getDigitalObjects(Model model) {
    String path = (String)model.get(pathProperty);
    String reference = String.valueOf(model.get(referenceProperty));
    return Collections.singletonList(DigitalObject.fromFile(reference, new File(path)));
  }

}
