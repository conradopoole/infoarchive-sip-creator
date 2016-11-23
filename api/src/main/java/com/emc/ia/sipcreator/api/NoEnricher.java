/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

public class NoEnricher<T> implements EnrichModelFromDomainObject<T> {

  @Override
  public void enrich(Model model, T from) {
    // NOP
  }

}
