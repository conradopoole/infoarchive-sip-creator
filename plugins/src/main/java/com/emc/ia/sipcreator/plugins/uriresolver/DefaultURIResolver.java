/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.uriresolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.emc.ia.sipcreator.api.URIResolver;

public class DefaultURIResolver implements URIResolver {

  @Override
  public InputStream open(String uri) throws IOException {
    return new FileInputStream(new File(uri));
  }

}
