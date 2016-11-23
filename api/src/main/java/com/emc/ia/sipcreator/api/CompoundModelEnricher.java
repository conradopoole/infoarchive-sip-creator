/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class CompoundModelEnricher<T> implements EnrichModelFromDomainObject<T> {

  private final List<EnrichModelFromDomainObject<T>> enrichers;

  public CompoundModelEnricher(List<EnrichModelFromDomainObject<T>> enrichers) {
    this.enrichers = ImmutableList.copyOf(enrichers);
  }

  @Override
  public void enrich(Model model, T from) {
    for (EnrichModelFromDomainObject<T> enricher : enrichers) {
      enricher.enrich(model, from);
    }
  }

}
