/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator;

import java.util.concurrent.Callable;

import com.emc.ia.sipcreator.cli.DefaultCommandLineInitiator;
import com.emc.ia.sipcreator.exceptions.ConfigurationException;
import com.emc.ia.sipcreator.spi.Result;
import com.emc.ia.sipcreator.spi.ReturnCodes;

public final class Start {

  private Start() {
    throw new IllegalStateException("Start is a static utility class and its constructor should never be called.");
  }

  public static void main(String[] args) {
    int returnCode = ReturnCodes.ERR_GENERAL;
    try {

      DefaultCommandLineInitiator cli = new DefaultCommandLineInitiator();
      Callable<Result> app = cli.parse(args);
      Result result = app.call();
      returnCode = result.getReturnCode();
    } catch (ConfigurationException e) {
      System.err.println("Configuration error."); // NOPMD
      System.err.println(e.getMessage()); // NOPMD
      returnCode = ReturnCodes.ERR_USAGE;
    } catch (Exception e) {
      System.err.println("Internal error."); // NOPMD
      System.err.println("Dumping stacktrace."); // NOPMD
      e.printStackTrace(); // NOPMD
    }
    System.exit(returnCode); // NOPMD
  }

}
