/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.transformation;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.ModelTransformation;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class TransformingModelSourcePlugin extends AbstractPluginImpl<ModelSource> {

  private static final String NAME = "transformModel";

  private static final String PARAM_SOURCE = "source";

  private static final String PARAM_TRANSFORMATION = "transformation";

  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(ModelSource.class)
    .description("A model source that consists of transformed models from another source.")
    .param(PARAM_SOURCE, ModelSource.class, "The models to transform.")
    .param(PARAM_TRANSFORMATION, ModelTransformation.class, "The transformation to apply to the models.")
    .build();

  public TransformingModelSourcePlugin() {
    super(NAME, ModelSource.class, DOCUMENTATION);
  }

  @Override
  public ModelSource create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    ModelSource source = configUtils.newObject(context, PARAM_SOURCE, ModelSource.class);
    ModelTransformation transform = configUtils.newObject(context, PARAM_TRANSFORMATION, ModelTransformation.class);
    return new TransformingModelSource(source, transform);
  }

}
