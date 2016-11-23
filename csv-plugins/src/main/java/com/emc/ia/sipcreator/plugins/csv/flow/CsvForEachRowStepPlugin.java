/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.csv.flow;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;
import com.emc.ia.sipcreator.plugins.csv.CsvConfig;

public class CsvForEachRowStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "csv.foreachrow";

  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description(
        "Executes the specified steps once for each row in the specified CSV file after updating the context with the values of the row.")
    .apply(CsvConfig.DOCUMENTATION)
    .param("steps", Step.class, "The steps that should be repeated.")
    .build();

  public CsvForEachRowStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Step create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    CsvConfig csvConfig = CsvConfig.fromConfig(NAME, context);
    Step continuation = configUtils.newObject("sequence", context, "steps", Step.class);
    return new CsvForEachRowStep(csvConfig, continuation);

  }

}
