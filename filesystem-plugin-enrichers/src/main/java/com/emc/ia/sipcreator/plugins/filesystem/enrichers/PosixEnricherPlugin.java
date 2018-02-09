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


public class PosixEnricherPlugin extends AbstractPluginImpl<EnrichModelFromDomainObject>  {

    private static final String NAME = "posix.attributes";
    private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
            .name(NAME)
            .type(EnrichModelFromDomainObject.class)
            .description("Enriches the model with POSIX file attributes")
            .build();

    public PosixEnricherPlugin() {
        super(NAME, EnrichModelFromDomainObject.class, DOCUMENTATION);
    }


    @Override
    public Object create(PluginContext context) {
        return new PosixEnricher<File>();
    }
}
