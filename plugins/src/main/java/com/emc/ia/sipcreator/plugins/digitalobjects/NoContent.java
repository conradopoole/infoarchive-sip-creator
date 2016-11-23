/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.digitalobjects;

import java.util.Collections;
import java.util.List;

import com.emc.ia.sdk.sip.assembly.DigitalObject;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.Model;

public class NoContent implements DigitalObjectsFromModel {

  @Override
  public List<DigitalObject> getDigitalObjects(Model value) {
    return Collections.emptyList();
  }

}
