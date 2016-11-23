/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

import java.util.UUID;
import java.util.function.Consumer;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;

public class BriefPluginDocumentationPrinter implements Consumer<Plugin> {

  @Override
  public void accept(Plugin p) {
    PluginDocumentation documentation = p.getDocumentation();
    String name = PluginDocUtils.nameOf(documentation, p);
    String type = PluginDocUtils.typeOf(documentation, p);
    String text = String.format("  %-24s %52s", name, type);
    output(text);
    newline();
  }

  protected void newline() {
    System.out.println(); // NOPMD
  }

  protected void output(String text) {
    System.out.print(text); // NOPMD
  }

  public static void main(String[] args) {
    Plugin p = new Plugin() {

      @Override
      public String getId() {
        return UUID.randomUUID()
          .toString();
      }

      @Override
      public Class<?> getType() {
        Class<?> cls = getClass();
        return cls;
      }

      @Override
      public Object create(PluginContext context) {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public PluginDocumentation getDocumentation() {
        // TODO Auto-generated method stub
        return null;
      }
    };
    new BriefPluginDocumentationPrinter().accept(p);
  }
}
