/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.spi;

public class Result {

  private final int returnCode;
  private final String returnMessage;

  public Result(int returnCode, String returnMessage) {
    this.returnCode = returnCode;
    this.returnMessage = returnMessage;
  }

  public int getReturnCode() {
    return returnCode;
  }

  public String getReturnMessage() {
    return returnMessage;
  }

  public static Result of(int returnCode) {
    return new Result(returnCode, "");
  }

  @Override
  public String toString() {
    return "Result [returnCode=" + returnCode + ", returnMessage=" + returnMessage + "]";
  }

}
