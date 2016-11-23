/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.state;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.URIResolver;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class LoadPropertiesPlugin extends AbstractStepPlugin {

  private static final String NAME = "properties";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Loads a set of properties.")
    .param("file", RuntimeVariable.class, "The property file to load.")
    .param("uriresolver", String.class, "The name of a URIResolver.", "uriresolver.default")
    .param("prefix", RuntimeVariable.class,
        "An optional prefix which will be prepended to the name of each loaded property.", "")
    .build();

  public LoadPropertiesPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    RuntimeVariable propertyFileVariable = configUtils.getRuntimeVariable(context, "file");
    URIResolver resolver = context.getObject(URIResolver.class,
        StringUtils.defaultString(configUtils.getString(context, "uriresolver"), "uriresolver.default"));
    RuntimeVariable prefixVariable =
        configUtils.getRuntimeVariable(StringUtils.defaultString(configUtils.getString(context, "prefix"), ""));

    return new LoadPropertiesStep(configUtils, propertyFileVariable, resolver, prefixVariable);
  }

}
