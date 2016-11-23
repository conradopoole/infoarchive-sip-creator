/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.util.List;

public interface Config {

  String getName();

  Object getValue(String id);

  Object getValue();

  List<Config> getChildGroups();

  boolean isCompound();

  boolean hasValue();

}
