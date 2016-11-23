/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.xml;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.ModelBatchSerializer;
import com.emc.ia.sipcreator.api.ModelSerializer;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class Model2XMLStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "model2xml";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Converts models into XML.")
    .param("source", ModelSource.class, "The source of model instances.")
    .param("serializer", ModelSerializer.class, "The model serializer.")
    .param("directory", RuntimeVariable.class, "The directory where to place the serialized file.")
    .build();

  public Model2XMLStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Step create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    ModelSource source = configUtils.newObject(context, "source", ModelSource.class);

    ModelBatchSerializer serializer =
        configUtils.newObject("batchmodelserializer.default", context, "serializer", ModelBatchSerializer.class);
    RuntimeVariable directory = configUtils.getRuntimeVariable(context, "directory");
    // TODO: add variable name
    return new Model2XMLStep(source, serializer, directory);
  }

}
