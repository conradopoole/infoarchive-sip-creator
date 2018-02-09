/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.enrichers;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;


public class AclAttributesEnricherPlugin extends AbstractPluginImpl<EnrichModelFromDomainObject>  {

    private static final String NAME = "acl.attributes";
    private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
            .name(NAME)
            .type(EnrichModelFromDomainObject.class)
            .description("Enriches the model with ACL file attributes")
            .build();

    public AclAttributesEnricherPlugin() {
        super(NAME, EnrichModelFromDomainObject.class, DOCUMENTATION);
    }


    @Override
    public Object create(PluginContext context) {
        return new AclAttributesEnricher<>();
    }
}
