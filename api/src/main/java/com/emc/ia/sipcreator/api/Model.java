/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */

package com.emc.ia.sipcreator.api;

public interface Model {

  Object get(String name);

  void set(String name, Object value);

}
