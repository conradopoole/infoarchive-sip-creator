/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.io.IOException;
import java.io.Writer;

public interface ModelSerializer {

  void serialize(Model model, Writer writer) throws IOException;

}
