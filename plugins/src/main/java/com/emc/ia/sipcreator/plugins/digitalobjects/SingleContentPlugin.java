/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.digitalobjects;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class SingleContentPlugin extends AbstractPluginImpl<DigitalObjectsFromModel> {

  private static final String NAME = "single";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(DigitalObjectsFromModel.class)
    .description("Extracts a single digital object from a model.")
    .param("path", String.class, "The property of the model that holds the path.")
    .param("reference", String.class, "The property of the model that holds the reference")

    .build();

  public SingleContentPlugin() {
    super(NAME, DigitalObjectsFromModel.class, DOCUMENTATION);
  }

  @Override
  public DigitalObjectsFromModel create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    String pathProperty = configUtils.getString(context, "path");
    String referenceProperty = configUtils.getString(context, "reference");
    return new SingleContent(pathProperty, referenceProperty);
  }

}
