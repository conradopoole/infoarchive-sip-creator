/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.enrichers;

import java.io.File;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;


public class BasicAttributesEnricherPlugin extends AbstractPluginImpl<EnrichModelFromDomainObject>  {

    private static final String NAME = "basic.attributes";
    private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
            .name(NAME)
            .type(EnrichModelFromDomainObject.class)
            .description("Enriches the model with basic file attributes")
            .build();

    public BasicAttributesEnricherPlugin() {
        super(NAME, EnrichModelFromDomainObject.class, DOCUMENTATION);
    }


    @Override
    public Object create(PluginContext context) {
        return new BasicAttributesEnricher<File>();
    }
}
