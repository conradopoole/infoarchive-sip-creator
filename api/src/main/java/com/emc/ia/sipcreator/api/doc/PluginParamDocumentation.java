/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api.doc;

public class PluginParamDocumentation {

  private final String name;
  private final Class<?> type;
  private final String description;
  private final String defaultValue;
  private final boolean multivalued;

  public PluginParamDocumentation(String name, Class<?> type, String description, String defaultValue,
      boolean multivalued) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.defaultValue = defaultValue;
    this.multivalued = multivalued;
  }

  public String getName() {
    return name;
  }

  public Class<?> getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public boolean isMultivalued() {
    return multivalued;
  }

}
