/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */

package com.emc.ia.sipcreator.api;

import com.emc.ia.sipcreator.api.doc.PluginDocumentation;

public abstract class AbstractStepPlugin extends AbstractPluginImpl<Step> {

  protected AbstractStepPlugin(String id, PluginDocumentation documentation) {
    super(id, Step.class, documentation);
  }

}
