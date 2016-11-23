/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

public class InvalidCommandLine extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidCommandLine(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidCommandLine(String message) {
    super(message);
  }

}
