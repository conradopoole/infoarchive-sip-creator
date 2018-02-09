/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.enrichers;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;


public class ContentHashEnricherPlugin extends AbstractPluginImpl<EnrichModelFromDomainObject>  {

    private static final String NAME = "content.hash";
    private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
            .name(NAME)
            .type(EnrichModelFromDomainObject.class)
            .description("Enriches the model with the file's content hash.")
            .build();

    public ContentHashEnricherPlugin() {
        super(NAME, EnrichModelFromDomainObject.class, DOCUMENTATION);
    }


    @Override
    public Object create(PluginContext context) {
        return new ContentHashEnricher<>();
    }
}
