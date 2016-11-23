/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginParamDocumentation;

public final class PluginDocUtils {

  private static final String NOT_AVAILABLE = "N/A";

  private PluginDocUtils() {
    throw new IllegalStateException(
        "PluginDocUtils is a static utility class and its constructor should never be called.");
  }

  public static String typeOf(PluginDocumentation documentation, Plugin p) {
    if (documentation == null) {
      return shortenType(p.getType());
    } else {
      return shortenType(ObjectUtils.defaultIfNull(documentation.getType(), p.getType()));
    }
  }

  public static String shortenType(Class<?> type) {
    String simpleName = type.getSimpleName();
    if (StringUtils.isBlank(simpleName)) {
      String fullName = type.getName();
      return StringUtils.defaultString(StringUtils.substringAfterLast(fullName, "."), fullName);
    } else {
      return simpleName;
    }
  }

  public static String nameOf(PluginDocumentation documentation, Plugin p) {
    if (documentation == null) {
      return p.getId();
    } else {
      return StringUtils.defaultIfBlank(documentation.getName(), p.getId());
    }
  }

  public static String nameOf(PluginParamDocumentation paramDoc) {
    if (paramDoc == null) {
      return NOT_AVAILABLE;
    } else {
      return StringUtils.defaultIfBlank(paramDoc.getName(), NOT_AVAILABLE);
    }
  }

  public static String typeOf(PluginParamDocumentation paramDoc) {
    Class<?> type = paramDoc.getType();
    if (type == null) {
      return NOT_AVAILABLE;
    } else {
      return shortenType(paramDoc.getType());
    }
  }

  public static String descriptionOf(PluginParamDocumentation paramDoc) {
    return StringUtils.defaultIfBlank(paramDoc.getDescription(), NOT_AVAILABLE);
  }

  public static String defaultValueOf(PluginParamDocumentation paramDoc) {
    return "Default value: " + StringUtils.defaultString(paramDoc.getDefaultValue(), NOT_AVAILABLE);
  }

  public static String descriptionOf(PluginDocumentation documentation) {
    if (documentation == null) {
      return NOT_AVAILABLE;
    } else {
      return StringUtils.defaultString(documentation.getDescription(), NOT_AVAILABLE);
    }
  }
}
