/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface URIResolver {

  InputStream open(String uri) throws IOException;
}
