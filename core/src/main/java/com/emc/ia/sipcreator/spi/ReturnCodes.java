/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.spi;

public final class ReturnCodes {

  public static final int ERR_GENERAL = 1;
  public static final int SUCCESS = 0;
  public static final int ERR_USAGE = 64;

  private ReturnCodes() {
    throw new IllegalStateException(
        "ReturnCodes is a static utility class and its constructor should never be called.");
  }

}
