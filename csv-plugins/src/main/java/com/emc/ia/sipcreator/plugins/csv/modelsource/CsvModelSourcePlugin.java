/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.csv.modelsource;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;
import com.emc.ia.sipcreator.plugins.csv.CsvConfig;

public class CsvModelSourcePlugin extends AbstractPluginImpl<ModelSource> {

  private static final String NAME = "csv";

  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(ModelSource.class)
    .description("Creates models from the specified CSV file.")
    .apply(CsvConfig.DOCUMENTATION)
    .build();

  public CsvModelSourcePlugin() {
    super(NAME, ModelSource.class, DOCUMENTATION);
  }

  @Override
  public ModelSource create(PluginContext context) {
    CsvConfig config = CsvConfig.fromConfig(NAME, context);
    return new CsvModelSource(config);
  }
}
