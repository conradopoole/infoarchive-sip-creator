/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.enrichers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.ArrayList;
import java.util.List;

import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.Model;


public class AclAttributesEnricher<T> implements EnrichModelFromDomainObject<T> {
    @Override
    public void enrich(Model model, T from) {

        //ACL ATTRIBUTES
        if (from instanceof File) {
            File file = (File)from;

            List<String> aclAttributes = new ArrayList<>();
            try {
                AclFileAttributeView aclAttr = Files.getFileAttributeView(file.toPath(), AclFileAttributeView.class);
                if (aclAttr != null) {
                    for (AclEntry aclEntry : aclAttr.getAcl()) {
                        aclAttributes.add(aclEntry.toString());
                    }
                }
                model.set("aclattributes", aclAttributes);
            } catch (IOException e) {

            }
        }
    }

}
