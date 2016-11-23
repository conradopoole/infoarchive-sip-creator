/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.digitalobjects;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class NoContentPlugin extends AbstractPluginImpl<DigitalObjectsFromModel> {

  private static final String NAME = "no";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(DigitalObjectsFromModel.class)
    .description("Extracts no digital objects from a model.")
    .build();

  public NoContentPlugin() {
    super(NAME, DigitalObjectsFromModel.class, DOCUMENTATION);
  }

  @Override
  public DigitalObjectsFromModel create(PluginContext context) {
    return new NoContent();
  }

}
