/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.cli;

import java.util.concurrent.Callable;

import com.emc.ia.sipcreator.spi.Result;

public interface SipCreatorCli {

  Callable<Result> fromArgs(String... args);

}
