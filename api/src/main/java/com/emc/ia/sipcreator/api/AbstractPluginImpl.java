/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.util.Objects;

import com.emc.ia.sipcreator.api.doc.PluginDocumentation;

public abstract class AbstractPluginImpl<T> implements Plugin {

  private final String id;
  private final Class<T> type;
  private final PluginDocumentation documentation;

  protected AbstractPluginImpl(String id, Class<T> type, PluginDocumentation documentation) {
    this.id = Objects.requireNonNull(id);
    this.type = Objects.requireNonNull(type);
    this.documentation = Objects.requireNonNull(documentation);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Class<T> getType() {
    return type;
  }

  @Override
  public PluginDocumentation getDocumentation() {
    return documentation;
  }
}
