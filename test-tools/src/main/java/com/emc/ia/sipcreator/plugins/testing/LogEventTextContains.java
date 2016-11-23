/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.testing;

import org.mockito.ArgumentMatcher;

import ch.qos.logback.classic.spi.LoggingEvent;

public class LogEventTextContains extends ArgumentMatcher<LoggingEvent> {

  private final String text;

  public LogEventTextContains(String text) {
    this.text = text;
  }

  @Override
  public boolean matches(final Object argument) {
    LoggingEvent event = (LoggingEvent)argument;
    String msg = event.getFormattedMessage();
    return msg.contains(text);
  }

  public static ArgumentMatcher<LoggingEvent> of(String text) {
    return new LogEventTextContains(text);
  }

}
