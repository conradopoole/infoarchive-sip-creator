/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.steps.flow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class WhenStepTests extends PluginTestBase {

  private static final String RESULT = "result";
  private static final String WHEN = "when";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(WHEN));

    chk.name(WHEN);
    chk.type(Step.class);
    chk.description("Performs a set of steps only if a condition is fulfilled.");
    chk.param("test", String.class, "The condition expression");
    chk.param("steps", Step.class, "The steps that should be repeated.");
  }

  @Test
  public void runConditionIsFalseDoNothing() {
    PluginContext context = context("when.yml", WHEN);
    Step step = newInstance(Step.class, WHEN, context);
    RuntimeState state = state(RESULT, "Don't do it");
    step.run(state);
    assertEquals("Don't do it", state.get(RESULT));
  }

  @Test
  public void runConditionIsTrueDoNothing() {
    PluginContext context = context("when.yml", WHEN);
    Step step = newInstance(Step.class, WHEN, context);
    RuntimeState state = state(RESULT, "Do it");
    step.run(state);
    assertEquals("Did it", state.get(RESULT));
  }

}
