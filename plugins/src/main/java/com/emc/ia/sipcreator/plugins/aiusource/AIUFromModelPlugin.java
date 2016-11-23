/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.aiusource;

import com.emc.ia.sipcreator.api.AIUSource;
import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.ModelSerializer;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;
import com.emc.ia.sipcreator.plugins.digitalobjects.NoContent;

public class AIUFromModelPlugin extends AbstractPluginImpl<AIUSource> {

  private static final String NAME = "aiuFromModel";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(AIUSource.class)
    .description("Converts a collection of models to AIUs")
    .param("models", ModelSource.class, "The model source.")
    .param("serializer", ModelSerializer.class, "A serializer that knows how to serialize this model to XML.")
    .param("content", DigitalObjectsFromModel.class, "The digital object extrator.", "no")
    .build();

  public AIUFromModelPlugin() {
    super(NAME, AIUSource.class, DOCUMENTATION);
  }

  @Override
  public AIUSource create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    ModelSource modelSource = configUtils.newObject(context, "models", ModelSource.class);
    ModelSerializer serializer = configUtils.newObject(context, "serializer", ModelSerializer.class);
    DigitalObjectsFromModel extractor = configUtils.newOptionalObject(context, "content", DigitalObjectsFromModel.class)
      .orElse(new NoContent());
    return new ModelAIUSource(serializer, extractor, modelSource);
  }

}
