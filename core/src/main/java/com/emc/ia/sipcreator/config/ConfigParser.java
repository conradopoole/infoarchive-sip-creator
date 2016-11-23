/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.config;

import java.io.IOException;

import com.emc.ia.sipcreator.api.Config;

public interface ConfigParser {

  Config parse(String text) throws IOException;

}
