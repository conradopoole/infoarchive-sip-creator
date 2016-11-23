/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SipCreatorLoggers {

  private SipCreatorLoggers() {
    throw new IllegalStateException(
        "SipCreatorLoggers is a static utility class and its constructor should never be called.");
  }

  // // Public loggers
  // public static final Logger CONFIG =
  // LoggerFactory.getLogger("hip.ca.config");
  public static final Logger INIT = LoggerFactory.getLogger("sipcreator.init");

  // public static final Logger RESOURCE =
  // LoggerFactory.getLogger("hip.ca.resource");

}
