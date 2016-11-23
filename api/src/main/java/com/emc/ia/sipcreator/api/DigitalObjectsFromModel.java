/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */

package com.emc.ia.sipcreator.api;

import java.util.List;

import com.emc.ia.sdk.sip.assembly.DigitalObject;

public interface DigitalObjectsFromModel {

  List<DigitalObject> getDigitalObjects(Model value);

}
