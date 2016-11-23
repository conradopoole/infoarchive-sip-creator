/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.emc.ia.sipcreator.spi.Result;
import com.emc.ia.sipcreator.spi.ReturnCodes;

import joptsimple.OptionParser;

public class HelpCommand implements Callable<Result> {

  private final OptionParser parser;

  public HelpCommand(OptionParser parser) {
    this.parser = parser;
  }

  @Override
  public Result call() throws Exception {
    try {
      System.out.println("USAGE: sipcreator [option...]"); // NOPMD
      System.out.println(); // NOPMD
      parser.printHelpOn(System.out);
    } catch (IOException e) {
      System.err.println("Failed to print help message."); // NOPMD
      e.printStackTrace(); // NOPMD
    }
    return Result.of(ReturnCodes.SUCCESS);
  }

}
