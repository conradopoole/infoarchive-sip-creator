/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.steps.output;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Matchers;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.LogEventTextContains;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

public class EchoStepPluginTests extends PluginTestBase {

  private static final String ECHO = "echo";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(ECHO));
    chk.name(ECHO);
    chk.type(Step.class);
    chk.description("Outputs the specified message.");
    chk.param(".", RuntimeVariable.class, "The message to output.");
  }

  @Test
  public void runOutputsMessage() {
    PluginContext context = context("echo1.yml", ECHO);

    final Appender<ILoggingEvent> mockAppender = mockAppender();

    Step echo = newInstance(Step.class, ECHO, context);
    echo.run(state("name", "world"));

    verify(mockAppender).doAppend(Matchers.argThat(LogEventTextContains.of("Hello world")));
  }

}
