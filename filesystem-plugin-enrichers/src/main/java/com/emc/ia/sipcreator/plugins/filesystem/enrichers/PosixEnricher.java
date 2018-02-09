/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.enrichers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;

import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.Model;


public class PosixEnricher<T> implements EnrichModelFromDomainObject<T> {
    @Override
    public void enrich(Model model, T from) {

        //POSIX ATTRIBUTES
        if (from instanceof File) {
            File file = (File)from;

            HashMap<String, String> posixAttributes = new HashMap<>();
            try {
                PosixFileAttributes posixAttr = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
                posixAttributes.put("owner", posixAttr.owner().getName());
                posixAttributes.put("group", posixAttr.group().getName());
                posixAttributes.put("permissions", PosixFilePermissions.toString(posixAttr.permissions()));
                model.set("posixattributes", posixAttributes);
            } catch (IOException e) {
            }
        }
    }

}
