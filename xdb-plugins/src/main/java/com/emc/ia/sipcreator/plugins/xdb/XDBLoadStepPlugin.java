/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class XDBLoadStepPlugin extends AbstractStepPlugin {

  private static final String DEFAULT_XDB = "xdb.default";
  private static final String PARAM_FILES = "files";
  private static final String DEFAULT_FILES = "files";
  private static final String PARAM_XDB = "xdb";
  private static final String PARAM_LIBRARY = "library";
  private static final String NAME = "xdb.load";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Loads the specified files into the specified library in xDB.")
    .param(PARAM_LIBRARY, RuntimeVariable.class, "The name of the xDB library to create.")
    .param(PARAM_XDB, String.class, "The name of the xDB session provider to use.", DEFAULT_XDB)
    .param(PARAM_FILES, String.class, "Name of the variable that holds the files to load.", DEFAULT_FILES)
    .build();

  public XDBLoadStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    RuntimeVariable libraryPathVariable = configUtils.getRuntimeVariable(context, PARAM_LIBRARY);
    String filesVariable = StringUtils.defaultString(configUtils.getString(context, PARAM_FILES), DEFAULT_FILES);
    XDBSessionProvider sessionProvider = context.getObject(XDBSessionProvider.class,
        StringUtils.defaultString(configUtils.getString(context, PARAM_XDB), DEFAULT_XDB));
    return new XDBLoadStep(sessionProvider, libraryPathVariable, filesVariable);
  }

}
