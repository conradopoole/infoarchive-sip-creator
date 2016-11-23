/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PluginDocumentationBuilder {

  private String name;
  private Class<?> type;
  private final List<PluginParamDocumentation> params = new ArrayList<>();
  private String description;

  public PluginDocumentationBuilder name(String aName) {
    name = aName;
    return this;
  }

  public PluginDocumentationBuilder type(Class<?> aType) {
    type = aType;
    return this;
  }

  public PluginDocumentationBuilder description(String aDescription) {
    description = aDescription;
    return this;
  }

  public PluginDocumentationBuilder apply(Consumer<PluginDocumentationBuilder> proc) {
    proc.accept(this);
    return this;
  }

  public PluginDocumentation build() {
    return new PluginDocumentation(name, description, type, params);
  }

  public PluginDocumentationBuilder param(String aName, Class<?> aType, String aDescription) {
    params.add(new PluginParamDocumentation(aName, aType, aDescription, null, false));
    return this;
  }

  public PluginDocumentationBuilder param(String aName, Class<?> aType, String aDescription, String defaultValue) {
    params.add(new PluginParamDocumentation(aName, aType, aDescription, defaultValue, false));
    return this;
  }

  public PluginDocumentationBuilder param(String aName, Class<?> aType, String aDescription, String defaultValue,
      boolean multivalued) {
    params.add(new PluginParamDocumentation(aName, aType, aDescription, defaultValue, multivalued));
    return this;
  }

  public static PluginDocumentationBuilder builder() {
    return new PluginDocumentationBuilder();
  }
}
