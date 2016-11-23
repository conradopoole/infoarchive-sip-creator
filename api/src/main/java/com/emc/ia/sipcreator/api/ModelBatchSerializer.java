/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */

package com.emc.ia.sipcreator.api;

import java.io.IOException;

import com.emc.ia.sdk.support.io.DataBuffer;

public interface ModelBatchSerializer {

  void open(DataBuffer buffer) throws IOException;

  void serialize(Model model) throws IOException;

  void end() throws IOException;

}
