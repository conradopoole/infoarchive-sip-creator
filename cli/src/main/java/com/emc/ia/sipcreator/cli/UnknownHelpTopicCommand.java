/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

import java.util.concurrent.Callable;

import com.emc.ia.sipcreator.spi.Result;
import com.emc.ia.sipcreator.spi.ReturnCodes;

public class UnknownHelpTopicCommand implements Callable<Result> {

  private final String topic;

  public UnknownHelpTopicCommand(String topic) {
    this.topic = topic;
  }

  @Override
  public Result call() throws Exception {
    System.out.println("There is no help available on the topic \"" + topic + "\""); // NOPMD
    return Result.of(ReturnCodes.SUCCESS);
  }

}
