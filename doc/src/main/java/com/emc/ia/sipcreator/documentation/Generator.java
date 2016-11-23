/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.documentation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.runtime.plugins.DefaultPluginRegistry;
import com.emc.ia.sipcreator.spi.PluginRegistry;

public final class Generator {

  private Generator() {
    throw new IllegalStateException("Generator is a static utility class and its constructor should never be called.");
  }

  public static void main(String[] args) {
    PluginRegistry pluginRegistry = new DefaultPluginRegistry();

    File file = new File(args[0]);

    file.mkdirs();

    try (PrintWriter out = new PrintWriter(
        new OutputStreamWriter(new FileOutputStream(new File(file, "readme.md")), StandardCharsets.UTF_8))) {
      out.println("= SIP Creator Documentation");
      out.println();

      // TODO: add command line details
      // TODO: add configuration file format details

      List<Plugin> plugins = pluginRegistry.getAllPlugins();

      out.println("== Plugins");
      out.println();
      for (Plugin plugin : plugins) {
        out.println("* " + plugin.getId());
      }

      // TODO add plugin details

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
