/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.runtime.plugins.DefaultPluginRegistry;
import com.emc.ia.sipcreator.spi.PluginRegistry;
import com.emc.ia.sipcreator.spi.Result;
import com.emc.ia.sipcreator.spi.ReturnCodes;

public class HelpPluginById implements Callable<Result> {

  private final String id;
  private final Consumer<Plugin> printer;

  public HelpPluginById(String id, Consumer<Plugin> printer) {
    this.id = id;
    this.printer = printer;
  }

  @Override
  public Result call() throws Exception {
    PluginRegistry pluginRegistry = new DefaultPluginRegistry();

    List<Plugin> plugins = pluginRegistry.getPluginsById(id);
    Collections.sort(plugins, (a, b) -> typeOf(a).compareTo(typeOf(b)));
    System.out.println("Plugins with id = '" + id + "': "); // NOPMD
    for (Plugin p : plugins) {
      printer.accept(p);
    }

    return Result.of(ReturnCodes.SUCCESS);
  }

  private String typeOf(Plugin a) {
    return a.getType()
      .getSimpleName();
  }

}
