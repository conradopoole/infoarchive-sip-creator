/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

import java.util.List;
import java.util.function.Consumer;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginParamDocumentation;

public class FullPluginDocumentationPrinter implements Consumer<Plugin> {

  @Override
  public void accept(Plugin plugin) {
    PluginDocumentation documentation = plugin.getDocumentation();

    String name = PluginDocUtils.nameOf(documentation, plugin);
    String type = PluginDocUtils.typeOf(documentation, plugin);
    String description = PluginDocUtils.descriptionOf(documentation);

    output("* ");
    output(name);
    output(" (");
    output(type);
    output(")");
    newline();
    output(description);
    newline();

    newline();
    output("** Parameters");
    newline();
    if (documentation == null) {
      output("N/A");
    } else {
      List<PluginParamDocumentation> params = documentation.getParams();
      if (params == null) {
        output("N/A");
      } else {
        for (PluginParamDocumentation paramDoc : params) {
          outputParameterDocumentation(paramDoc);
        }
      }
    }
  }

  private void outputParameterDocumentation(PluginParamDocumentation paramDoc) {
    String paramName = PluginDocUtils.nameOf(paramDoc);
    String paramType = PluginDocUtils.typeOf(paramDoc);
    String paramDescription = PluginDocUtils.descriptionOf(paramDoc);
    String paramDefaultValue = PluginDocUtils.defaultValueOf(paramDoc);

    newline();
    output("*** ");
    output(paramName);
    output(" (");
    output(paramType);
    output(")");
    newline();

    output(paramDescription);
    newline();
    output(paramDefaultValue);
    newline();
  }

  private void newline() {
    System.out.println(); // NOPMD
  }

  protected void output(String text) {
    System.out.print(text); // NOPMD
  }
}
