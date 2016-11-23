/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginParamDocumentation;

public class PluginChecker {

  private final Plugin plugin;
  private final PluginDocumentation doc;

  public PluginChecker(Plugin plugin) {
    this.plugin = plugin;
    this.doc = plugin.getDocumentation();
  }

  public void name(String name) {
    assertEquals(name, plugin.getId());
    assertEquals(name, doc.getName());
  }

  public void type(Class<?> type) {
    assertEquals(type, plugin.getType());
    assertEquals(type, doc.getType());
  }

  public void description(String msg) {
    assertEquals(msg, doc.getDescription());
  }

  public void param(String name, Class<?> type, String description) {
    param(name, type, description, null);
  }

  public void param(String name, Class<?> type, String description, String defaultValue) {
    param(name, type, description, defaultValue, false);
  }

  public void param(String name, Class<?> type, String description, String defaultValue, boolean repeatable) {
    PluginParamDocumentation param = paramDoc(name);
    assertEquals(name, param.getName());
    assertEquals(type, param.getType());
    assertEquals(description, param.getDescription());
    assertEquals(defaultValue, param.getDefaultValue());
    assertEquals(repeatable, param.isMultivalued());
  }

  private PluginParamDocumentation paramDoc(String name) {
    for (PluginParamDocumentation pdoc : doc.getParams()) {
      if (name.equals(pdoc.getName())) {
        return pdoc;
      }
    }
    assertTrue("No documentation found for parameter called " + name + " for plugin " + plugin, false);
    return null;
  }
}
