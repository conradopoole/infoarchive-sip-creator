/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.uriresolver;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.URIResolver;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class DefaultURIResolverPlugin extends AbstractPluginImpl<URIResolver> {

  private static final String NAME = "defaulturiresolver";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(URIResolver.class)
    .description("Loads a resource from the filesystem.")
    .build();

  public DefaultURIResolverPlugin() {
    super(NAME, URIResolver.class, DOCUMENTATION);
  }

  @Override
  public URIResolver create(PluginContext context) {
    return new DefaultURIResolver();
  }

}
