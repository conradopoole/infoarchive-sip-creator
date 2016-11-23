/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.runtime.plugins.DefaultPluginRegistry;
import com.emc.ia.sipcreator.spi.PluginRegistry;
import com.emc.ia.sipcreator.spi.Result;
import com.emc.ia.sipcreator.spi.ReturnCodes;

public class HelpPlugins implements Callable<Result> {

  private final Consumer<Plugin> printer;

  public HelpPlugins(Consumer<Plugin> printer) {
    this.printer = printer;
  }

  @Override
  public Result call() throws Exception {
    PluginRegistry pluginRegistry = new DefaultPluginRegistry();
    List<Plugin> plugins = pluginRegistry.getAllPlugins();
    Collections.sort(plugins, (a, b) -> a.getId()
      .compareTo(b.getId()));
    System.out.println("Plugins: "); // NOPMD
    System.out.println(StringUtils.repeat('-', 79)); // NOPMD
    for (Plugin p : plugins) {
      printer.accept(p);
    }
    System.out.println(StringUtils.repeat('=', 79)); // NOPMD

    return Result.of(ReturnCodes.SUCCESS);
  }

}
