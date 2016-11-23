/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.serializers;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.ModelBatchSerializer;
import com.emc.ia.sipcreator.api.ModelSerializer;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class BatchModelSerializerPlugin extends AbstractPluginImpl<ModelBatchSerializer> {

  private static final String NAME = "batchmodelserializer.default";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Creates / updates a collection of files")
    .param("header", String.class, "The file header.")
    .param("footer", String.class, "The file footer.")
    .build();

  public BatchModelSerializerPlugin() {
    super(NAME, ModelBatchSerializer.class, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    String header = configUtils.getString(context, "header");
    String footer = configUtils.getString(context, "footer");
    ModelSerializer serializer =
        configUtils.newObject(context, configUtils.groupSelectorFirstNot("header", "footer"), ModelSerializer.class);
    return new BatchModelSerializerImpl(header, footer, serializer);
  }

}
