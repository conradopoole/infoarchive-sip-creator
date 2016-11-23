/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.sip;

import java.util.Iterator;

import com.emc.ia.sdk.sip.assembly.DigitalObject;
import com.emc.ia.sdk.sip.assembly.DigitalObjectsExtraction;
import com.emc.ia.sipcreator.api.AIU;

public class AIUDigitalObjectsExtractor implements DigitalObjectsExtraction<AIU> {

  @Override
  public Iterator<? extends DigitalObject> apply(AIU aiu) {
    return aiu.getDigitalObjects()
      .iterator();
  }

}
