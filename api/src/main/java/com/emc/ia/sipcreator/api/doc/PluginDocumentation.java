/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api.doc;

import java.util.List;

public class PluginDocumentation {

  private final String name;
  private final Class<?> type;
  private final String description;
  private List<PluginParamDocumentation> params;

  public PluginDocumentation(String name, String description, Class<?> type, List<PluginParamDocumentation> params) {
    this.name = name;
    this.description = description;
    this.type = type;
    this.params = params;
  }

  public List<PluginParamDocumentation> getParams() {
    return params;
  }

  public void setParams(List<PluginParamDocumentation> params) {
    this.params = params;
  }

  public String getName() {
    return name;
  }

  public Class<?> getType() {
    return type;
  }

  public static PluginDocumentationBuilder builder() {
    return new PluginDocumentationBuilder();
  }

  public String getDescription() {
    return description;
  }

}
