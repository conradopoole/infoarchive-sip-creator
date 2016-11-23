/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class FilesStepPlugin extends AbstractStepPlugin {

  private static final String PARAM_VARIABLE = "variable";
  private static final String PARAM_FILES = "files";
  private static final String DEFAULT_VARIABLE = "files";
  private static final String NAME = "filecollection";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Creates / updates a collection of files")
    .param(PARAM_FILES, RuntimeVariable.class, "The files to add to the collection", "", true)
    .param(PARAM_VARIABLE, String.class, "The name of the variable that holds the file collection", PARAM_FILES)
    .build();

  public FilesStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    String variableName = StringUtils.defaultString(configUtils.getString(context, PARAM_VARIABLE), DEFAULT_VARIABLE);
    List<RuntimeVariable> filesVariable = configUtils.getRuntimeVariables(context, PARAM_FILES);
    return new FilesStep(filesVariable, variableName);
  }

}
