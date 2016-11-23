/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

public class ExceptionFormatterTestFlow {

  public void entryPoint() {
    try {
      outer();
    } catch (Exception e) {
      throw new RuntimeException("In entrypoint.", e);
    }
  }

  public void outer() {
    try {
      inner();
    } catch (Exception e) {
      throw new RuntimeException("In outer.", e);
    }
  }

  public String inner(String... args) {
    return args[0].substring(1, args[0].length());
  }
}
