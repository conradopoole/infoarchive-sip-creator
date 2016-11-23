/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.io.Closeable;

public interface HouseKeeper {

  void add(Closeable resource);
}
