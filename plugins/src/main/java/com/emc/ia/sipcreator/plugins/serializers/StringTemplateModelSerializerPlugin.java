/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.serializers;

import java.io.IOException;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSerializer;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class StringTemplateModelSerializerPlugin extends AbstractPluginImpl<ModelSerializer> {

  private static final String NAME = "stringtemplate";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Creates / updates a collection of files")
    .param("template", String.class, "The file header.")
    .build();

  public StringTemplateModelSerializerPlugin() {
    super(NAME, ModelSerializer.class, DOCUMENTATION);
  }

  @Override
  public Object create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    try {
      String templateText = configUtils.getExternalResource(getResourcePath(context, configUtils));
      STGroup group = new STGroup('$', '$');
      group.registerModelAdaptor(Model.class, new ModelSTAdaptor());
      group.defineTemplate("main", "model", templateText);
      ST template = group.getInstanceOf("main");
      return new StringTemplateModelSerializer(template);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String getResourcePath(PluginContext context, ConfigUtils configUtils) {
    if (context.getConfig()
      .isCompound()) {
      return configUtils.getString(context, "template");
    } else {
      return (String)context.getConfig()
        .getValue();
    }

  }
}
