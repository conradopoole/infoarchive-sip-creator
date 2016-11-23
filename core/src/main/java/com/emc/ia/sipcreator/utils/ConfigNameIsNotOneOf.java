/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.Set;
import java.util.function.Predicate;

import com.emc.ia.sipcreator.api.Config;

public final class ConfigNameIsNotOneOf implements Predicate<Config> {

  private final Set<String> names;

  public ConfigNameIsNotOneOf(Set<String> names) {
    Params.notNull(names, "ConfigNameIsNotOneOf.names");
    this.names = names;
  }

  @Override
  public boolean test(Config config) {
    return !names.contains(config.getName());
  }

  @Override
  public String toString() {
    return "ConfigNameIsNotOneOf [names=" + names + "]";
  }

}
