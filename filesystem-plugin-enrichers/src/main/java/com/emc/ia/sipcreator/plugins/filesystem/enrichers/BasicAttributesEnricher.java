/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.enrichers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.Model;



public class BasicAttributesEnricher<T> implements EnrichModelFromDomainObject<T> {
    @Override
    public void enrich(Model model, T from) {

        //BASIC ATTRIBUTES
        if (from instanceof File) {
            File file = (File)from;

            HashMap<String, String> basicAttributes = new HashMap<>();
            try {
                BasicFileAttributes basicAttr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                basicAttributes.put("creationtime", basicAttr.creationTime().toString());
                basicAttributes.put("lastaccesstime", basicAttr.lastAccessTime().toString());
                basicAttributes.put("lastmodifiedtime", basicAttr.lastModifiedTime().toString());
                model.set("basicattributes", basicAttributes);
            } catch (IOException e) {

            }
        }
    }

}
