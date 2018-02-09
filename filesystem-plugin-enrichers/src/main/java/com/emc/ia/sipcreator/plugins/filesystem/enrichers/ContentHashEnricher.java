/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.enrichers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.output.NullOutputStream;

import com.emc.ia.sdk.support.io.*;
import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.Model;


public class ContentHashEnricher<T> implements EnrichModelFromDomainObject<T> {

    private static final int BUFFER_SIZE = 4096;


    @Override
    public void enrich(Model model, T from) {
        if (from instanceof File) {
            File file = (File)from;
            final HashAssembler hashAssembler = new SingleHashAssembler(HashFunction.SHA1, Encoding.HEX);
            hashAssembler.initialize();
            try {
                IOStreams.copy(new FileInputStream(file), new NullOutputStream(), BUFFER_SIZE, hashAssembler);
                EncodedHash hash = hashAssembler.get().iterator().next();
                model.set("hashvalue", hash.getValue());
                model.set("hashtype", hash.getHashFunction());
                model.set("hashencoding", hash.getEncoding());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
