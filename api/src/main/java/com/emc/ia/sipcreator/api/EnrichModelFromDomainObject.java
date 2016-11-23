/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

public interface EnrichModelFromDomainObject<T> {

  void enrich(Model model, T from);
}
